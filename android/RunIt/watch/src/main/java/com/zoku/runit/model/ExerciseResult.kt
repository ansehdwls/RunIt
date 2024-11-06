package com.zoku.runit.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class ExerciseResult(
    val distance: Double?,
    val pace: Double?,
    val time: Long?,
    val bpm: Double?
) : Parcelable
