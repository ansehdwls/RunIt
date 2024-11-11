package com.zoku.ui.model

data class PhoneWatchData(
    val bpm: List<Int>,
    val sendType: PhoneWatchConnection?
) {
    companion object {
        val DEFAULT = PhoneWatchData(
            bpm = emptyList(),
            sendType = PhoneWatchConnection.EMPTY
        )
    }
}
