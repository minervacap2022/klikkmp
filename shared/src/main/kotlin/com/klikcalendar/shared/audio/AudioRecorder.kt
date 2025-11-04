package com.klikcalendar.shared.audio

/**
 * Platform-agnostic audio recorder interface.
 * Implementations are provided for each platform (iOS, Android).
 */
interface AudioRecorder {
    /**
     * Start audio recording.
     * @return true if recording started successfully
     */
    suspend fun startRecording(): Boolean

    /**
     * Stop audio recording and return the recorded audio data.
     * @return Recorded audio data as ByteArray, or null if recording failed
     */
    suspend fun stopRecording(): ByteArray?

    /**
     * Check if currently recording.
     */
    fun isRecording(): Boolean

    /**
     * Request necessary permissions for audio recording.
     * @return true if permissions are granted
     */
    suspend fun requestPermissions(): Boolean

    /**
     * Check if audio recording permissions are granted.
     */
    suspend fun hasPermissions(): Boolean
}

/**
 * Platform-specific factory for creating AudioRecorder instances.
 */
// Removed 'expect' keyword for Android-only build
// expect fun createAudioRecorder(): AudioRecorder

/**
 * Recording state for UI display.
 */
data class RecordingState(
    val isRecording: Boolean = false,
    val duration: Long = 0L, // Duration in milliseconds
    val error: String? = null,
)
