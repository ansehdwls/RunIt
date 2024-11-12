package com.zoku.network.model.response

import java.sql.Timestamp
import java.time.LocalDateTime

data class RunRecordDetailResponse(
    val data : RunRecordDetail,
    val message: String
)
data class RunRecordDetail(
    val id: Int ,
    val distance: Double,
    val bpm: Int,
    val startTime: String,
    val endTime: String,
    val paceList: List<PaceRecord> ,
)