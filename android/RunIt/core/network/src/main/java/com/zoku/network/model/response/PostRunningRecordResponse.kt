package com.zoku.network.model.response

data class PostRunningRecordResponse(
    val data: ExpWithAttend,
    val message: String,
)

data class ExpWithAttend(
    val id: Int,
    val isAttend: Boolean,
    val exp: Int
)