package com.zoku.data.repository

import com.zoku.data.ApiHandler
import com.zoku.data.NetworkResult
import com.zoku.network.api.RouteApi
import com.zoku.network.model.response.RouteInfo
import com.zoku.network.model.response.RouteInfoList
import com.zoku.network.model.response.RouteResponse
import javax.inject.Inject

class RouteRepositoryImpl @Inject constructor(
   private val routeApi: RouteApi
) : RouteRepository, ApiHandler
{
    override suspend fun getRouteList(recordId: Int): NetworkResult<RouteResponse> {
        return handleApi {
            routeApi.getRouteTrackInfo(recordId)
        }
    }
}
