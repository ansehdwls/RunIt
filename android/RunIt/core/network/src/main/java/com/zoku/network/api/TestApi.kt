package com.zoku.network.api

import com.zoku.network.model.TestApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TestApi {
    @GET("comments")
    suspend fun getComments(@Query("postId") postId: Int): Response<List<TestApiResponse>>
}