package com.klikcalendar.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.PictureAsPdf
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.klikcalendar.app.model.EventStatus
import com.klikcalendar.app.model.TranscriptRecord
import com.klikcalendar.app.strings.AppStrings
import kotlinx.datetime.LocalDateTime

@Composable
fun TranscriptsScreen(
    records: List<TranscriptRecord>,
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
            Text(text = strings.pipeline, style = MaterialTheme.typography.titleMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AssistChip(
                    onClick = { /* export csv */ },
                    label = { Text("CSV") },
                    leadingIcon = {
                        Icon(Icons.Outlined.FilterList, contentDescription = null)
                    },
                )
                AssistChip(
                    onClick = { /* export pdf */ },
                    label = { Text("PDF") },
                    leadingIcon = {
                        Icon(Icons.Outlined.PictureAsPdf, contentDescription = null)
                    },
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(records, key = { it.id }) { record ->
                TranscriptCard(record = record, strings = strings)
            }
        }
    }
}

@Composable
private fun TranscriptCard(record: TranscriptRecord, strings: AppStrings) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
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
                Text(
                    text = record.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                AssistChip(
                    onClick = { /* playback hook */ },
                    label = { Text(record.channel.name) },
                    leadingIcon = { Icon(Icons.Outlined.PlayCircle, contentDescription = null) },
                )
            }
            Text(
                text = record.summary,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = record.capturedAt.formatTimestamp(),
                    style = MaterialTheme.typography.labelMedium,
                )
                Text(
                    text = "@${record.owner}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
            if (record.attachments.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    record.attachments.forEach { file ->
                        Text(
                            text = "• $file",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline,
                        )
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                StatusActionButton(
                    label = strings.confirm,
                    enabled = true,
                    modifier = Modifier.fillMaxWidth(0.5f),
                )
                StatusActionButton(
                    label = strings.cancel,
                    enabled = record.status == EventStatus.Pending,
                    modifier = Modifier.fillMaxWidth(0.5f),
                )
            }
        }
    }
}

@Composable
private fun StatusActionButton(label: String, enabled: Boolean, modifier: Modifier = Modifier) {
    val container = if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
    val content = if (enabled) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant

    Surface(
        modifier = modifier
            .height(44.dp)
            .let { base -> if (enabled) base else base.alpha(0.6f) },
        shape = RoundedCornerShape(12.dp),
        color = container,
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = content,
            )
        }
    }
}

private fun LocalDateTime.formatTimestamp(): String =
    "${monthNumber.pad2()}-${dayOfMonth.pad2()} ${hour.pad2()}:${minute.pad2()}"

private fun Int.pad2(): String = toString().padStart(2, '0')
