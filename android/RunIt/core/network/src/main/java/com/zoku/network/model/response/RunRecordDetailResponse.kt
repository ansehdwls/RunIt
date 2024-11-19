package com.zoku.network.model.response

import java.sql.Timestamp
import java.time.LocalDateTime

data class RunRecordDetailResponse(
    val data : RunRecordDetail?,
    val message: String
)
data class RunRecordDetail(
    val id: Int = 0,
    val distance: Double = 0.0,
    val bpm: Int = 0,
    val startTime: String = "",
    val endTime: String = "",
    val paceList: List<PaceRecord> = emptyList(),
)