package com.klikcalendar.shared.data.backend

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json

/**
 * Backend API Client for communicating with the audio processing pipeline.
 * Base URL: http://86.38.238.159:8000
 *
 * Usage:
 * 1. Upload audio file: uploadAudio(audioData)
 * 2. Poll status: pollUntilComplete(runId)
 * 3. Get results: getStatus(runId)
 */
class BackendApiClient(
    private val baseUrl: String = "http://86.38.238.159:8000",
) {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
                prettyPrint = true
            })
        }
    }

    /**
     * Health check endpoint to verify backend is accessible.
     * GET /api/pipeline/health
     */
    suspend fun healthCheck(): Result<Map<String, Any>> {
        return try {
            val response: Map<String, Any> = client.get("$baseUrl/api/pipeline/health").body()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Upload audio file and start pipeline execution.
     * POST /api/pipeline/execute
     *
     * @param audioData Raw audio file data
     * @param fileName File name (e.g., "recording.m4a")
     * @param sessionId Optional session ID
     * @param enablePreprocessing Whether to enable audio preprocessing (default: true)
     * @return Upload response containing runId
     */
    suspend fun uploadAudio(
        audioData: ByteArray,
        fileName: String = "recording.m4a",
        sessionId: String? = null,
        enablePreprocessing: Boolean = true,
    ): Result<UploadResponse> {
        return try {
            val response: UploadResponse = client.submitFormWithBinaryData(
                url = "$baseUrl/api/pipeline/execute",
                formData = formData {
                    append("file", audioData, Headers.build {
                        append(HttpHeaders.ContentType, "audio/m4a")
                        append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
                    })
                    if (sessionId != null) {
                        append("session_id", sessionId)
                    }
                    append("enable_preprocessing", enablePreprocessing.toString().capitalize())
                }
            ).body()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get pipeline execution status.
     * GET /api/pipeline/status/{runId}
     *
     * @param runId Run ID from upload response
     * @return Status response with results when completed
     */
    suspend fun getStatus(runId: String): Result<StatusResponse> {
        return try {
            val response: StatusResponse = client.get("$baseUrl/api/pipeline/status/$runId").body()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Poll status until pipeline completes or fails.
     * Polls every `pollIntervalMs` milliseconds.
     *
     * @param runId Run ID from upload response
     * @param pollIntervalMs Interval between polls in milliseconds (default: 3000)
     * @param maxAttempts Maximum number of poll attempts (default: 200, ~10 minutes)
     * @param onProgress Callback invoked on each poll with current status
     * @return Final status response when completed/failed
     */
    suspend fun pollUntilComplete(
        runId: String,
        pollIntervalMs: Long = 3000,
        maxAttempts: Int = 200,
        onProgress: ((StatusResponse) -> Unit)? = null,
    ): Result<StatusResponse> {
        repeat(maxAttempts) { attempt ->
            val statusResult = getStatus(runId)

            if (statusResult.isFailure) {
                return statusResult
            }

            val status = statusResult.getOrThrow()
            onProgress?.invoke(status)

            when (status.status) {
                PipelineStatus.COMPLETED -> return Result.success(status)
                PipelineStatus.FAILED -> return Result.failure(
                    Exception(status.error ?: "Pipeline failed")
                )
                PipelineStatus.RUNNING, PipelineStatus.STARTED -> {
                    // Continue polling
                    delay(pollIntervalMs)
                }
            }
        }

        return Result.failure(Exception("Polling timeout after $maxAttempts attempts"))
    }

    /**
     * List all pipeline runs.
     * GET /api/pipeline/runs
     */
    suspend fun listRuns(): Result<Map<String, Any>> {
        return try {
            val response: Map<String, Any> = client.get("$baseUrl/api/pipeline/runs").body()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Close the HTTP client.
     * Call this when done using the API client.
     */
    fun close() {
        client.close()
    }
}

/**
 * Extension function to capitalize first letter (for "True"/"False" strings).
 */
private fun String.capitalize(): String {
    return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
}
