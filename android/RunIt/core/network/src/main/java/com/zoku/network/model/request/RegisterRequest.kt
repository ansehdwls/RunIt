package com.zoku.network.model.request

data class RegisterRequest(
    val userName : String,
    val userImageUrl : String,
    val userNumber : String
)