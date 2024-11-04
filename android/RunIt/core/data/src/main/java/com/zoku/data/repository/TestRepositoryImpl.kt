package com.zoku.data.repository

import com.zoku.data.model.MyTestData
import com.zoku.network.api.TestApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TestRepositoryImpl @Inject constructor(
    private val testApi: TestApi
) : TestRepository {
    override suspend fun getComments(page: String): Result<List<MyTestData>> {
        val response = testApi.getComments(1)
        if (response.isSuccessful) {
            response.body()?.let {
                return Result.success(it.map { data ->
                    MyTestData(
                        postId = data.postId,
                        id = data.id,
                        name = data.name,
                        email = data.email,
                        body = data.body
                    )
                })
            }
            return Result.success(emptyList())
        } else {
            return Result.failure(Exception())
        }

    }
}

// 1번. 서버 URL 잘못 지정했을 때
// 2번. Request 잘못 보냈을떄 (서버가 오류 이류를 알려줌)
// 3번 성공