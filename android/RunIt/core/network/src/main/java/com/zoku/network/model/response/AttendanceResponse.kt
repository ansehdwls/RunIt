package com.zoku.network.model.response

data class AttendanceResponse(
    val data : List<AttendanceDay>,
    val message: String
)

data class AttendanceDay(
    val day : String,
    val attended : Boolean,
    val date: String
)
