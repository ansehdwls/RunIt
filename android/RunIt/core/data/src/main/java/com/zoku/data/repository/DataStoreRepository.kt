package com.zoku.data.repository

import com.zoku.data.model.UserData
import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {

    val accessTokenFlow: Flow<String?>

    val refreshTokenFlow: Flow<String?>

    val groupId : Flow<Int>

    val userName : Flow<String>
    val userId : Flow <Int>
    suspend fun saveTokens(accessToken: String, refreshToken: String)

    suspend fun saveUser(userData: UserData)

    suspend fun clearTokens()
}