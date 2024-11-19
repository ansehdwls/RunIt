package com.zoku.data.repository

import com.zoku.data.ApiHandler
import com.zoku.data.NetworkResult
import com.zoku.network.api.AttendanceApi
import com.zoku.network.model.response.AttendanceResponse
import javax.inject.Inject

class AttendanceRepositoryImpl @Inject constructor(
    private val attendanceApi: AttendanceApi
) : AttendanceRepository, ApiHandler{
    override suspend fun getAttendanceWeek(): NetworkResult<AttendanceResponse> {
        return handleApi {
            attendanceApi.getAttendanceWeek()
        }
    }
}