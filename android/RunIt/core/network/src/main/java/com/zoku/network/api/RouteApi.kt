package com.zoku.network.api

import com.zoku.network.model.response.RouteResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface RouteApi {

    @GET("api/route/coordinate/{recordId}")
    suspend fun getRouteTrackInfo(
        @Path("recordId") recordId: Int
    ) : Response<RouteResponse>
}