package com.zoku.network.model.response

data class UserResponse(
    val data : UserInfo,
    val message: String
)

data class UserInfo(
    val userId : Long,
    val userName : String,
    val userNumber : String,
    val imageUrl : String,
    val groupId: Long,
)
