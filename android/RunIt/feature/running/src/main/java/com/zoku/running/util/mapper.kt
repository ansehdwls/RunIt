package com.zoku.running.util

import com.zoku.network.model.request.Pace
import com.zoku.network.model.response.PaceRecord

fun Pace.toPaceRecord() = PaceRecord(
    bpmList = bpm,
    durationList = pace
)