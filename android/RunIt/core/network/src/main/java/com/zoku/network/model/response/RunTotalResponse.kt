package com.zoku.network.model.response

data class RunTotalResponse(
    val data : RunTotal,
    val message: String
)
data class RunTotal(
    val totalDistance: Double,
    val totalTime : Double,
    val weekDistance : Double,
    val weekTime: Double
)

data class AllTotal(
    val totalDistance: Double,
    val totalTime : Double,
)

data class WeekTotal(
    val weekDistance : Double,
    val weekTime: Double
)