package com.zoku.data.repository

import com.zoku.data.model.MyTestData
import java.util.Date

interface RunningRepository {

    suspend fun getRunningRecord(date: Date): Result<List<MyTestData>>
}