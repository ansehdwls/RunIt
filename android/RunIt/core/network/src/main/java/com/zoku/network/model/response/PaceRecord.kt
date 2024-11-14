package com.zoku.network.model.response


data class PaceRecord(
    // 시간당 bpm
    val bpmList: Int,
    // 100m 페이스 값 :
    val durationList : Int? = 0
)