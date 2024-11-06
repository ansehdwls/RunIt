package com.zoku.network.api

import com.zoku.network.model.response.RegisterResponse
import com.zoku.network.model.response.UserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

interface UserApi {
    @GET("api/user/me")
    suspend fun getUserData() : Response<UserResponse>

    @PATCH
    suspend fun patchFCMToken() : Response<RegisterResponse>
}