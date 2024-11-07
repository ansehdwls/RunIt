package com.zoku.network.model.request

import java.sql.Date

data class PostRunningRecordRequest(
    val track: Track,
    val record: Record,
    val paceList: List<Pace>
)

data class Track(
    val path: String,
)

data class Record(
    val distance: Double,
    val startTime: String,
    val endTime: String,
    val bpm: Int,
)

data class Pace(
    val pace: Int,
    val bpm: Int,
)
