package com.klikcalendar.app.data

import com.klikcalendar.app.model.CalendarEvent
import com.klikcalendar.app.model.MeetingMemo
import com.klikcalendar.app.model.OperationTask
import com.klikcalendar.app.model.QuickAction
import com.klikcalendar.app.model.StatusOverlayState
import com.klikcalendar.app.model.TimelineDay
import com.klikcalendar.app.model.TranscriptRecord
import com.klikcalendar.app.model.WorkLifeNode
import com.klikcalendar.app.ui.screens.FeedbackItem

/**
 * Repository interface for Klik Calendar data.
 * This interface defines all data operations that will be backed by a remote API.
 *
 * To integrate with backend:
 * 1. Implement this interface with API calls (e.g., using Ktor client)
 * 2. Add suspend functions for async data fetching
 * 3. Implement proper error handling and caching
 * 4. Use Flow<T> for reactive data streams if needed
 */
interface KlikRepository {
    /**
     * Fetch the current status overlay state including device indicators,
     * sync status, and alerts.
     *
     * Backend endpoint: GET /api/v1/status
     */
    suspend fun getStatusOverlay(): StatusOverlayState

    /**
     * Fetch timeline data for the calendar view.
     *
     * Backend endpoint: GET /api/v1/timeline?startDate=...&endDate=...
     */
    suspend fun getTimeline(): List<TimelineDay>

    /**
     * Fetch transcript records.
     *
     * Backend endpoint: GET /api/v1/transcripts
     */
    suspend fun getTranscripts(): List<TranscriptRecord>

    /**
     * Fetch meeting memos.
     *
     * Backend endpoint: GET /api/v1/memos
     */
    suspend fun getMemos(): List<MeetingMemo>

    /**
     * Fetch knowledge graph nodes for Work/Life view.
     *
     * Backend endpoint: GET /api/v1/knowledge-graph
     */
    suspend fun getKnowledgeGraph(): List<WorkLifeNode>

    /**
     * Fetch operational tasks.
     *
     * Backend endpoint: GET /api/v1/operations
     */
    suspend fun getOperations(): List<OperationTask>

    /**
     * Fetch available quick actions.
     *
     * Backend endpoint: GET /api/v1/quick-actions
     */
    suspend fun getQuickActions(): List<QuickAction>

    /**
     * Fetch pending feedback items.
     *
     * Backend endpoint: GET /api/v1/feedback/pending
     */
    suspend fun getFeedbackItems(): List<FeedbackItem>

    /**
     * Submit feedback for a specific item.
     *
     * Backend endpoint: POST /api/v1/feedback/{itemId}
     * @param itemId The ID of the feedback item
     * @param approved Whether the item was approved (true), rejected (false), or uncertain (null)
     */
    suspend fun submitFeedback(itemId: String, approved: Boolean?)
}
