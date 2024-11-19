package com.zoku.data.repository

import com.zoku.data.NetworkResult
import com.zoku.network.model.response.RouteInfo
import com.zoku.network.model.response.RouteInfoList
import com.zoku.network.model.response.RouteResponse

interface RouteRepository{
    suspend fun getRouteList(recordId : Int) : NetworkResult<RouteResponse>
}
