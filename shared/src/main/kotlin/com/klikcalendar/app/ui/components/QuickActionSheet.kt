package com.klikcalendar.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Aod
import androidx.compose.material.icons.outlined.DevicesOther
import androidx.compose.material.icons.outlined.NoteAlt
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material.icons.outlined.TaskAlt
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.klikcalendar.app.model.QuickAction
import com.klikcalendar.app.model.QuickActionType
import com.klikcalendar.app.strings.AppStrings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickActionsSheet(
    open: Boolean,
    sheetState: SheetState,
    actions: List<QuickAction>,
    strings: AppStrings,
    onDismiss: () -> Unit,
) {
    if (!open) return

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(text = strings.quickActions, style = androidx.compose.material3.MaterialTheme.typography.titleMedium)
            HorizontalDivider()
            actions.forEachIndexed { index, action ->
                QuickActionRow(action)
                if (index != actions.lastIndex) {
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
private fun QuickActionRow(action: QuickAction) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Icon(
            imageVector = iconFor(action.type),
            contentDescription = action.label,
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(text = action.label, style = androidx.compose.material3.MaterialTheme.typography.titleSmall)
            Text(text = action.description, style = androidx.compose.material3.MaterialTheme.typography.bodySmall)
        }
    }
}

private fun iconFor(type: QuickActionType): ImageVector = when (type) {
    QuickActionType.Meeting -> Icons.Outlined.Aod
    QuickActionType.Device -> Icons.Outlined.DevicesOther
    QuickActionType.Notes -> Icons.Outlined.NoteAlt
    QuickActionType.Automation -> Icons.Outlined.PlayCircle
    QuickActionType.Task -> Icons.Outlined.TaskAlt
}
