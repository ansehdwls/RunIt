package com.zoku.network.model.response

data class LoginResponse(
    val data : LoginToken,
    val message: String
)

data class LoginToken(
    val accessToken: String,
    val refreshToken: String
)
