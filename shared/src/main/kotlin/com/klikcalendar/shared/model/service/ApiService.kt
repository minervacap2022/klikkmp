package com.klikcalendar.shared.model.service

import com.klikcalendar.shared.model.data.MeetingInfo
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("getmeetinginfo")
    suspend fun getMeetingInfo(): Response<MeetingInfo>
}