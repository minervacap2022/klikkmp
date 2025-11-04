//package com.klikcalendar.app
//
//import androidx.compose.animation.AnimatedContent
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Mic
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.FloatingActionButton
//import androidx.compose.material3.Icon
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.SheetState
//import androidx.compose.material3.rememberModalBottomSheetState
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import com.klikcalendar.app.state.AppTab
//import com.klikcalendar.app.state.KlikAppState
//import com.klikcalendar.app.state.RecordingViewModel
//import com.klikcalendar.app.state.rememberKlikAppState
//import com.klikcalendar.app.strings.strings
//import com.klikcalendar.app.ui.components.BottomActionBar
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.setValue
//import com.klikcalendar.app.ui.components.QuickActionsSheet
//import com.klikcalendar.app.ui.components.RecordingButton
//import com.klikcalendar.app.ui.components.RecordingStatusIndicator
//import com.klikcalendar.app.ui.components.StatusOverlay
//import com.klikcalendar.app.ui.screens.CalendarScreen
//import com.klikcalendar.app.ui.screens.FeedbackScreen
//import com.klikcalendar.app.ui.screens.MeetingDetailScreen
//import com.klikcalendar.app.ui.screens.ProfileScreen
//import com.klikcalendar.app.ui.screens.RecordingResultsScreen
//import com.klikcalendar.app.ui.screens.SwipeDirection
//import com.klikcalendar.app.ui.screens.WorkLifeScreen
//import com.klikcalendar.app.ui.theme.KlikTheme
//import kotlinx.coroutines.launch
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun KlikApp(
//    state: KlikAppState = rememberKlikAppState(),
//) {
//    val strings = strings(state.language)
//    val sheetState: SheetState = rememberModalBottomSheetState(
//        skipPartiallyExpanded = true,
//    )
//    val coroutine = rememberCoroutineScope()
//    var showProfile by remember { mutableStateOf(false) }
//    var showRecordingResults by remember { mutableStateOf(false) }
//
//    // Recording view model
//    val recordingViewModel = remember { RecordingViewModel(coroutine) }
//    val recordingState by recordingViewModel.recordingState.collectAsState()
//    val processingState by recordingViewModel.processingState.collectAsState()
//    val results by recordingViewModel.results.collectAsState()
//
//    // Show results screen when processing completes
//    if (results != null && processingState.status == com.klikcalendar.app.state.ProcessingStatus.Completed) {
//        showRecordingResults = true
//    }
//
//    KlikTheme {
//        Scaffold(
//            modifier = Modifier.fillMaxSize(),
//            bottomBar = {
//                BottomActionBar(
//                    currentTab = state.currentTab,
//                    strings = strings,
//                    onSelectTab = state::selectTab,
//                    showFeedback = state.showFeedbackButton,
//                    onFeedback = {
//                        state.openFeedbackScreen()
//                    },
//                )
//            },
//        ) { padding ->
//            Box(modifier = Modifier
//                .padding(padding)
//                .fillMaxSize(),
//            ) {
//                if (showProfile) {
//                    // Profile screen (full screen overlay)
//                    ProfileScreen(
//                        strings = strings,
//                        onBack = { showProfile = false },
//                        modifier = Modifier.fillMaxSize(),
//                    )
//                } else {
//                    Column(modifier = Modifier.fillMaxSize()) {
//                        StatusOverlay(
//                            state = state.overlayState,
//                            strings = strings,
//                            onProfile = { showProfile = true },
//                            onLanguageToggle = { state.toggleLanguage() },
//                        )
//                        Spacer(modifier = Modifier.height(12.dp))
//                        AnimatedContent(
//                            targetState = state.currentTab,
//                            label = "tab-transition",
//                        ) { tab ->
//                            when (tab) {
//                                AppTab.Schedule -> CalendarScreen(
//                                    timeline = state.timeline,
//                                    filters = state.filterState,
//                                    strings = strings,
//                                    onFilters = state::updateFilters,
//                                    onOpenMeeting = state::openMeetingDetail,
//                                )
//                                AppTab.WorkLife -> WorkLifeScreen(
//                                    nodes = state.knowledge,
//                                    strings = strings,
//                                )
//                            }
//                        }
//                    }
//                }
//
//                QuickActionsSheet(
//                    open = state.showQuickActions,
//                    sheetState = sheetState,
//                    actions = state.quickActions,
//                    strings = strings,
//                    onDismiss = {
//                        state.revealQuickActions(false)
//                        coroutine.launch { sheetState.hide() }
//                    },
//                )
//
//                state.activeMeeting?.let { meeting ->
//                    MeetingDetailScreen(
//                        event = meeting,
//                        transcripts = state.transcriptsFor(meeting.id),
//                        memos = state.memosFor(meeting.id),
//                        todos = state.tasksFor(meeting.id),
//                        miniApps = state.quickActionsFor(meeting.id),
//                        strings = strings,
//                        onClose = state::closeMeetingDetail,
//                    )
//                }
//
//                // Feedback Screen (full screen overlay)
//                if (state.showFeedbackScreen) {
//                    FeedbackScreen(
//                        items = state.feedbackItems,
//                        strings = strings,
//                        onFeedback = { item, direction ->
//                            // TODO: Send feedback to backend via repository
//                            // repository.submitFeedback(item.id, direction.toApprovalValue())
//                        },
//                        onComplete = {
//                            // Called when all items are processed
//                            state.closeFeedbackScreen()
//                        },
//                        onClose = {
//                            state.closeFeedbackScreen()
//                        },
//                        modifier = Modifier.fillMaxSize()
//                    )
//                }
//
//                // Recording Results Screen (full screen overlay)
//                if (showRecordingResults) {
//                    RecordingResultsScreen(
//                        results = results,
//                        strings = strings,
//                        onBack = {
//                            showRecordingResults = false
//                            recordingViewModel.clearResults()
//                        },
//                        modifier = Modifier.fillMaxSize()
//                    )
//                }
//            }
//        }
//    }
//}
