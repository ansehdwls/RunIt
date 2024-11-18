package com.zoku.ui.model

import androidx.compose.runtime.Immutable


@Immutable
data class WatchToPhoneData(
    val bpm: Int?,
    val time: Int?,
    val distance : Double?
) {
    companion object {
        val DEFAULT = WatchToPhoneData(
            bpm = null,
            time = null,
            distance = null
        )
    }
}
