package com.zoku.network.model.response

data class PostRunningRecordResponse(
    val data: ExpWithAttend,
    val message: String,
)

data class ExpWithAttend(
    val isAttend: Boolean,
    val exp: Int
)