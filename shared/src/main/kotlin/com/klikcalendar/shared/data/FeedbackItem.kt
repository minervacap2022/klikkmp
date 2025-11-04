package com.klikcalendar.shared.data

data class FeedbackItem(
    val id: String,
    val title: String,
    val description: String,
    val content: String,
    val type: FeedbackType,
)

enum class FeedbackType {
    SPEAKER_IDENTIFICATION,  // "SPEAKER_00 ��������"
    TASK_EXTRACTION,         // ������ȡ��֤
    RELATIONSHIP_EXTRACTION, // ��ϵ��ȡ��֤
    MEETING_RESULT,         // ��������֤
}