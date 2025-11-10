package com.klikcalendar.shared.model.service

import android.util.Log
import com.klikcalendar.shared.model.data.MeetingInfo
import retrofit2.Response

class MeetingApiServiceImp {
    private val apiService = ApiConfig.meetingApiService

    suspend fun fetchMeetingInfo(): Result<MeetingInfo> {
        return try {
            val response: Response<MeetingInfo> = apiService.getMeetingInfo()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("HTTP ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}