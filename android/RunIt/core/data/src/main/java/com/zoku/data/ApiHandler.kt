package com.zoku.data

import android.util.Log
import retrofit2.HttpException
import retrofit2.Response

interface ApiHandler {
    suspend fun <T : Any> handleApi(
        execute: suspend () -> Response<T>
    ): NetworkResult<T> {
        return try {
            val response = execute()
            Log.d("확인", "handleApi: $response")
            if (response.isSuccessful) {
                NetworkResult.Success(response.code(), response.body()!!)
            } else {
                NetworkResult.Error(response.code(), response.errorBody()?.string())
            }
        } catch (e: HttpException) {
            Log.d("확인", "handleApi: $e")
            NetworkResult.Error(e.code(), e.message())
        } catch (e: Throwable) {
            Log.d("확인", "handleApi: $e")
            NetworkResult.Exception(e)
        }
    }
}