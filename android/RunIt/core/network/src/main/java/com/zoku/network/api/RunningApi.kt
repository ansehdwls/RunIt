package com.zoku.network.api

import com.zoku.network.model.request.TestSumRequest
import com.zoku.network.model.response.GetRunningRecordResponse
import com.zoku.network.model.response.RunningAllHistoryResponse
import com.zoku.network.model.response.RunningHistoryResponse
import com.zoku.network.model.response.TestSumData
import com.zoku.network.model.response.TestSumResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.Date

interface RunningApi {

    @GET("api/run")
    fun getRunningRecord(@Query("date") date: Date) : Response<GetRunningRecordResponse>

    @POST("api/auth/test-sum")
    suspend fun testSum(@Body request: TestSumRequest): Response<TestSumResponse>

    @GET("api/run")
    suspend fun getRunAllHistory() : Response<RunningAllHistoryResponse>

    @GET("api/run/{recordId}")
    suspend fun getRunHistory(@Path("recordId") recordId : Int) : Response<RunningHistoryResponse>

}