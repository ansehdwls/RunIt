package com.zoku.running.model

sealed interface RunningEventState {

    data object RunningEmpty : RunningEventState
    data class RunningFailToast(val message : String) : RunningEventState

}