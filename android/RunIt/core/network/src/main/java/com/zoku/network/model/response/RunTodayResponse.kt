package com.zoku.network.model.response

data class RunTodayResponse(
    val data : RunToday,
    val message: String
)

data class RunToday(
    val distance : Double,
    val time: Double,
    val pace: Double
)