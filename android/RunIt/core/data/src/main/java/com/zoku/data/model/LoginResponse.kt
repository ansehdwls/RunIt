package com.zoku.data.model

data class LoginResponse(
    var userId : String,
    var nickName : String,
    var image : String,
    var isLogin : Boolean = false
)