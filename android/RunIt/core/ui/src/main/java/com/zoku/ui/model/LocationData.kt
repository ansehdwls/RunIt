package com.zoku.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LocationData(
    val latitude: Double,
    val longitude: Double,
) : Parcelable
