//package com.klikcalendar.shared.ui.components
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.aspectRatio
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.foundation.interaction.MutableInteractionSource
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import com.klikcalendar.shared.model.TimelineDay
//import kotlinx.datetime.LocalDate
//
//@Composable
//fun MonthlyCalendar(
//    currentMonth: LocalDate,
//    timeline: List<TimelineDay>,
//    selectedDate: LocalDate?,
//    onDateSelected: (LocalDate) -> Unit,
//    modifier: Modifier = Modifier,
//) {
//    Column(
//        modifier = modifier.fillMaxWidth(),
//        verticalArrangement = Arrangement.spacedBy(8.dp),
//    ) {
//        // Month/Year header
//        Text(
//            text = "${currentMonth.year}年 ${currentMonth.monthNumber}月",
//            style = MaterialTheme.typography.titleLarge,
//            fontWeight = FontWeight.Bold,
//            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
//        )
//
//        // Day of week headers
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 8.dp),
//            horizontalArrangement = Arrangement.SpaceEvenly,
//        ) {
//            listOf("日", "一", "二", "三", "四", "五", "六").forEach { day ->
//                Text(
//                    text = day,
//                    style = MaterialTheme.typography.labelMedium,
//                    color = MaterialTheme.colorScheme.onSurfaceVariant,
//                    textAlign = TextAlign.Center,
//                    modifier = Modifier.weight(1f),
//                )
//            }
//        }
//
//        // Calendar grid
//        val firstDayOfMonth = LocalDate(currentMonth.year, currentMonth.monthNumber, 1)
//        val daysInMonth = getDaysInMonth(currentMonth.year, currentMonth.monthNumber)
//        val startDayOfWeek = firstDayOfMonth.dayOfWeek.ordinal
//        val totalCells = ((daysInMonth + startDayOfWeek + 6).toInt() / 7) * 7
//
//        Column(
//            modifier = Modifier.padding(horizontal = 8.dp),
//            verticalArrangement = Arrangement.spacedBy(4.dp),
//        ) {
//            for (week in 0 until (totalCells / 7)) {
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceEvenly,
//                ) {
//                    for (dayOfWeek in 0..6) {
//                        val cellIndex = week * 7 + dayOfWeek
//                        val dayOfMonth = cellIndex - startDayOfWeek + 1
//
//                        if (dayOfMonth in 1..daysInMonth) {
//                            val date = LocalDate(currentMonth.year, currentMonth.monthNumber, dayOfMonth)
//                            val dayData = timeline.find { it.date == date }
//                            val isSelected = date == selectedDate
//                            val hasEvents = (dayData?.events?.size ?: 0) > 0
//
//                            CalendarDayCell(
//                                day = dayOfMonth,
//                                eventCount = dayData?.events?.size ?: 0,
//                                utilization = dayData?.utilization ?: 0,
//                                isSelected = isSelected,
//                                hasEvents = hasEvents,
//                                onClick = { onDateSelected(date) },
//                                modifier = Modifier.weight(1f),
//                            )
//                        } else {
//                            Box(modifier = Modifier.weight(1f))
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//private fun CalendarDayCell(
//    day: Int,
//    eventCount: Int,
//    utilization: Int,
//    isSelected: Boolean,
//    hasEvents: Boolean,
//    onClick: () -> Unit,
//    modifier: Modifier = Modifier,
//) {
//    Box(
//        modifier = modifier
//            .aspectRatio(1f)
//            .padding(2.dp)
//            .clip(CircleShape)
//            .background(
//                when {
//                    isSelected -> MaterialTheme.colorScheme.primary
//                    utilization > 80 -> MaterialTheme.colorScheme.errorContainer
//                    utilization > 50 -> MaterialTheme.colorScheme.tertiaryContainer
//                    hasEvents -> MaterialTheme.colorScheme.primaryContainer
//                    else -> Color.Transparent
//                }
//            )
//            .clickable(
//                indication = null,
//                interactionSource = remember { MutableInteractionSource() },
//                onClick = onClick,
//            ),
//        contentAlignment = Alignment.Center,
//    ) {
//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center,
//        ) {
//            Text(
//                text = day.toString(),
//                style = MaterialTheme.typography.bodyMedium,
//                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
//                color = when {
//                    isSelected -> MaterialTheme.colorScheme.onPrimary
//                    hasEvents -> MaterialTheme.colorScheme.onPrimaryContainer
//                    else -> MaterialTheme.colorScheme.onSurface
//                },
//            )
//            if (hasEvents && !isSelected) {
//                Box(
//                    modifier = Modifier
//                        .size(4.dp)
//                        .clip(CircleShape)
//                        .background(MaterialTheme.colorScheme.primary)
//                )
//            }
//        }
//    }
//}
//
//private fun getDaysInMonth(year: Int, month: Int): Int {
//    return when (month) {
//        1, 3, 5, 7, 8, 10, 12 -> 31
//        4, 6, 9, 11 -> 30
//        2 -> if (isLeapYear(year)) 29 else 28
//        else -> 30
//    }
//}
//
//private fun isLeapYear(year: Int): Boolean {
//    return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
//}
