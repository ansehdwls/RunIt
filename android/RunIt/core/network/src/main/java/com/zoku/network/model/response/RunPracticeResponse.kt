package com.zoku.network.model.response

data class RunPracticeResponse(
    val data: List<RunPractice>,
    val message: String,
)

data class RunPractice(
    val id: Int,
    val distance: Double,
    val bpm: Int,
    val startTime: String,
    val endTime: String,
    val name: String,
    val imageUrl: String,
    val avgPace: Double,
)
