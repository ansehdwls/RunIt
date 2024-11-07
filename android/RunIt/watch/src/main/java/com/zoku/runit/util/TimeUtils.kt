package com.zoku.runit.util

import com.google.android.horologist.health.composables.ActiveDurationDefaults
import java.time.Duration

fun Duration.toActiveTime(): String {
    val type = ActiveDurationDefaults.HH_MM_SS
    val activeSeconds = this.toMillis() / 1000
    val hours = activeSeconds / 3600
    val minutes = (activeSeconds % 3600) / 60
    val seconds = activeSeconds % 60
    return type.format(hours, minutes, seconds)
}