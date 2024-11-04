package com.zoku.network.model.response

data class TestApiResponse(
    val postId: Int,
    val id: Int,
    val name: String,
    val email: String,
    val body: String,
)
