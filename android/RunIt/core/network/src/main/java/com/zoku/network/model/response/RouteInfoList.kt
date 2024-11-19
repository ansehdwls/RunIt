package com.zoku.network.model.response

data class RouteInfoList(
    val routeInfo: List<RouteInfo>
)

data class RouteInfo(
    val latitude : Double,
    val longitude : Double
)