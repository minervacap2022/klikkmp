package com.klikcalendar.app.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material.icons.outlined.TaskAlt
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.klikcalendar.shared.model.EventStatus
import com.klikcalendar.shared.model.OperationTask
import com.klikcalendar.shared.strings.AppStrings
import kotlinx.datetime.LocalDate

@Composable
fun OperationsScreen(
    tasks: List<OperationTask>,
    strings: AppStrings,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(text = strings.operations, style = MaterialTheme.typography.titleMedium)
            AssistChip(
                onClick = { /* trigger automation hub */ },
                label = { Text(strings.runAutomation) },
                leadingIcon = { Icon(Icons.Outlined.PlayCircle, contentDescription = null) },
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(tasks, key = { it.id }) { task ->
                OperationCard(task = task, strings = strings)
            }
        }
    }
}

@Composable
private fun OperationCard(task: OperationTask, strings: AppStrings) {
    val automationEnabled = remember(task.id) { mutableStateOf(task.automationEligible) }
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(text = task.title, style = MaterialTheme.typography.titleMedium)
                Surface(
                    shape = CircleShape,
                    color = colorFor(task.status).copy(alpha = 0.15f),
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        text = task.status.name,
                        style = MaterialTheme.typography.labelMedium,
                        color = colorFor(task.status),
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.Timer, contentDescription = null)
                    Text(
                        modifier = Modifier.padding(start = 6.dp),
                        text = task.dueDate.formatMonthDay(),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
                AssistChip(
                    onClick = { /* view playbook */ },
                    label = { Text(task.stage) },
                    leadingIcon = { Icon(Icons.Outlined.TaskAlt, contentDescription = null) },
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "@${task.owner}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = strings.runAutomation, style = MaterialTheme.typography.bodySmall)
                    Switch(checked = automationEnabled.value, onCheckedChange = { automationEnabled.value = it })
                }
            }
        }
    }
}

@Composable
private fun colorFor(status: EventStatus) = when (status) {
    EventStatus.Pending -> MaterialTheme.colorScheme.tertiary
    EventStatus.Confirmed -> MaterialTheme.colorScheme.primary
    EventStatus.Declined -> MaterialTheme.colorScheme.error
    EventStatus.Archived -> MaterialTheme.colorScheme.outline
}

private fun LocalDate.formatMonthDay(): String =
    "${monthNumber.pad2()}-${dayOfMonth.pad2()}"

private fun Int.pad2(): String = toString().padStart(2, '0')
