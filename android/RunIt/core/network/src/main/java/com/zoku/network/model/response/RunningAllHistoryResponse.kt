package com.zoku.network.model.response

import java.sql.Timestamp

data class RunningAllHistoryResponse(
    val data : List<RunningAllHistory>,
    val message: String
)

data class RunningHistoryResponse(
    val data : List<RunningHistoryDetail>,
    val message: String
)

data class RunningAllHistory(
    val id : Int,
    val distance: Int,
    val bpm: Int,
    val startTime: String,
    val endTime: String,
    val name: String,
    val imageUrl : String?
)

data class RunningHistoryDetail(
    val id: Int,
    val distance: Int,
    val bpm: Int,
    val startTime: String,
    val endTime: String,
    val paceList : List<PaceRecord>
)

data class PaceRecord(
    val bpmList: Int,
    val durationList : Int? = 0
)