package com.zoku.ui.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Immutable
sealed interface RunningConnectionState {

    @Stable
    data object ConnectionDefault : RunningConnectionState
    @Stable
    data class ConnectionSuccess(val data: WatchToPhoneData) : RunningConnectionState
    @Stable
    data object ConnectionFail : RunningConnectionState

}