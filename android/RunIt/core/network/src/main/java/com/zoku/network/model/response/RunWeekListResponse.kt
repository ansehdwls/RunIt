package com.zoku.network.model.response

data class RunWeekListResponse(
    val data : RunWeekList,
    val message: String
)
data class RunWeekList(
    val disList : List<Double>,
    val timeList : List<Double>,
    val paceList : List<Double>
)
