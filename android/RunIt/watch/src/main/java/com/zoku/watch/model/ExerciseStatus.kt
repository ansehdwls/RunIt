package com.zoku.watch.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class ExerciseStatus(
    val distance : Double?,
    val pace : Double?,
    val duration : Double?,
    val bpm : Double?
) : Parcelable
