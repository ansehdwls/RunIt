package com.zoku.data.model

data class LoginData(
    var userId : String,
    var nickName : String,
    var image : String,
    var isLogin : Boolean = false
)