package com.zoku.util

sealed class ScreenDestinations(
    open val route: String
) {
    data object home : ScreenDestinations(route = "home")


}