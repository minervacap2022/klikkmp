package com.klikcalendar.shared.audio

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Build
import androidx.core.content.ContextCompat
import java.io.File
import java.io.IOException

/**
 * Android implementation of AudioRecorder using MediaRecorder.
 */
fun createAudioRecorder(): AudioRecorder = AndroidAudioRecorder.instance

class AndroidAudioRecorder private constructor(private val context: Context) : AudioRecorder {
    private var mediaRecorder: MediaRecorder? = null
    private var recordingFile: File? = null
    private var recording = false

    override suspend fun startRecording(): Boolean {
        try {
            // Check permissions first
            if (!hasPermissions()) {
                return false
            }

            // Create temporary file
            val timestamp = System.currentTimeMillis()
            recordingFile = File(context.cacheDir, "recording_$timestamp.m4a")

            // Initialize MediaRecorder
            mediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                MediaRecorder(context)
            } else {
                @Suppress("DEPRECATION")
                MediaRecorder()
            }

            mediaRecorder?.apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setAudioEncodingBitRate(128000)
                setAudioSamplingRate(44100)
                setOutputFile(recordingFile?.absolutePath)

                try {
                    prepare()
                    start()
                    recording = true
                    return true
                } catch (e: IOException) {
                    println("Error preparing MediaRecorder: ${e.message}")
                    release()
                    mediaRecorder = null
                    return false
                }
            }

            return false
        } catch (e: Exception) {
            println("Error starting recording: ${e.message}")
            return false
        }
    }

    override suspend fun stopRecording(): ByteArray? {
        try {
            mediaRecorder?.apply {
                stop()
                release()
            }
            mediaRecorder = null
            recording = false

            // Read recorded file
            val file = recordingFile ?: return null
            if (!file.exists()) return null

            val bytes = file.readBytes()

            // Clean up file
            file.delete()
            recordingFile = null

            return bytes
        } catch (e: Exception) {
            println("Error stopping recording: ${e.message}")
            mediaRecorder?.release()
            mediaRecorder = null
            recording = false
            return null
        }
    }

    override fun isRecording(): Boolean = recording

    override suspend fun requestPermissions(): Boolean {
        // Note: Actual permission request needs to be handled by Activity
        // This just checks if permissions are granted
        // The Activity should use ActivityResultContracts.RequestPermission
        return hasPermissions()
    }

    override suspend fun hasPermissions(): Boolean {
        val recordPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED

        return recordPermission
    }

    companion object {
        private var _instance: AndroidAudioRecorder? = null

        val instance: AndroidAudioRecorder
            get() = _instance ?: throw IllegalStateException(
                "AndroidAudioRecorder not initialized. Call initialize(context) first."
            )

        fun initialize(context: Context) {
            if (_instance == null) {
                _instance = AndroidAudioRecorder(context.applicationContext)
            }
        }
    }
}
