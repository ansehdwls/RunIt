package com.zoku.network.api

import com.zoku.network.model.response.ExpAllResponse
import com.zoku.network.model.response.ExpWeekResponse
import retrofit2.Response
import retrofit2.http.GET

interface ExpApi {
    @GET("api/exp")
    suspend fun getAllExpHistory() : Response<ExpAllResponse>

    @GET("api/week/exp")
    suspend fun getWeekExpHistory() : Response<ExpWeekResponse>
}