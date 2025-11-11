package com.klikcalendar.app.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.EventNote
import androidx.compose.material.icons.outlined.Launch
import androidx.compose.material.icons.outlined.TaskAlt
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.klikcalendar.shared.model.CalendarEvent
import com.klikcalendar.shared.model.EventStage
import com.klikcalendar.shared.model.EventStatus
import com.klikcalendar.shared.model.MeetingMemo
import com.klikcalendar.shared.model.OperationTask
import com.klikcalendar.shared.model.QuickAction
import com.klikcalendar.shared.model.TranscriptRecord
import com.klikcalendar.shared.state.KlikAppState
import com.klikcalendar.shared.state.rememberKlikAppState
import com.klikcalendar.shared.strings.AppStrings
import com.klikcalendar.shared.strings.strings
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.minus
import kotlinx.datetime.plus

private enum class MeetingDetailTab {
    Transcripts,
    Memos,
    Todos,
    MiniApps,
}
@Composable // Overlay sheet showing meeting-specific drill-down tabs.
fun MeetingDetailScreen(
    event: CalendarEvent,
    transcripts: List<TranscriptRecord>,
    memos: List<MeetingMemo>,
    todos: List<OperationTask>,
    miniApps: List<QuickAction>,
    strings: AppStrings,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedTab by remember { mutableStateOf(MeetingDetailTab.Transcripts) }
    val tabs = remember(strings) {
        listOf(
            MeetingDetailTab.Transcripts to strings.records,
            MeetingDetailTab.Memos to strings.captureNotes,
            MeetingDetailTab.Todos to strings.operations,
            MeetingDetailTab.MiniApps to strings.miniApps,
        )
    }
    val selectedIndex = tabs.indexOfFirst { it.first == selectedTab }.coerceAtLeast(0)

    Box(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.45f))
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                ) { onClose() },
        )

        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .fillMaxHeight(0.92f),
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            tonalElevation = 10.dp,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                HeaderSection(event = event, strings = strings, onClose = onClose)

                TabRow(selectedTabIndex = selectedIndex) {
                    tabs.forEach { (tab, label) ->
                        Tab(
                            selected = selectedTab == tab,
                            onClick = { selectedTab = tab },
                            text = { Text(label) },
                        )
                    }
                }

                when (selectedTab) {
                    MeetingDetailTab.Transcripts -> TranscriptList(
                        items = transcripts,
                        strings = strings,
                    )

                    MeetingDetailTab.Memos -> MemoList(
                        memos = memos,
                        strings = strings,
                    )

                    MeetingDetailTab.Todos -> TodoList(
                        tasks = todos,
                        strings = strings,
                    )

                    MeetingDetailTab.MiniApps -> MiniAppList(
                        apps = miniApps,
                        strings = strings,
                    )
                }
            }
        }
    }
}

@Composable
private fun HeaderSection(event: CalendarEvent, strings: AppStrings, onClose: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = strings.meetingDetails,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                )
            }
            IconButton(onClick = onClose) {
                Icon(imageVector = Icons.Outlined.Close, contentDescription = strings.cancel)
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Outlined.CalendarMonth,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
            Column {
                Text(
                    text = "${event.start.formatDate()} ${event.start.formatClock()} - ${event.end.formatClock()}",
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    text = event.location,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            StatusPill(event.status, strings)
            AssistChip(
                onClick = { /* stage drilldown */ },
                label = { Text(event.stage.name) },
                leadingIcon = { Icon(Icons.Outlined.CheckCircle, contentDescription = null) },
            )
            Text(
                text = "@${event.owner}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.secondary,
            )
        }

        if (event.tags.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                event.tags.forEach { tag ->
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.secondaryContainer,
                    ) {
                        Text(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            text = tag,
                            style = MaterialTheme.typography.labelSmall,
                        )
                    }
                }
            }
        }
        Divider()
    }
}

@Composable
private fun TranscriptList(items: List<TranscriptRecord>, strings: AppStrings) {
    if (items.isEmpty()) {
        EmptyState(message = strings.noLinkedContent)
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(items, key = { it.id }) { record ->
            TranscriptDetailCard(record = record, strings = strings)
        }
    }
}

/**
 * 转录
 * 该页面主要包括6个相关字段
 * 1、转录信息title
 * 2、一个标签
 * 3、转录总结
 * 4、时间信息
 * 5、所属人员信息
 * 6、对应的文件名称
 */
