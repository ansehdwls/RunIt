package com.zoku.network.api

import com.zoku.network.model.request.TestSumRequest
import com.zoku.network.model.response.GetRunningRecordResponse
import com.zoku.network.model.response.HistoryWeekResponse
import com.zoku.network.model.response.TestSumData
import com.zoku.network.model.response.PostRunningRecordResponse
import com.zoku.network.model.response.RunPracticeResponse
import com.zoku.network.model.response.RunRecordDetailResponse
import com.zoku.network.model.response.TestSumResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Part
import retrofit2.http.Query
import java.util.Date

interface RunningApi {

    @GET("api/run")
    fun getRunningRecord(@Query("date") date: Date): Response<GetRunningRecordResponse>

    @POST("api/auth/test-sum")
    suspend fun testSum(@Body request: TestSumRequest): Response<TestSumResponse>


    @Multipart
    @POST("api/run")
    suspend fun postRunningRecord(
        @Part dto: MultipartBody.Part,
        @Part images: MultipartBody.Part
    ): Response<PostRunningRecordResponse>

    @GET("api/run/weekList/{today}")
    suspend fun getWeekHistory(
        @Path("today") today : String
    ) : Response<HistoryWeekResponse>

    @GET("api/run/{recordId}")
    suspend fun getRunRecordDetail(@Path("recordId") recordId : Int) : Response<RunRecordDetailResponse>

    @GET("api/run/practice")
    suspend fun getRecordMode() : Response<RunPracticeResponse>
}