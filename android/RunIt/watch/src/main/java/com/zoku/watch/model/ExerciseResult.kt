package com.zoku.watch.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class ExerciseResult(
    val distance: Double?,
    val pace: Double?,
    val time: Double?,
    val bpm: Double?
) : Parcelable
