package com.klikcalendar.shared.model.data

import com.google.gson.annotations.SerializedName
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class MeetingInfo(
    @SerializedName("meetingTime") val meetingTime: String,
    @SerializedName("initiator") val initiator: String,
    @SerializedName("subject") val subject: String
) {
    // 可选：提供格式化后的时间字符串（用于 UI 显示）
    fun getFormattedTime(): String {
        return try {
            val instant = Instant.parse(meetingTime)
            val zonedDateTime = instant.atZone(ZoneId.systemDefault())
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(zonedDateTime)
        } catch (e: Exception) {
            meetingTime // 解析失败则返回原始字符串
        }
    }
}