package com.zoku.network.api

import com.zoku.network.model.request.RegisterRequest
import com.zoku.network.model.response.LoginResponse
import com.zoku.network.model.response.RegisterResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface LoginApi {
    @POST("api/auth/login")
    suspend fun login(@Body userNumber: Map<String, String>): Response<LoginResponse>

    @POST("api/auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest) : Response<RegisterResponse>

    @POST("api/auth/token")
    suspend fun refreshLogin(@Body refreshToken : Map<String, String>) : Response<LoginResponse>
}