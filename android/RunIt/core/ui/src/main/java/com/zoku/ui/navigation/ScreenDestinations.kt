package com.zoku.util

import com.google.gson.Gson
import com.zoku.network.model.response.RunRecordDetail

sealed class ScreenDestinations(
    open val route: String
) {
    data object login : ScreenDestinations(route = "login")
    data object home : ScreenDestinations(route = "home")
    data object running : ScreenDestinations(route = "running/{recordDto}"){
        fun createRoute(recordDetail: RunRecordDetail): String {
            val gson = Gson()
            val recordDtoJson = gson.toJson(recordDetail)
            return "running/$recordDtoJson"
        }
    }
    data object runHistory : ScreenDestinations(route = "home/run-history")
    data object RecordMode : ScreenDestinations(route = "home/record-mode")
    data object RecordModeDetail : ScreenDestinations(route = "home/record-mode/detail/{recordId}") {
        fun createRoute(recordId: Int) = "home/record-mode/detail/$recordId"
    }
    data object runningPause: ScreenDestinations(route = "pause")
    data object runningResult: ScreenDestinations(route = "running-result")
    data object expHistory: ScreenDestinations(route = "home/rank/exp-history")
}