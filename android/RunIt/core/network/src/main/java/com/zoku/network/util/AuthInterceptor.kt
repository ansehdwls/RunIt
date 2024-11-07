package com.zoku.network.util

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val dataStore: DataStore<Preferences>
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val url = originalRequest.url.toString()

        // 특정 도메인이 아닐때만 추가
        return if (!url.contains("api/auth") ){
            // DataStore에서 토큰 가져오기
            val token = runBlocking {
                dataStore.data.map { preferences ->
                    preferences[stringPreferencesKey("access_token")] ?: ""
                }.first()
            }

            // 토큰이 있다면 Authorization 헤더 추가
            val newRequest = if (token.isNotEmpty()) {
                originalRequest.newBuilder()
                    .header("Authorization", "$token")
                    .build()
            } else {
                originalRequest
            }

            chain.proceed(newRequest)
        } else {
            // 다른 도메인에 대한 요청은 그대로 진행
            chain.proceed(originalRequest)
        }
    }
}

