package com.zoku.data.repository

import com.zoku.data.model.MyTestData
import java.util.Date

class RunningRepositoryImpl : RunningRepository{
    override suspend fun getRunningRecord(date: Date): Result<List<MyTestData>> {
        TODO("Not yet implemented")
    }
}