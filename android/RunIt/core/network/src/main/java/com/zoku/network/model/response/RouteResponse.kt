package com.zoku.network.model.response

data class RouteResponse(
    val data : RoutePath,
    val message: String
)

data class RoutePath(
    val path : String
)

