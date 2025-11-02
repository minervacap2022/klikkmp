package com.klikcalendar.app.ui.screens

/**
 * ⚠️ PLACEHOLDER_REMOVE: Feedback Screen for UI Development
 *
 * This screen uses placeholder feedback data from SampleData.kt.
 * When integrating with real backend:
 * 1. Remove placeholder feedback items from SampleData.kt (search for "PLACEHOLDER_REMOVE")
 * 2. Connect to real feedback API endpoint via KlikRepository
 * 3. Update ViewModel to fetch real feedback items
 * 4. Remove placeholder feedback button animation from BottomActionBar.kt if needed
 *
 * Search for "PLACEHOLDER_REMOVE" throughout the codebase to find all related code.
 */

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Help
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.klikcalendar.app.strings.AppStrings
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs

enum class SwipeDirection {
    LEFT,    // Incorrect
    RIGHT,   // Correct
    UP,      // Uncertain
    NONE
}

data class FeedbackItem(
    val id: String,
    val title: String,
    val description: String,
    val content: String,
    val type: FeedbackType,
)

enum class FeedbackType {
    SPEAKER_IDENTIFICATION,  // "SPEAKER_00 ��������"
    TASK_EXTRACTION,         // ������ȡ��֤
    RELATIONSHIP_EXTRACTION, // ��ϵ��ȡ��֤
    MEETING_RESULT,         // ��������֤
}

@Composable
fun FeedbackScreen(
    items: List<FeedbackItem>,
    strings: AppStrings,
    onFeedback: (FeedbackItem, SwipeDirection) -> Unit,
    onComplete: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var currentIndex by remember { mutableStateOf(0) }
    val scope = rememberCoroutineScope()

    // When all items are processed, auto-close
    LaunchedEffect(currentIndex) {
        if (currentIndex >= items.size) {
            onComplete()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        // Close button
        IconButton(
            onClick = onClose,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        // Header
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = strings.feedback,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${currentIndex + 1} / ${items.size}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }

        // Instructions
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 100.dp)
                .fillMaxWidth()
                .padding(horizontal = 40.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    tint = Color(0xFFE57373),
                    modifier = Modifier.size(32.dp)
                )
                Text(
                    text = "\u2190\nWrong",
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Help,
                    contentDescription = null,
                    tint = Color(0xFFFFA726),
                    modifier = Modifier.size(32.dp)
                )
                Text(
                    text = "\u2191\nUnsure",
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color(0xFF81C784),
                    modifier = Modifier.size(32.dp)
                )
                Text(
                    text = "\u2192\nCorrect",
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }

        // Card stack
        if (items.isNotEmpty() && currentIndex < items.size) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 140.dp, bottom = 200.dp, start = 32.dp, end = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                // Show next card in background (for depth effect)
                if (currentIndex + 1 < items.size) {
                    FeedbackCard(
                        item = items[currentIndex + 1],
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                scaleX = 0.9f
                                scaleY = 0.9f
                                alpha = 0.5f
                            }
                    )
                }

                // Current card (swipeable) with key to force recreation
                key(items[currentIndex].id) {
                    SwipeableCard(
                        item = items[currentIndex],
                        onSwipe = { direction ->
                            onFeedback(items[currentIndex], direction)
                            // Delay index increment to avoid race condition
                            scope.launch {
                                delay(50) // Small delay to ensure state updates
                                currentIndex++
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
private fun SwipeableCard(
    item: FeedbackItem,
    onSwipe: (SwipeDirection) -> Unit,
    modifier: Modifier = Modifier,
) {
    val offsetX = remember { Animatable(0f) }
    val offsetY = remember { Animatable(0f) }
    val rotation = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    val swipeThreshold = 200f

    Box(
        modifier = modifier
            .graphicsLayer {
                translationX = offsetX.value
                translationY = offsetY.value
                rotationZ = rotation.value
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {
                        scope.launch {
                            val direction = when {
                                offsetX.value > swipeThreshold -> SwipeDirection.RIGHT
                                offsetX.value < -swipeThreshold -> SwipeDirection.LEFT
                                offsetY.value < -swipeThreshold -> SwipeDirection.UP
                                else -> SwipeDirection.NONE
                            }

                            if (direction != SwipeDirection.NONE) {
                                // Animate off screen and wait for completion
                                val animX = launch {
                                    offsetX.animateTo(
                                        when (direction) {
                                            SwipeDirection.RIGHT -> 1000f
                                            SwipeDirection.LEFT -> -1000f
                                            else -> offsetX.value
                                        },
                                        animationSpec = tween(200)
                                    )
                                }
                                val animY = launch {
                                    offsetY.animateTo(
                                        if (direction == SwipeDirection.UP) -1000f else offsetY.value,
                                        animationSpec = tween(200)
                                    )
                                }
                                // Wait for both animations to complete
                                animX.join()
                                animY.join()
                                // Now call the callback
                                onSwipe(direction)
                            } else {
                                // Snap back to center
                                launch { offsetX.animateTo(0f, animationSpec = tween(200)) }
                                launch { offsetY.animateTo(0f, animationSpec = tween(200)) }
                                launch { rotation.animateTo(0f, animationSpec = tween(200)) }
                            }
                        }
                    }
                ) { change, dragAmount ->
                    change.consume()
                    scope.launch {
                        offsetX.snapTo(offsetX.value + dragAmount.x)
                        offsetY.snapTo(offsetY.value + dragAmount.y)
                        rotation.snapTo(offsetX.value / 30f)
                    }
                }
            }
    ) {
        FeedbackCard(item = item, modifier = Modifier.fillMaxSize())

        // Overlay indicators
        if (abs(offsetX.value) > 50 || abs(offsetY.value) > 50) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        when {
                            offsetX.value > 50 -> Color(0x4081C784) // Green
                            offsetX.value < -50 -> Color(0x40E57373) // Red
                            offsetY.value < -50 -> Color(0x40FFA726) // Orange
                            else -> Color.Transparent
                        }
                    ),
                contentAlignment = when {
                    offsetX.value > 50 -> Alignment.Center
                    offsetX.value < -50 -> Alignment.Center
                    offsetY.value < -50 -> Alignment.Center
                    else -> Alignment.Center
                }
            ) {
                Icon(
                    imageVector = when {
                        offsetX.value > 50 -> Icons.Default.Check
                        offsetX.value < -50 -> Icons.Default.Close
                        offsetY.value < -50 -> Icons.Default.Help
                        else -> Icons.Default.Check
                    },
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(80.dp)
                        .rotate(
                            when {
                                offsetX.value > 50 -> -rotation.value
                                offsetX.value < -50 -> -rotation.value
                                else -> 0f
                            }
                        )
                )
            }
        }
    }
}

@Composable
private fun FeedbackCard(
    item: FeedbackItem,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Type badge
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Text(
                    text = when (item.type) {
                        FeedbackType.SPEAKER_IDENTIFICATION -> "Speaker ID"
                        FeedbackType.TASK_EXTRACTION -> "Task Extract"
                        FeedbackType.RELATIONSHIP_EXTRACTION -> "Relation"
                        FeedbackType.MEETING_RESULT -> "Result"
                    },
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Title
            Text(
                text = item.title,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Description
            Text(
                text = item.description,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Content (the actual feedback item)
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = item.content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(20.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

