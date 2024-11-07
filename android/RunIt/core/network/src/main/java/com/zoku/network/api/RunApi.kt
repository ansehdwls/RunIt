package com.zoku.network.api

import retrofit2.http.GET

interface RunApi {

    @GET("api/run")
    suspend fun getRunAllHistory()

    @GET("api/run/{recordId}")
    suspend fun getRunHistory()
}