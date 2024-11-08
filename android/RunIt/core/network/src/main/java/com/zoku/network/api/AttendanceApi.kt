package com.zoku.network.api

import com.zoku.network.model.response.AttendanceResponse
import retrofit2.Response
import retrofit2.http.GET

interface AttendanceApi {
    @GET("api/attendance/week")
    suspend fun getAttendanceWeek() : Response<AttendanceResponse>
}