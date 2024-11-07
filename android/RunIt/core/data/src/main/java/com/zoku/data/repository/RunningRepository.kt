package com.zoku.data.repository

import com.zoku.data.NetworkResult
import com.zoku.data.model.MyTestData
import com.zoku.network.model.request.TestSumRequest
import com.zoku.network.model.response.RunningAllHistoryResponse
import com.zoku.network.model.response.RunningHistoryResponse
import com.zoku.network.model.response.TestSumResponse
import java.util.Date

interface RunningRepository {

    suspend fun getRunningRecord(date: Date): Result<List<MyTestData>>

    suspend fun postTestSum(testSumRequest: TestSumRequest) : NetworkResult<TestSumResponse>

    suspend fun getRunningAllHistory() : NetworkResult<RunningAllHistoryResponse>

    suspend fun getRunningHistory(recordId : Int) : NetworkResult<RunningHistoryResponse>
}