package com.zoku.running.util

import java.util.Locale

fun formatTime(seconds: Int): String {
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val secs = seconds % 60

    return if (hours > 0) {
        String.format(Locale.getDefault(),"%02d:%02d:%02d", hours, minutes, secs)
    } else {
        String.format(Locale.getDefault(),"%02d:%02d", minutes, secs)
    }
}