package com.zoku.data.repository

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {

    val accessTokenFlow: Flow<String?>

    val refreshTokenFlow: Flow<String?>

    suspend fun saveTokens(accessToken: String, refreshToken: String)

    suspend fun clearTokens()
}