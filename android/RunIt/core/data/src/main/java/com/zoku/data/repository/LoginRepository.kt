package com.zoku.data.repository

import com.zoku.data.NetworkResult
import com.zoku.data.model.LoginData
import com.zoku.network.model.request.RegisterRequest
import com.zoku.network.model.response.LoginResponse
import com.zoku.network.model.response.RegisterResponse
import okhttp3.ResponseBody
import retrofit2.Call

interface LoginRepository {
    suspend fun postLogin(userNumber : String): NetworkResult<LoginResponse>

    suspend fun postRefresh(refreshToken: String) :NetworkResult<LoginResponse>

    suspend fun postRegister(user: RegisterRequest) : NetworkResult<RegisterResponse>
}