package com.zoku.network.model.response

import java.sql.Timestamp

data class ExpAllResponse(
    val data : List<ExpDataHistory>,
    val message: String
)

data class ExpDataHistory(
    val activity : String,
    val changed : Long,
    val createAt: Timestamp
)
