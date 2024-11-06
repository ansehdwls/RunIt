package com.zoku.data.repository

import com.zoku.data.NetworkResult
import com.zoku.network.model.response.RegisterResponse
import com.zoku.network.model.response.UserResponse

interface UserRepository {
    suspend fun getUserData() : NetworkResult<UserResponse>

    suspend fun patchFCMToken(token : String) : NetworkResult<RegisterResponse>
}