package com.zoku.network.model.response

data class GroupResponse(
    val data : List<GroupMember>,
    val message: String
)

data class GroupMember(
    val userName: String,
    val imageUrl: String,
    val exp: Long
)