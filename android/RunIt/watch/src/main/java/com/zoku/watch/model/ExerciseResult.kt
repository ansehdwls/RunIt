package com.zoku.watch.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.Duration


@Parcelize
data class ExerciseResult(
    val distance: Double?,
    val pace: Double?,
    val time: Long?,
    val bpm: Double?
) : Parcelable
