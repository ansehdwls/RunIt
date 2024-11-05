package com.zoku.data.repository

import android.util.Log
import com.google.gson.Gson
import com.zoku.data.ApiHandler
import com.zoku.data.NetworkResult
import com.zoku.data.model.LoginData
import com.zoku.network.api.LoginApi
import com.zoku.network.model.request.RegisterRequest
import com.zoku.network.model.response.LoginResponse
import com.zoku.network.model.response.RegisterResponse
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "LoginRepositoryImpl"
@Singleton
class LoginRepositoryImpl @Inject constructor
    (private val loginApi: LoginApi) : LoginRepository, ApiHandler {
    override suspend fun postLogin(userNumber: String) : NetworkResult<LoginResponse> {
        Log.d(TAG, "postLogin: ${userNumber}")
        return handleApi{
            loginApi.login(mapOf( Pair("userNumber", userNumber)))
        }
    }

    override suspend fun postRefresh(refreshToken: String): NetworkResult<LoginResponse> {
        Log.d(TAG, "postRefresh: ${refreshToken}")
        return handleApi{
            loginApi.refreshLogin(mapOf( Pair("refreshToken", refreshToken)))
        }
    }

    override suspend fun postRegister(user: RegisterRequest): NetworkResult<RegisterResponse> {
        Log.d(TAG, "postRegister: ${user}")
        return handleApi{
            loginApi.register(user)
        }
    }
}