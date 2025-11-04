//package com.klikcalendar.shared.ui.screens
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import com.klikcalendar.shared.data.backend.FrontendData
//import com.klikcalendar.shared.strings.AppStrings
//
///**
// * Screen to display results from audio processing pipeline.
// */
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun RecordingResultsScreen(
//    results: FrontendData?,
//    strings: AppStrings,
//    onBack: () -> Unit,
//    modifier: Modifier = Modifier,
//) {
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Recording Results") },
//                navigationIcon = {
//                    IconButton(onClick = onBack) {
//                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
//                    }
//                }
//            )
//        }
//    ) { paddingValues ->
//        if (results == null) {
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(paddingValues),
//                contentAlignment = androidx.compose.ui.Alignment.Center,
//            ) {
//                Text("No results available")
//            }
//        } else {
//            var selectedTab by remember { mutableStateOf(ResultsTab.Transcript) }
//
//            Column(
//                modifier = modifier
//                    .fillMaxSize()
//                    .padding(paddingValues)
//            ) {
//                // Tab selector
//                ScrollableTabRow(
//                    selectedTabIndex = selectedTab.ordinal,
//                    modifier = Modifier.fillMaxWidth(),
//                ) {
//                    ResultsTab.values().forEach { tab ->
//                        Tab(
//                            selected = selectedTab == tab,
//                            onClick = { selectedTab = tab },
//                            text = { Text(tab.title) }
//                        )
//                    }
//                }
//
//                // Content based on selected tab
//                when (selectedTab) {
//                    ResultsTab.Transcript -> TranscriptTab(results)
//                    ResultsTab.Todos -> TodosTab(results)
//                    ResultsTab.Minutes -> MinutesTab(results)
//                    ResultsTab.Participants -> ParticipantsTab(results)
//                    ResultsTab.KnowledgeGraph -> KnowledgeGraphTab(results)
//                }
//            }
//        }
//    }
//}
//
//enum class ResultsTab(val title: String) {
//    Transcript("Transcript"),
//    Todos("To-Dos"),
//    Minutes("Minutes"),
//    Participants("Participants"),
//    KnowledgeGraph("Knowledge Graph"),
//}
//
//@Composable
//private fun TranscriptTab(results: FrontendData) {
//    LazyColumn(
//        modifier = Modifier.fillMaxSize(),
//        contentPadding = PaddingValues(16.dp),
//        verticalArrangement = Arrangement.spacedBy(12.dp),
//    ) {
//        // Summary
//        item {
//            Card(
//                modifier = Modifier.fillMaxWidth(),
//            ) {
//                Column(modifier = Modifier.padding(16.dp)) {
//                    Text(
//                        text = "Summary",
//                        style = MaterialTheme.typography.titleMedium,
//                    )
//                    Spacer(modifier = Modifier.height(8.dp))
//                    Text("Speakers: ${results.transcript.speakersDetected}")
//                    Text("Segments: ${results.transcript.totalSegments}")
//                    Text("Duration: ${formatSeconds(results.transcript.totalDuration.toInt())}")
//                    Text("Language: ${results.transcript.language}")
//                }
//            }
//        }
//
//        // Transcript segments
//        items(results.transcript.segments) { segment ->
//            Card(
//                modifier = Modifier.fillMaxWidth(),
//            ) {
//                Column(modifier = Modifier.padding(12.dp)) {
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.SpaceBetween,
//                    ) {
//                        Text(
//                            text = segment.speakerName ?: segment.speaker,
//                            style = MaterialTheme.typography.titleSmall,
//                            color = MaterialTheme.colorScheme.primary,
//                        )
//                        Text(
//                            text = formatTimestamp(segment.start),
//                            style = MaterialTheme.typography.bodySmall,
//                            color = MaterialTheme.colorScheme.onSurfaceVariant,
//                        )
//                    }
//                    Spacer(modifier = Modifier.height(4.dp))
//                    Text(
//                        text = segment.text,
//                        style = MaterialTheme.typography.bodyMedium,
//                    )
//                }
//            }
//        }
//    }
//}
//
//@Composable
//private fun TodosTab(results: FrontendData) {
//    LazyColumn(
//        modifier = Modifier.fillMaxSize(),
//        contentPadding = PaddingValues(16.dp),
//        verticalArrangement = Arrangement.spacedBy(12.dp),
//    ) {
//        item {
//            Text(
//                text = "Total Todos: ${results.todos.totalCount}",
//                style = MaterialTheme.typography.titleMedium,
//            )
//        }
//
//        items(results.todos.items) { todo ->
//            Card(
//                modifier = Modifier.fillMaxWidth(),
//            ) {
//                Column(modifier = Modifier.padding(12.dp)) {
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.SpaceBetween,
//                    ) {
//                        Text(
//                            text = todo.text,
//                            style = MaterialTheme.typography.bodyMedium,
//                            modifier = Modifier.weight(1f),
//                        )
//                        if (todo.priority != null) {
//                            AssistChip(
//                                onClick = {},
//                                label = { Text(todo.priority.uppercase()) },
//                                modifier = Modifier.padding(start = 8.dp),
//                            )
//                        }
//                    }
//                    Spacer(modifier = Modifier.height(8.dp))
//                    if (todo.assignee != null) {
//                        Text(
//                            text = "Assignee: ${todo.assignee}",
//                            style = MaterialTheme.typography.bodySmall,
//                            color = MaterialTheme.colorScheme.onSurfaceVariant,
//                        )
//                    }
//                    if (todo.dueDate != null) {
//                        Text(
//                            text = "Due: ${todo.dueDate}",
//                            style = MaterialTheme.typography.bodySmall,
//                            color = MaterialTheme.colorScheme.onSurfaceVariant,
//                        )
//                    }
//                    Text(
//                        text = "Status: ${todo.status} • Category: ${todo.category ?: "N/A"}",
//                        style = MaterialTheme.typography.bodySmall,
//                        color = MaterialTheme.colorScheme.onSurfaceVariant,
//                    )
//                }
//            }
//        }
//    }
//}
//
//@Composable
//private fun MinutesTab(results: FrontendData) {
//    LazyColumn(
//        modifier = Modifier.fillMaxSize(),
//        contentPadding = PaddingValues(16.dp),
//    ) {
//        item {
//            Card(
//                modifier = Modifier.fillMaxWidth(),
//            ) {
//                Column(modifier = Modifier.padding(16.dp)) {
//                    Text(
//                        text = "Meeting Minutes",
//                        style = MaterialTheme.typography.titleMedium,
//                    )
//                    Spacer(modifier = Modifier.height(8.dp))
//                    if (results.meetingMinutes.generatedAt != null) {
//                        Text(
//                            text = "Generated at: ${results.meetingMinutes.generatedAt}",
//                            style = MaterialTheme.typography.bodySmall,
//                            color = MaterialTheme.colorScheme.onSurfaceVariant,
//                        )
//                        Spacer(modifier = Modifier.height(8.dp))
//                    }
//                    // Display markdown content as plain text (TODO: add markdown rendering)
//                    Text(
//                        text = results.meetingMinutes.content,
//                        style = MaterialTheme.typography.bodyMedium,
//                    )
//                }
//            }
//        }
//    }
//}
//
//@Composable
//private fun ParticipantsTab(results: FrontendData) {
//    LazyColumn(
//        modifier = Modifier.fillMaxSize(),
//        contentPadding = PaddingValues(16.dp),
//        verticalArrangement = Arrangement.spacedBy(12.dp),
//    ) {
//        item {
//            Text(
//                text = "Total Participants: ${results.participants.totalCount}",
//                style = MaterialTheme.typography.titleMedium,
//            )
//        }
//
//        items(results.participants.items) { participant ->
//            Card(
//                modifier = Modifier.fillMaxWidth(),
//            ) {
//                Column(modifier = Modifier.padding(12.dp)) {
//                    Text(
//                        text = participant.name,
//                        style = MaterialTheme.typography.titleSmall,
//                    )
//                    Spacer(modifier = Modifier.height(4.dp))
//                    Text(
//                        text = "Speaking time: ${formatSeconds(participant.duration.toInt())}",
//                        style = MaterialTheme.typography.bodySmall,
//                    )
//                    Text(
//                        text = "Speech segments: ${participant.speechSegments}",
//                        style = MaterialTheme.typography.bodySmall,
//                    )
//                }
//            }
//        }
//    }
//}
//
//@Composable
//private fun KnowledgeGraphTab(results: FrontendData) {
//    val kg = results.knowledgeGraph
//
//    LazyColumn(
//        modifier = Modifier.fillMaxSize(),
//        contentPadding = PaddingValues(16.dp),
//        verticalArrangement = Arrangement.spacedBy(12.dp),
//    ) {
//        if (kg == null || kg.entities.isEmpty()) {
//            item {
//                Text("No knowledge graph data available")
//            }
//        } else {
//            item {
//                Text(
//                    text = "Total Entities: ${kg.totalEntities}",
//                    style = MaterialTheme.typography.titleMedium,
//                )
//            }
//
//            items(kg.entities) { entity ->
//                Card(
//                    modifier = Modifier.fillMaxWidth(),
//                ) {
//                    Column(modifier = Modifier.padding(12.dp)) {
//                        Row(
//                            modifier = Modifier.fillMaxWidth(),
//                            horizontalArrangement = Arrangement.SpaceBetween,
//                        ) {
//                            Text(
//                                text = entity.name,
//                                style = MaterialTheme.typography.titleSmall,
//                            )
//                            AssistChip(
//                                onClick = {},
//                                label = { Text(entity.type) }
//                            )
//                        }
//                        if (entity.confidence != null) {
//                            Text(
//                                text = "Confidence: ${(entity.confidence * 100).toInt()}%",
//                                style = MaterialTheme.typography.bodySmall,
//                                color = MaterialTheme.colorScheme.onSurfaceVariant,
//                            )
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
//
//private fun formatTimestamp(seconds: Double): String {
//    val totalSeconds = seconds.toInt()
//    val minutes = totalSeconds / 60
//    val secs = totalSeconds % 60
//    return "${minutes.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}"
//}
//
//private fun formatSeconds(seconds: Int): String {
//    val minutes = seconds / 60
//    val secs = seconds % 60
//    return "$minutes:${secs.toString().padStart(2, '0')}"
//}
