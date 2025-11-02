package com.klikcalendar.app.data.backend

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Backend API Response Models
 * These models match the backend API structure from http://86.38.238.159:8000
 */

// ========== Upload Response ==========
@Serializable
data class UploadResponse(
    @SerialName("sessionId") val sessionId: String,
    @SerialName("status") val status: String,
    @SerialName("message") val message: String,
    @SerialName("runId") val runId: String,
)

// ========== Status Response ==========
@Serializable
data class StatusResponse(
    @SerialName("runId") val runId: String,
    @SerialName("sessionId") val sessionId: String,
    @SerialName("status") val status: PipelineStatus,
    @SerialName("startTime") val startTime: String? = null,
    @SerialName("endTime") val endTime: String? = null,
    @SerialName("executionTime") val executionTime: Double? = null,
    @SerialName("inputFile") val inputFile: String? = null,
    @SerialName("logs") val logs: List<String>? = null,
    @SerialName("error") val error: String? = null,
    @SerialName("completeResult") val completeResult: CompleteResult? = null,
    @SerialName("frontendData") val frontendData: FrontendData? = null,
)

@Serializable
enum class PipelineStatus {
    @SerialName("RUNNING") RUNNING,
    @SerialName("COMPLETED") COMPLETED,
    @SerialName("FAILED") FAILED,
    @SerialName("started") STARTED,
}

// ========== Frontend Data (Optimized for UI) ==========
@Serializable
data class FrontendData(
    @SerialName("session_id") val sessionId: String,
    @SerialName("timestamp") val timestamp: String,
    @SerialName("transcript") val transcript: TranscriptData,
    @SerialName("todos") val todos: TodosData,
    @SerialName("meeting_minutes") val meetingMinutes: MeetingMinutesData,
    @SerialName("participants") val participants: ParticipantsData,
    @SerialName("knowledge_graph") val knowledgeGraph: KnowledgeGraphData? = null,
)

@Serializable
data class TranscriptData(
    @SerialName("segments") val segments: List<TranscriptSegment>,
    @SerialName("total_segments") val totalSegments: Int,
    @SerialName("total_duration") val totalDuration: Double,
    @SerialName("speakers_detected") val speakersDetected: Int,
    @SerialName("language") val language: String,
)

@Serializable
data class TranscriptSegment(
    @SerialName("start") val start: Double,
    @SerialName("end") val end: Double,
    @SerialName("text") val text: String,
    @SerialName("speaker") val speaker: String,
    @SerialName("speaker_name") val speakerName: String? = null,
    @SerialName("speaker_profile_id") val speakerProfileId: String? = null,
    @SerialName("speaker_user_id") val speakerUserId: String? = null,
    @SerialName("duration") val duration: Double,
)

@Serializable
data class TodosData(
    @SerialName("items") val items: List<TodoItem>,
    @SerialName("total_count") val totalCount: Int,
)

@Serializable
data class TodoItem(
    @SerialName("id") val id: String,
    @SerialName("text") val text: String,
    @SerialName("assignee") val assignee: String? = null,
    @SerialName("due_date") val dueDate: String? = null,
    @SerialName("status") val status: String,
    @SerialName("category") val category: String? = null,
    @SerialName("priority") val priority: String? = null,
    @SerialName("timestamp") val timestamp: Double,
    @SerialName("assignee_profile_id") val assigneeProfileId: String? = null,
)

@Serializable
data class MeetingMinutesData(
    @SerialName("content") val content: String,
    @SerialName("generated_at") val generatedAt: String? = null,
)

@Serializable
data class ParticipantsData(
    @SerialName("items") val items: List<ParticipantItem>,
    @SerialName("total_count") val totalCount: Int,
)

@Serializable
data class ParticipantItem(
    @SerialName("name") val name: String,
    @SerialName("profile_id") val profileId: String,
    @SerialName("duration") val duration: Double,
    @SerialName("speech_segments") val speechSegments: Int,
)

@Serializable
data class KnowledgeGraphData(
    @SerialName("entities") val entities: List<KGEntity>,
    @SerialName("total_entities") val totalEntities: Int,
)

@Serializable
data class KGEntity(
    @SerialName("id") val id: String,
    @SerialName("type") val type: String,
    @SerialName("name") val name: String,
    @SerialName("confidence") val confidence: Double? = null,
)

// ========== Complete Result (Raw Pipeline Output - Fallback) ==========
@Serializable
data class CompleteResult(
    @SerialName("session_id") val sessionId: String,
    @SerialName("asr_result") val asrResult: AsrResult? = null,
    @SerialName("todos") val todos: List<TodoItem>? = null,
    @SerialName("meeting_minutes") val meetingMinutes: String? = null,
    @SerialName("kg_entities") val kgEntities: List<KGEntity>? = null,
    @SerialName("kg_relations") val kgRelations: List<KGRelation>? = null,
    @SerialName("participants") val participants: Map<String, ParticipantInfo>? = null,
)

@Serializable
data class AsrResult(
    @SerialName("segments") val segments: List<TranscriptSegment>,
    @SerialName("num_speakers") val numSpeakers: Int? = null,
)

@Serializable
data class KGRelation(
    @SerialName("from") val from: String,
    @SerialName("to") val to: String,
    @SerialName("type") val type: String,
    @SerialName("weight") val weight: Double? = null,
)

@Serializable
data class ParticipantInfo(
    @SerialName("name") val name: String,
    @SerialName("profile_id") val profileId: String,
    @SerialName("duration") val duration: Double,
    @SerialName("speech_segments") val speechSegments: Int,
)

// ========== Error Response ==========
@Serializable
data class ErrorResponse(
    @SerialName("runId") val runId: String? = null,
    @SerialName("sessionId") val sessionId: String? = null,
    @SerialName("status") val status: String,
    @SerialName("error") val error: String,
    @SerialName("message") val message: String? = null,
)