@Composable
private fun TranscriptDetailCard(record: TranscriptRecord, strings: AppStrings) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
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
                )
            }
            Text(
                text = record.summary,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = "${record.capturedAt.formatDate()} ${record.capturedAt.formatClock()} • @${record.owner}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
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
        }
    }
}

@Composable
private fun MemoList(memos: List<MeetingMemo>, strings: AppStrings) {
    if (memos.isEmpty()) {
        EmptyState(message = strings.noLinkedContent)
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        memos.forEach { memo ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Outlined.EventNote, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = memo.title,
                                style = MaterialTheme.typography.titleMedium,
                            )
                        }
                        Text(
                            text = memo.lastUpdated.formatClock(),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    Text(
                        text = memo.body,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Text(
                        text = "@${memo.author}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                }
            }
        }
    }
}

@Composable
private fun TodoList(tasks: List<OperationTask>, strings: AppStrings) {
    if (tasks.isEmpty()) {
        EmptyState(message = strings.noLinkedContent)
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(tasks, key = { it.id }) { task ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.4f)),
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = task.title,
                            style = MaterialTheme.typography.titleMedium,
                        )
                        AssistChip(
                            onClick = { /* mark complete */ },
                            label = { Text(task.stage) },
                            leadingIcon = { Icon(Icons.Outlined.TaskAlt, contentDescription = null) },
                        )
                    }
                    Text(
                        text = "@${task.owner}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Text(
                            text = task.dueDate.formatDate(),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                        StatusPill(task.status, strings)
                    }
                    Text(
                        text = task.category,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

@Composable
private fun MiniAppList(apps: List<QuickAction>, strings: AppStrings) {
    if (apps.isEmpty()) {
        EmptyState(message = strings.noLinkedContent)
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        apps.forEach { action ->
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
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Outlined.Launch, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = action.label,
                                style = MaterialTheme.typography.titleMedium,
                            )
                        }
                        AssistChip(
                            onClick = { /* open mini app hook */ },
                            label = { Text(strings.openMiniApp) },
                        )
                    }
                    Text(
                        text = action.description,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Text(
                        text = action.type.name,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyState(message: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun StatusPill(status: EventStatus, strings: AppStrings) {
    val (label, color) = when (status) {
        EventStatus.Pending -> strings.pending to MaterialTheme.colorScheme.tertiary
        EventStatus.Confirmed -> strings.approved to MaterialTheme.colorScheme.primary
        EventStatus.Declined -> strings.declined to MaterialTheme.colorScheme.error
        EventStatus.Archived -> strings.archived to MaterialTheme.colorScheme.outline
    }
    Surface(
        shape = CircleShape,
        color = color.copy(alpha = 0.15f),
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = color,
        )
    }
}

private fun LocalDate.formatDate(): String =
    "${year}年${monthNumber.pad2()}月${dayOfMonth.pad2()}日"

private fun LocalDateTime.formatClock(): String =
    "${hour.pad2()}:${minute.pad2()}"

private fun LocalDateTime.formatDate(): String =
    "${monthNumber.pad2()}-${dayOfMonth.pad2()}"

private fun Int.pad2(): String = toString().padStart(2, '0')
//给默认参数进行页码预览
private val nowDate = LocalDate(2025, 10, 28)
private val nextDay = nowDate.plus(DatePeriod(days = 1))
private val previousDay = nowDate.minus(DatePeriod(days = 1))
@Preview
@Composable
fun PreviewMeetingDetailScreen(state: KlikAppState = rememberKlikAppState(),){
    val strings = strings(state.language)
    MeetingDetailScreen(
        event = previewMeetingDetailEvent(),
        transcripts = state.transcriptsFor(previewMeetingDetailEvent().id),
        memos = state.memosFor(previewMeetingDetailEvent().id),
        todos = state.tasksFor(previewMeetingDetailEvent().id),
        miniApps = state.quickActionsFor(previewMeetingDetailEvent().id),
        strings = strings,
        onClose = state::closeMeetingDetail,
    )
}

@Stable
fun previewMeetingDetailEvent() = CalendarEvent(
    id = "evt-1",
    title = "Pipeline #9 Stand-up",
    owner = "Iris",
    start = LocalDateTime(nowDate.year, nowDate.monthNumber, nowDate.dayOfMonth, 9, 30),
    end = LocalDateTime(nowDate.year, nowDate.monthNumber, nowDate.dayOfMonth, 10, 0),
    stage = EventStage.Discovery,
    status = EventStatus.Confirmed,
    priority = 1,
    location = "Atlas Room",
    tags = listOf("Pipeline #9", "Sprint"),
)
