package com.zoku.network.api

import com.zoku.network.model.GetRunningRecordResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.Date

interface RunningApi {

    @GET("api/run")
    fun getRunningRecord(@Query("date") date: Date) : Response<GetRunningRecordResponse>
}