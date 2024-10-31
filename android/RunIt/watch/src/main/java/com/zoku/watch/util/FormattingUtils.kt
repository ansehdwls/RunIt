package com.zoku.watch.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.wear.compose.material.MaterialTheme
import java.time.Duration
import java.util.concurrent.TimeUnit

private val MINUTES_PER_HOUR = TimeUnit.HOURS.toMinutes(1)
private val SECONDS_PER_MINUTE = TimeUnit.MINUTES.toSeconds(1)


@Composable
fun formatElapsedTime(
    elapsedDuration: Duration?,
    includeSeconds: Boolean = false
) = buildAnnotatedString {
    if (elapsedDuration == null) {
        append("--")
    } else {
        val hours = elapsedDuration.toHours()
        if (hours > 0) {
            append(hours.toString())
            withStyle(style = MaterialTheme.typography.caption3.toSpanStyle()) {
                append("시간")
            }
        }
        val minutes = elapsedDuration.toMinutes() % MINUTES_PER_HOUR
        append("%02d".format(minutes))
        withStyle(style = MaterialTheme.typography.caption3.toSpanStyle()) {
            append(" 분 ")
        }
        if (includeSeconds) {
            val seconds = elapsedDuration.seconds % SECONDS_PER_MINUTE
            append("%02d".format(seconds))
            withStyle(style = MaterialTheme.typography.caption3.toSpanStyle()) {
                append(" 초 ")
            }
        }
    }
}


@Composable
fun formatDistanceKm(meters: Double?) = buildAnnotatedString {
    if (meters == null) {
        append("--")
    } else {
        append("%02.2f".format(meters / 1_000))
        withStyle(style = MaterialTheme.typography.caption3.toSpanStyle()) {
            append("KM")
        }
    }
}

@Composable
fun formatBpm(heartRate: Double?) = buildAnnotatedString {
    if (heartRate == null) {
        append("--")
    } else {
        append("%.0f".format(heartRate))
        withStyle(style = MaterialTheme.typography.caption3.toSpanStyle()) {
            append("BPM")
        }
    }

}