package com.zoku.util

sealed class ScreenDestinations(
    open val route: String
) {
    data object login : ScreenDestinations(route = "login")
    data object home : ScreenDestinations(route = "home")
    data object runHistory : ScreenDestinations(route = "home/run-history")
    data object RecordMode : ScreenDestinations(route = "home/record-mode")
}