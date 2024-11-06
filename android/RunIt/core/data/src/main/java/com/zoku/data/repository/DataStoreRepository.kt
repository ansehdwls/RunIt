package com.zoku.data.repository

import com.zoku.data.model.UserData
import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {

    val accessTokenFlow: Flow<String?>

    val refreshTokenFlow: Flow<String?>

    suspend fun saveTokens(accessToken: String, refreshToken: String)

    suspend fun saveUser(userData: UserData)

    suspend fun clearTokens()
}