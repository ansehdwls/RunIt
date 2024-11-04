package com.zoku.watch.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.wear.compose.material.MaterialTheme
import com.zoku.watch.navigation.WatchScreenDestination
import java.time.Duration
import java.util.concurrent.TimeUnit

private val MINUTES_PER_HOUR = TimeUnit.HOURS.toMinutes(1)
private val SECONDS_PER_MINUTE = TimeUnit.MINUTES.toSeconds(1)




@Composable
fun formatElapsedTime(
    elapsedDuration: Duration?,
    includeSeconds: Boolean = false,
    screenType: WatchScreenDestination = WatchScreenDestination.running
) = buildAnnotatedString {
    if (elapsedDuration == null) {
        append("--")
    } else {
        val hours = elapsedDuration.toHours()


        if (hours > 0) {
            append(hours.toString())
            withStyle(style = MaterialTheme.typography.caption3.toSpanStyle()) {
                val hour = if (screenType == WatchScreenDestination.running) {
                    "시간"
                } else {
                    ":"
                }
                append(hour)
            }
        }
        val minutes = elapsedDuration.toMinutes() % MINUTES_PER_HOUR
        append("%02d".format(minutes))
        withStyle(style = MaterialTheme.typography.caption3.toSpanStyle()) {
            val minute = if (screenType == WatchScreenDestination.running) {
                "분"
            } else {
                ": "
            }
            append(minute)
        }
        if (includeSeconds) {
            val seconds = elapsedDuration.seconds % SECONDS_PER_MINUTE
            append("%02d".format(seconds))
            withStyle(style = MaterialTheme.typography.caption3.toSpanStyle()) {
                val second = if (screenType == WatchScreenDestination.running) {
                    "초"
                } else {
                    ""
                }
                append(second)
            }
        }
    }
}


@Composable
fun formatDistanceKm(
    meters: Double?,
    screenType: WatchScreenDestination = WatchScreenDestination.running
) = buildAnnotatedString {
    if (meters == null) {
        append("--")
    } else {
        append("%02.2f".format(meters / 1_000))
        if (screenType == WatchScreenDestination.running) {
            withStyle(style = MaterialTheme.typography.caption3.toSpanStyle()) {
                append("KM")
            }
        }

    }
}

@Composable
fun formatBpm(
    heartRate: Double?,
    screenType: WatchScreenDestination = WatchScreenDestination.running
) = buildAnnotatedString {
    if (heartRate == null) {
        append("--")
    } else {
        append("%.0f".format(heartRate))
        if (screenType == WatchScreenDestination.running) {
            withStyle(style = MaterialTheme.typography.caption3.toSpanStyle()) {
                append("BPM")
            }
        }

    }

}


@Composable
fun formatPace(
    pace: Double?,
) = buildAnnotatedString {
    if (pace == null || pace.isNaN()) {
        append("--")
    } else {
        val secondsPerKm = pace / 1000.0
        val minutes = (secondsPerKm / 60).toInt()
        val seconds = (secondsPerKm % 60).toInt()

        append("%02d".format(minutes))
        append("'")
        append("%02d".format(seconds))
        append('"')
    }
}