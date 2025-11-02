package com.klikcalendar.app.state

import com.klikcalendar.app.audio.AudioRecorder
import com.klikcalendar.app.audio.RecordingState
import com.klikcalendar.app.audio.createAudioRecorder
import com.klikcalendar.app.data.backend.BackendApiClient
import com.klikcalendar.app.data.backend.FrontendData
import com.klikcalendar.app.data.backend.PipelineStatus
import com.klikcalendar.app.data.backend.StatusResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

/**
 * View model for managing audio recording and backend processing.
 */
class RecordingViewModel(
    private val scope: CoroutineScope,
) {
    private val audioRecorder: AudioRecorder = createAudioRecorder()
    private val backendClient = BackendApiClient()

    // Recording state
    private val _recordingState = MutableStateFlow(RecordingState())
    val recordingState: StateFlow<RecordingState> = _recordingState.asStateFlow()

    // Processing state
    private val _processingState = MutableStateFlow(ProcessingState())
    val processingState: StateFlow<ProcessingState> = _processingState.asStateFlow()

    // Results
    private val _results = MutableStateFlow<FrontendData?>(null)
    val results: StateFlow<FrontendData?> = _results.asStateFlow()

    private var recordingStartTime: Long = 0L

    /**
     * Request audio recording permissions.
     */
    suspend fun requestPermissions(): Boolean {
        return audioRecorder.requestPermissions()
    }

    /**
     * Check if audio recording permissions are granted.
     */
    suspend fun hasPermissions(): Boolean {
        return audioRecorder.hasPermissions()
    }

    /**
     * Start recording audio.
     */
    fun startRecording() {
        scope.launch {
            try {
                // Check permissions
                if (!audioRecorder.hasPermissions()) {
                    val granted = audioRecorder.requestPermissions()
                    if (!granted) {
                        _recordingState.value = RecordingState(
                            isRecording = false,
                            error = "Microphone permission denied"
                        )
                        return@launch
                    }
                }

                // Start recording
                val success = audioRecorder.startRecording()
                if (success) {
                    recordingStartTime = Clock.System.now().toEpochMilliseconds()
                    _recordingState.value = RecordingState(
                        isRecording = true,
                        duration = 0L
                    )
                } else {
                    _recordingState.value = RecordingState(
                        isRecording = false,
                        error = "Failed to start recording"
                    )
                }
            } catch (e: Exception) {
                _recordingState.value = RecordingState(
                    isRecording = false,
                    error = "Error: ${e.message}"
                )
            }
        }
    }

    /**
     * Stop recording and upload to backend for processing.
     */
    fun stopRecordingAndUpload() {
        scope.launch {
            try {
                // Stop recording
                val audioData = audioRecorder.stopRecording()

                _recordingState.value = RecordingState(
                    isRecording = false,
                    duration = Clock.System.now().toEpochMilliseconds() - recordingStartTime
                )

                if (audioData == null) {
                    _processingState.value = ProcessingState(
                        status = ProcessingStatus.Error,
                        message = "Failed to stop recording"
                    )
                    return@launch
                }

                // Upload to backend
                _processingState.value = ProcessingState(
                    status = ProcessingStatus.Uploading,
                    message = "Uploading audio..."
                )

                val uploadResult = backendClient.uploadAudio(
                    audioData = audioData,
                    fileName = "recording_${Clock.System.now().toEpochMilliseconds()}.m4a"
                )

                if (uploadResult.isFailure) {
                    _processingState.value = ProcessingState(
                        status = ProcessingStatus.Error,
                        message = "Upload failed: ${uploadResult.exceptionOrNull()?.message}"
                    )
                    return@launch
                }

                val uploadResponse = uploadResult.getOrThrow()

                // Start polling for results
                _processingState.value = ProcessingState(
                    status = ProcessingStatus.Processing,
                    message = "Processing audio...",
                    runId = uploadResponse.runId
                )

                pollForResults(uploadResponse.runId)
            } catch (e: Exception) {
                _processingState.value = ProcessingState(
                    status = ProcessingStatus.Error,
                    message = "Error: ${e.message}"
                )
            }
        }
    }

    /**
     * Poll backend for processing results.
     */
    private suspend fun pollForResults(runId: String) {
        val pollResult = backendClient.pollUntilComplete(
            runId = runId,
            pollIntervalMs = 3000,
            onProgress = { status ->
                _processingState.value = ProcessingState(
                    status = ProcessingStatus.Processing,
                    message = "Processing... ${status.logs?.lastOrNull() ?: ""}",
                    runId = runId,
                    progress = estimateProgress(status)
                )
            }
        )

        if (pollResult.isFailure) {
            _processingState.value = ProcessingState(
                status = ProcessingStatus.Error,
                message = "Processing failed: ${pollResult.exceptionOrNull()?.message}",
                runId = runId
            )
            return
        }

        val finalStatus = pollResult.getOrThrow()

        when (finalStatus.status) {
            PipelineStatus.COMPLETED -> {
                // Extract frontend data
                val frontendData = finalStatus.frontendData
                if (frontendData != null) {
                    _results.value = frontendData
                    _processingState.value = ProcessingState(
                        status = ProcessingStatus.Completed,
                        message = "Processing complete!",
                        runId = runId,
                        progress = 100
                    )
                } else {
                    _processingState.value = ProcessingState(
                        status = ProcessingStatus.Error,
                        message = "No results returned",
                        runId = runId
                    )
                }
            }
            PipelineStatus.FAILED -> {
                _processingState.value = ProcessingState(
                    status = ProcessingStatus.Error,
                    message = "Pipeline failed: ${finalStatus.error}",
                    runId = runId
                )
            }
            else -> {
                _processingState.value = ProcessingState(
                    status = ProcessingStatus.Error,
                    message = "Unexpected status: ${finalStatus.status}",
                    runId = runId
                )
            }
        }
    }

    /**
     * Estimate progress based on logs (simplified).
     */
    private fun estimateProgress(status: StatusResponse): Int {
        val logs = status.logs ?: return 10
        // Simple heuristic: more logs = more progress
        return minOf(90, 10 + (logs.size * 2))
    }

    /**
     * Clear results and reset state.
     */
    fun clearResults() {
        _results.value = null
        _processingState.value = ProcessingState()
        _recordingState.value = RecordingState()
    }

    /**
     * Clean up resources.
     */
    fun dispose() {
        backendClient.close()
    }
}

/**
 * Processing state for UI display.
 */
data class ProcessingState(
    val status: ProcessingStatus = ProcessingStatus.Idle,
    val message: String = "",
    val runId: String? = null,
    val progress: Int = 0, // 0-100
)

enum class ProcessingStatus {
    Idle,
    Uploading,
    Processing,
    Completed,
    Error,
}
