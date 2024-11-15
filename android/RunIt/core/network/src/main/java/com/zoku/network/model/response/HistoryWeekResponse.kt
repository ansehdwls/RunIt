package com.zoku.network.model.response

import java.sql.Timestamp

data class HistoryWeekResponse(
    val data : List<List<WeekList>>,
    val message: String
)

data class WeekList(
    val id : Int,
    val distance : Double,
    val bpm : Int,
    val startTime : Timestamp,
    val endTime : Timestamp ,
    val name: String,
    val imageUrl : String
)