package com.zoku.network.model.response

data class ExpAllResponse(
    val data: List<ExpDataHistory>,
    val message: String,
)

data class ExpWeekResponse(
    val data: Long,
    val message: String,
)

data class ExpDataHistory(
    val activity: String,
    val changed: Long,
    val createAt: String,
)
