package com.zoku.data.repository

import com.zoku.data.NetworkResult
import com.zoku.network.model.response.AttendanceResponse

interface AttendanceRepository {
    suspend fun getAttendanceWeek() : NetworkResult<AttendanceResponse>
}