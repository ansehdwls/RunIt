package com.zoku.running.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun formatTime(seconds: Int): String {
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val secs = seconds % 60

    return if (hours > 0) {
        String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, secs)
    } else {
        String.format(Locale.getDefault(), "%02d:%02d", minutes, secs)
    }
}

fun getIso8601TimeString(timeMillis: Long): String {
    val date = Date(timeMillis)
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",Locale.getDefault())
    dateFormat.timeZone = TimeZone.getTimeZone("Asia/Seoul")
    return dateFormat.format(date)
}

fun timeToFace(second: Int):String{
    return "${second / 60}'${String.format("%02d", second % 60)}"
}