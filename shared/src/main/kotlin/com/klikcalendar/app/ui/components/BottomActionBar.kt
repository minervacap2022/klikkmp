package com.klikcalendar.app.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Feedback
import androidx.compose.material.icons.outlined.JoinInner
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.klikcalendar.app.state.AppTab
import com.klikcalendar.app.strings.AppStrings

@Composable
fun BottomActionBar(
    currentTab: AppTab,
    strings: AppStrings,
    onSelectTab: (AppTab) -> Unit,
    showFeedback: Boolean,
    onFeedback: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        shadowElevation = 0.dp,
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            // 日程 tab (left)
            BottomNavItem(
                icon = Icons.Outlined.CalendarMonth,
                label = strings.schedule,
                selected = currentTab == AppTab.Schedule,
                onClick = { onSelectTab(AppTab.Schedule) },
            )

            // 反馈 button (center, conditional)
            // ⚠️ PLACEHOLDER_REMOVE: Circular scale animation for feedback button
            // This button and animation should be removed when feedback feature is complete
            // Search for "PLACEHOLDER_REMOVE" to find all placeholder feedback code
            AnimatedVisibility(
                visible = showFeedback,
                enter = scaleIn(
                    animationSpec = tween(durationMillis = 300),
                    initialScale = 0f
                ),
                exit = scaleOut(
                    animationSpec = tween(durationMillis = 300),
                    targetScale = 0f
                )
            ) {
                FloatingActionButton(
                    onClick = onFeedback,
                    shape = CircleShape,
                    modifier = Modifier.size(56.dp),
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 0.dp,
                        focusedElevation = 0.dp,
                        hoveredElevation = 0.dp,
                    ),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Feedback,
                        contentDescription = strings.feedback,
                        modifier = Modifier.size(24.dp),
                    )
                }
            }

            // WorkLife tab (right)
            BottomNavItem(
                icon = Icons.Outlined.JoinInner,
                label = strings.workLife,
                selected = currentTab == AppTab.WorkLife,
                onClick = { onSelectTab(AppTab.WorkLife) },
            )
        }
    }
}

@Composable
private fun BottomNavItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val contentColor = if (selected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
    }

    Column(
        modifier = modifier
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick,
            )
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(24.dp),
            tint = contentColor,
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = contentColor,
        )
    }
}
