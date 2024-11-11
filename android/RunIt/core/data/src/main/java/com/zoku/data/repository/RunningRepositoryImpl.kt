package com.zoku.data.repository

import com.zoku.data.ApiHandler
import com.zoku.data.NetworkResult
import com.zoku.data.model.MyTestData
import com.zoku.network.api.RunningApi
import com.zoku.network.model.request.PostRunningRecordRequest
import com.zoku.network.model.request.TestSumRequest
import com.zoku.network.model.response.HistoryWeekResponse
import com.zoku.network.model.response.RunningAllHistoryResponse
import com.zoku.network.model.response.RunningHistoryResponse
import com.zoku.network.model.response.PostRunningRecordResponse
import com.zoku.network.model.response.TestSumResponse
import okhttp3.MultipartBody
import retrofit2.Response
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RunningRepositoryImpl @Inject constructor(
    private val runningApi: RunningApi
) : RunningRepository, ApiHandler {


    override suspend fun getRunningRecord(date: Date): Result<List<MyTestData>> {
        TODO("Not yet implemented")
    }

    override suspend fun postTestSum(testSumRequest: TestSumRequest): NetworkResult<TestSumResponse> {
        return handleApi { runningApi.testSum(testSumRequest) }
    }

    override suspend fun getRunningAllHistory(): NetworkResult<RunningAllHistoryResponse> {
        return handleApi { runningApi.getRunAllHistory() }
    }

    override suspend fun getRunningHistory(recordId: Int): NetworkResult<RunningHistoryResponse> {
        return handleApi { runningApi.getRunHistory(recordId) }
    }
    override suspend fun postRunningRecord(dto: MultipartBody.Part, images: MultipartBody.Part): NetworkResult<PostRunningRecordResponse> {
        return handleApi { runningApi.postRunningRecord(dto, images) }
    }

    override suspend fun getWeekList(today: String): NetworkResult<HistoryWeekResponse> {
        return handleApi { runningApi.getWeekHistory(today) }
    }

}