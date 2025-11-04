package com.klikcalendar.shared.state

// ⚠️ PLACEHOLDER_REMOVE: Importing SampleData for development
// Remove this import when integrating with real backend via KlikRepository

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.klikcalendar.shared.model.CalendarEvent
import com.klikcalendar.shared.model.EventStage
import com.klikcalendar.shared.model.EventStatus
import com.klikcalendar.shared.model.MeetingMemo
import com.klikcalendar.shared.model.OperationTask
import com.klikcalendar.shared.model.QuickAction
import com.klikcalendar.shared.model.StatusOverlayState
import com.klikcalendar.shared.model.TimelineDay
import com.klikcalendar.shared.model.TranscriptRecord
import com.klikcalendar.shared.model.WorkLifeNode
import com.klikcalendar.shared.strings.Language
import com.klikcalendar.shared.data.FeedbackItem
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

enum class AppTab {
    Schedule,
    WorkLife,
}

data class FilterState(
    val statuses: Set<EventStatus>,
    val stages: Set<EventStage>,
    val includeArchived: Boolean,
) {
    companion object {
        fun default(): FilterState = FilterState(
            statuses = setOf(EventStatus.Pending, EventStatus.Confirmed),
            stages = EventStage.values().toSet(),
            includeArchived = false,
        )
    }
}

@Stable
class KlikAppState(
    language: Language,
    tab: AppTab,
    overlay: StatusOverlayState,
    timeline: List<TimelineDay>,
    records: List<TranscriptRecord>,
    memos: List<MeetingMemo>,
    knowledgeGraph: List<WorkLifeNode>,
    operations: List<OperationTask>,
    quickActions: List<QuickAction>,
    feedbackItems: List<FeedbackItem>,
) {
    var language by mutableStateOf(language)
        private set

    var currentTab by mutableStateOf(tab)
        private set

    var overlayState by mutableStateOf(overlay)
        private set

    var timeline by mutableStateOf(timeline)
        private set

    var transcripts by mutableStateOf(records)
        private set

    var memos by mutableStateOf(memos)
        private set

    var knowledge by mutableStateOf(knowledgeGraph)
        private set

    var opsTasks by mutableStateOf(operations)
        private set

    var quickActions by mutableStateOf(quickActions)
        private set

    var showQuickActions by mutableStateOf(false)
        private set

    var filterState by mutableStateOf(FilterState.default())
        private set

    var showFeedbackButton by mutableStateOf(false)
        private set

    var activeMeeting by mutableStateOf<CalendarEvent?>(null)
        private set

    var feedbackItems by mutableStateOf(feedbackItems)
        private set

    var showFeedbackScreen by mutableStateOf(false)
        private set

    fun toggleLanguage() {
        language = when (language) {
            Language.Chinese -> Language.English
            Language.English -> Language.Chinese
        }
    }

    fun selectTab(tab: AppTab) {
        currentTab = tab
    }

    fun updateOverlay(newOverlay: StatusOverlayState) {
        overlayState = newOverlay
    }

    fun revealQuickActions(show: Boolean) {
        showQuickActions = show
    }

    fun updateFilters(filters: FilterState) {
        filterState = filters
    }

    fun showFeedbackButton(show: Boolean) {
        showFeedbackButton = show
    }

    fun openFeedbackScreen() {
        showFeedbackScreen = true
    }

    fun closeFeedbackScreen() {
        showFeedbackScreen = false
        // Hide button if no more feedback items
        if (feedbackItems.isEmpty()) {
            showFeedbackButton = false
        }
    }

    fun processFeedback(itemId: String) {
        feedbackItems = feedbackItems.filter { it.id != itemId }
        // Auto-hide button when no items left
        if (feedbackItems.isEmpty()) {
            showFeedbackButton = false
        }
    }

    fun openMeetingDetail(event: CalendarEvent) {
        // Persist the tapped meeting so downstream screens can render its related artifacts.
        activeMeeting = event
    }

    fun closeMeetingDetail() {
        activeMeeting = null
    }

    fun transcriptsFor(eventId: String): List<TranscriptRecord> =
        transcripts.filter { eventId in it.relatedEventIds }

    fun memosFor(eventId: String): List<MeetingMemo> =
        memos.filter { it.meetingId == eventId }

    fun tasksFor(eventId: String): List<OperationTask> =
        opsTasks.filter { eventId in it.relatedEventIds }

    fun quickActionsFor(eventId: String): List<QuickAction> =
        quickActions.filter { eventId in it.relatedEventIds }
}

@Composable
fun rememberKlikAppState(
    initialLanguage: Language = Language.Chinese,
    initialTab: AppTab = AppTab.Schedule,
): KlikAppState {
    // ⚠️ PLACEHOLDER_REMOVE: Using SampleData for development instead of backend
    // TODO: Integrate with KlikRepository to load data from backend
    // When integrating with real backend:
    // 1. Remove SampleData import at top of file
    // 2. Replace SampleData.* with repository calls
    // Example implementation:
    //   val repository = remember { KlikRepositoryImpl() }
    //   val scope = rememberCoroutineScope()
    //   LaunchedEffect(Unit) {
    //       scope.launch {
    //           val data = repository.getTimeline()
    //           state.timeline = data
    //       }
    //   }

    return remember {
        KlikAppState(
            language = initialLanguage,
            tab = initialTab,
            overlay = SampleData.overlay,          // ⚠️ PLACEHOLDER_REMOVE
            timeline = SampleData.timeline,        // ⚠️ PLACEHOLDER_REMOVE
            records = SampleData.transcripts,      // ⚠️ PLACEHOLDER_REMOVE
            memos = SampleData.memos,              // ⚠️ PLACEHOLDER_REMOVE
            knowledgeGraph = SampleData.knowledgeGraph, // ⚠️ PLACEHOLDER_REMOVE
            operations = SampleData.operations,    // ⚠️ PLACEHOLDER_REMOVE
            quickActions = SampleData.quickActions, // ⚠️ PLACEHOLDER_REMOVE
            feedbackItems = SampleData.feedbackItems, // ⚠️ PLACEHOLDER_REMOVE
        )
    }
}

private fun createEmptyOverlayState(): StatusOverlayState {
    return StatusOverlayState(
        indicators = emptyList(),
        miniAppCount = 0,
        syncTimestamp = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        alerts = emptyList(),
    )
}
