package com.klikcalendar.shared.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

enum class StatusLevel {
    Normal,
    Warning,
    Critical,
}

enum class DeviceIndicatorType {
    Battery,
    Sensors,
    Ble,
    Recording,
    Sync,
    Network,
}

data class DeviceIndicator(
    val type: DeviceIndicatorType,
    val value: String,
    val level: StatusLevel,
    val actionable: Boolean = false,
)

data class StatusOverlayState(
    val indicators: List<DeviceIndicator>,
    val miniAppCount: Int,
    val syncTimestamp: LocalDateTime,
    val alerts: List<String>,
)

enum class EventStage {
    Discovery,
    Engagement,
    Negotiation,
    Closing,
}

enum class EventStatus {
    Pending,
    Confirmed,
    Declined,
    Archived,
}

@Serializable
data class CalendarEvent(
    val id: String,
    val title: String,
    val owner: String,
    val start: LocalDateTime,
    val end: LocalDateTime,
    val stage: EventStage,
    val status: EventStatus,
    val priority: Int,
    val location: String,
    val tags: List<String>,
)

data class TimelineDay(
    val date: LocalDate,
    val focus: Boolean,
    val utilization: Int,
    val events: List<CalendarEvent>,
)

enum class TranscriptChannel {
    Audio,
    Call,
    Meeting,
    Automation,
}

data class TranscriptRecord(
    val id: String,
    val title: String,
    val owner: String,
    val capturedAt: LocalDateTime,
    val channel: TranscriptChannel,
    val summary: String,
    val status: EventStatus,
    val attachments: List<String>,
    val requiresAction: Boolean,
    val relatedEventIds: Set<String> = emptySet(),
)

data class MeetingMemo(
    val id: String,
    val meetingId: String,
    val title: String,
    val body: String,
    val lastUpdated: LocalDateTime,
    val author: String,
)

enum class WorkLifeNodeType {
    Person,
    Team,
    Project,
    Asset,
}

data class MetricSnapshot(
    val label: String,
    val value: String,
    val delta: String,
    val level: StatusLevel,
)

data class WorkLifeNode(
    val id: String,
    val type: WorkLifeNodeType,
    val name: String,
    val description: String,
    val peers: Int,
    val tags: List<String>,
    val metrics: List<MetricSnapshot>,
    val linkedNodes: List<String>,
)

data class OperationTask(
    val id: String,
    val title: String,
    val dueDate: LocalDate,
    val owner: String,
    val stage: String,
    val automationEligible: Boolean,
    val status: EventStatus,
    val category: String,
    val relatedEventIds: Set<String> = emptySet(),
)

enum class QuickActionType {
    Meeting,
    Device,
    Notes,
    Automation,
    Task,
}

data class QuickAction(
    val id: String,
    val type: QuickActionType,
    val label: String,
    val description: String,
    val relatedEventIds: Set<String> = emptySet(),
)
