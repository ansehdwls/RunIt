package com.zoku.running.model

data class RunningUIState(
    val time: Int,
    val distance: Int,
    val face: Int,
    val bpm: Int,
    val heartBeats: List<Int>? = null
)
