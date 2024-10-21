package com.zoku.data.repository

import com.zoku.data.model.MyTestData

interface TestRepository {

    suspend fun getComments(page: String): Result<List<MyTestData>>
}