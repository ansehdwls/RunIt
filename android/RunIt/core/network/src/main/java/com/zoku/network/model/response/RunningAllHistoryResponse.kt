package com.zoku.network.model.response

import java.sql.Timestamp


data class PaceRecord(
    val bpmList: Int,
    val durationList : Int? = 0
)