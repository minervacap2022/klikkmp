package com.klikcalendar.shared.model.service

import android.util.Log
import com.klikcalendar.shared.model.CalendarEvent
import com.klikcalendar.shared.model.data.MeetingInfo
import kotlinx.serialization.json.Json
import retrofit2.Response

class MeetingApiServiceImp() {
    private val apiService = ApiConfig.meetingApiService
    private val json = Json {
        ignoreUnknownKeys = true
    }

    suspend fun fetchMeetingInfo(): Result<CalendarEvent> = try {
        val response = apiService.getMeetingInfo()
        if (response.isSuccessful) {
            val jsonString = response.body() ?: throw Exception("Empty body")
            val event = json.decodeFromString<CalendarEvent>(jsonString)
            Result.success(event)
        } else {
            Result.failure(Exception("HTTP ${response.code()}"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}