package com.zoku.data.repository

import com.zoku.data.NetworkResult
import com.zoku.data.model.MyTestData
import com.zoku.network.model.request.TestSumRequest
import com.zoku.network.model.response.HistoryWeekResponse
import com.zoku.network.model.response.MessageResponse
import com.zoku.network.model.response.PostRunningRecordResponse
import com.zoku.network.model.response.RunPracticeResponse
import com.zoku.network.model.response.RunRecordDetailResponse
import com.zoku.network.model.response.RunTodayResponse
import com.zoku.network.model.response.RunTotalResponse
import com.zoku.network.model.response.RunWeekListResponse
import com.zoku.network.model.response.TestSumResponse
import okhttp3.MultipartBody
import java.util.Date

interface RunningRepository {

    suspend fun getRunningRecord(date: Date): Result<List<MyTestData>>

    suspend fun postTestSum(testSumRequest: TestSumRequest) : NetworkResult<TestSumResponse>

    suspend fun getRunRecordDetail(recordId : Int) : NetworkResult<RunRecordDetailResponse>

    suspend fun postRunningRecord(
        dto: MultipartBody.Part,
        images: MultipartBody.Part
    ): NetworkResult<PostRunningRecordResponse>

    suspend fun getWeekList(
        today : String
    ) : NetworkResult<HistoryWeekResponse>

    suspend fun getRunPracticeList() : NetworkResult<RunPracticeResponse>

    suspend fun updateRunPracticeMode(recordId: Int) : NetworkResult<MessageResponse>

    suspend fun getRunToday() : NetworkResult<RunTodayResponse>

    suspend fun getRunTotal() : NetworkResult<RunTotalResponse>

    suspend fun getRunWeek() : NetworkResult<RunWeekListResponse>
}