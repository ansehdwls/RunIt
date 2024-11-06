package com.zoku.data.repository

import com.zoku.data.ApiHandler
import com.zoku.data.NetworkResult
import com.zoku.network.api.UserApi
import com.zoku.network.model.response.RegisterResponse
import com.zoku.network.model.response.UserResponse
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi
) : UserRepository, ApiHandler{
    override suspend fun getUserData(): NetworkResult<UserResponse> {
       return handleApi {
            userApi.getUserData()
        }
    }

    override suspend fun patchFCMToken(token: String): NetworkResult<RegisterResponse> {
        return handleApi {
            userApi.patchFCMToken(mapOf(Pair("fcmToken",token)))
        }
    }
}