package com.zoku.data.repository

import com.zoku.data.ApiHandler
import com.zoku.data.NetworkResult
import com.zoku.network.api.ExpApi
import com.zoku.network.model.response.ExpAllResponse
import com.zoku.network.model.response.ExpWeekResponse
import javax.inject.Inject

class ExpRepositoryImpl @Inject constructor(
    private val expApi: ExpApi
) : ExpRepository, ApiHandler {

    override suspend fun getAllExpHistory(): NetworkResult<ExpAllResponse> {
        return handleApi{
            expApi.getAllExpHistory()
        }
    }

    override suspend fun getWeekExpHistory(): NetworkResult<ExpWeekResponse> {
        return handleApi {
            expApi.getWeekExpHistory()
        }
    }
}