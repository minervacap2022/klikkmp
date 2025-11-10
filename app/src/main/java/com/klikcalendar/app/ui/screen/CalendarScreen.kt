package com.klikcalendar.app.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.AssistChip
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.klikcalendar.shared.model.CalendarEvent
import com.klikcalendar.shared.model.EventStatus
import com.klikcalendar.shared.model.TimelineDay
import com.klikcalendar.shared.state.FilterState
import com.klikcalendar.shared.strings.AppStrings
import com.klikcalendar.app.ui.components.MonthlyCalendar
import com.klikcalendar.shared.model.viewmodel.MeetingViewModel
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    timeline: List<TimelineDay>,
    filters: FilterState,
    strings: AppStrings,
    onFilters: (FilterState) -> Unit,
    onOpenMeeting: (CalendarEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
    var selectedDate by remember { mutableStateOf<LocalDate?>(today) }

    // Create a pager for months - use a large range to simulate infinite scrolling
    val initialPage = 120 // Start at middle
    val pagerState = rememberPagerState(initialPage = initialPage, pageCount = { 240 })

    // Calculate the current month based on pager position
    val currentMonth by remember {
        derivedStateOf {
            val monthsOffset = pagerState.currentPage - initialPage
            today.plus(monthsOffset, DateTimeUnit.MONTH)
        }
    }

    // Bottom sheet state for to-do list
    val bottomSheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.PartiallyExpanded,
        skipHiddenState = false,
    )
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = bottomSheetState)

    val selectedDayEvents = timeline
        .find { it.date == selectedDate }
        ?.events
        ?.filter { filters.includeArchived || it.status != EventStatus.Archived }
        ?.filter { filters.statuses.contains(it.status) }
        ?.filter { filters.stages.contains(it.stage) }
        ?: emptyList()

    BottomSheetScaffold(
        modifier = modifier,
        scaffoldState = scaffoldState,
        sheetPeekHeight = 64.dp, // Only show the title when collapsed
        sheetContainerColor = MaterialTheme.colorScheme.surface, // unify colors with app surface
        sheetDragHandle = {}, // hide default grey drag handle
        sheetContent = {
            // To-do List Bottom Sheet Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    text = "待办事项",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                )

                // TODO: Connect to backend data source for tasks
                // Empty state - will be populated from backend
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    // Items will be populated from backend API
                }
            }
        }
    ) {
        // Main calendar content
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            // Horizontal pager for months
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f),
            ) { page ->
                val monthOffset = page - initialPage
                val monthDate = today.plus(monthOffset, DateTimeUnit.MONTH)

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    item {
                        MonthlyCalendar(
                            currentMonth = monthDate,
                            timeline = timeline,
                            selectedDate = selectedDate,
                            onDateSelected = { selectedDate = it },
                            modifier = Modifier.padding(vertical = 8.dp),
                        )
                    }

                    // Events for selected date
                    item {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            thickness = 1.dp,
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                    }

                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.CalendarMonth,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = selectedDate?.let { "${it.year}年${it.monthNumber}月${it.dayOfMonth}日" }
                                    ?: "未选择日期",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }

                    items(
                        selectedDayEvents,
                        key = { it.id },
                    ) { event ->
                        CalendarEventCard(
                            event = event,
                            strings = strings,
                            modifier = Modifier.padding(horizontal = 16.dp),
                            onOpenMeeting = onOpenMeeting,
                        )
                    }

                    if (selectedDayEvents.isEmpty()) {
                        item {
                            Text(
                                text = "该日无事件",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 24.dp),
                            )
                        }
                    }

                    // Add bottom padding to account for bottom sheet peek
                    item {
                        Spacer(modifier = Modifier.height(100.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun DayChip(day: TimelineDay, highlight: Boolean) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = if (highlight) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = if (highlight) 4.dp else 0.dp,
    ) {
        Column(
            modifier = Modifier
                .width(120.dp)
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            horizontalAlignment = Alignment.Start,
        ) {
                Text(
                    text = day.date.formatMonthDay(),
                    style = MaterialTheme.typography.labelMedium,
                    color = if (highlight) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                )
            Text(
                text = "${day.utilization}%",
                style = MaterialTheme.typography.bodyLarge,
                color = if (highlight) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = "${day.events.size} events",
                style = MaterialTheme.typography.bodySmall,
                color = if (highlight) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f) else MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
/**
 * 会议概要信息-card 样式 点击进入会议详情页面 meetingDetailScreen
 */
private fun CalendarEventCard(
    event: CalendarEvent,
    strings: AppStrings,
    onOpenMeeting: (CalendarEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: MeetingViewModel = viewModel()
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable {
                viewModel.refresh() //调用接口，返回会议数据
                onOpenMeeting(event) },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
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
                Text(text = event.title, style = MaterialTheme.typography.titleMedium)
                StatusChip(event.status, strings)
            }
            Text(
                text = "${event.start.formatClock()} - ${event.end.formatClock()}",
                style = MaterialTheme.typography.bodyMedium,
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    text = event.stage.name,
                    style = MaterialTheme.typography.labelMedium,
                )
                Text(
                    text = "@${event.owner}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                event.tags.forEach { tag ->
                    AssistChip(
                        onClick = { /* tag filter hook */ },
                        label = { Text(tag) },
                    )
                }
            }
        }
    }
}

@Composable
private fun StatusChip(status: EventStatus, strings: AppStrings) {
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
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = color,
        )
    }
}

private fun statusLabel(status: EventStatus, strings: AppStrings): String = when (status) {
    EventStatus.Pending -> strings.pending
    EventStatus.Confirmed -> strings.approved
    EventStatus.Declined -> strings.declined
    EventStatus.Archived -> strings.archived
}

private fun LocalDate.formatMonthDay(): String =
    "${monthNumber.pad2()}-${dayOfMonth.pad2()}"

private fun LocalDateTime.formatClock(): String =
    "${hour.pad2()}:${minute.pad2()}"

private fun Int.pad2(): String = toString().padStart(2, '0')
