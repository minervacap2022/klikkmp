package com.klikcalendar.app.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import com.klikcalendar.shared.state.ProcessingState
import com.klikcalendar.shared.state.ProcessingStatus
import com.klikcalendar.shared.audio.RecordingState

/**
 * Recording button with animated states for recording and processing.
 */
@Composable
fun RecordingButton(
    recordingState: RecordingState,
    processingState: ProcessingState,
    onStartRecording: () -> Unit,
    onStopRecording: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isRecording = recordingState.isRecording
    val isProcessing = processingState.status in listOf(
        ProcessingStatus.Uploading,
        ProcessingStatus.Processing
    )

    Box(
        modifier = modifier.size(72.dp),
        contentAlignment = Alignment.Center,
    ) {
        // Animated pulse effect when recording
        if (isRecording) {
            val infiniteTransition = rememberInfiniteTransition()
            val scale by infiniteTransition.animateFloat(
                initialValue = 1f,
                targetValue = 1.3f,
                animationSpec = infiniteRepeatable(
                    animation = tween(800, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                )
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .scale(scale)
                    .background(
                        color = MaterialTheme.colorScheme.error.copy(alpha = 0.3f),
                        shape = CircleShape
                    )
            )
        }

        // Main button
        FloatingActionButton(
            onClick = {
                if (isRecording) {
                    onStopRecording()
                } else if (!isProcessing) {
                    onStartRecording()
                }
            },
            modifier = Modifier.size(64.dp),
            shape = CircleShape,
            containerColor = when {
                isRecording -> MaterialTheme.colorScheme.error
                isProcessing -> MaterialTheme.colorScheme.secondary
                else -> MaterialTheme.colorScheme.primary
            },
        ) {
            when {
                isProcessing -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(32.dp),
                        color = MaterialTheme.colorScheme.onSecondary,
                        strokeWidth = 3.dp
                    )
                }
                isRecording -> {
                    Icon(
                        imageVector = Icons.Filled.Stop,
                        contentDescription = "Stop Recording",
                        modifier = Modifier.size(32.dp),
                    )
                }
                else -> {
                    Icon(
                        imageVector = Icons.Filled.Mic,
                        contentDescription = "Start Recording",
                        modifier = Modifier.size(32.dp),
                    )
                }
            }
        }
    }
}

/**
 * Recording status indicator for showing recording duration and processing state.
 */
@Composable
fun RecordingStatusIndicator(
    recordingState: RecordingState,
    processingState: ProcessingState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        when {
            recordingState.isRecording -> {
                Text(
                    text = "● Recording...",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.error
                )
                Text(
                    text = formatDuration(recordingState.duration),
                    style = MaterialTheme.typography.bodySmall,
                )
            }
            processingState.status != ProcessingStatus.Idle -> {
                Text(
                    text = processingState.message,
                    style = MaterialTheme.typography.labelMedium,
                )
                if (processingState.progress > 0) {
                    LinearProgressIndicator(
                        progress = processingState.progress / 100f,
                        modifier = Modifier
                            .width(200.dp)
                            .padding(top = 4.dp),
                    )
                }
            }
            recordingState.error != null -> {
                Text(
                    text = recordingState.error.toString(),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }
    }
}

/**
 * Format duration in milliseconds to MM:SS format.
 */
private fun formatDuration(durationMs: Long): String {
    val totalSeconds = durationMs / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
}
