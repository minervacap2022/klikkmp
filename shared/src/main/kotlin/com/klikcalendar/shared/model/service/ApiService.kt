package com.klikcalendar.shared.model.service

import com.klikcalendar.shared.model.CalendarEvent
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("getmeetinginfo")
    suspend fun getMeetingInfo(): Response<String>
}