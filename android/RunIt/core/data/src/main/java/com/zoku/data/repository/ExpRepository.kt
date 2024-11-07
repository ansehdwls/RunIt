package com.zoku.data.repository

import com.zoku.data.NetworkResult
import com.zoku.network.model.response.ExpAllResponse
import com.zoku.network.model.response.ExpWeekResponse

interface ExpRepository {

    suspend fun getAllExpHistory() : NetworkResult<ExpAllResponse>

    suspend fun getWeekExpHistory() : NetworkResult<ExpWeekResponse>
}