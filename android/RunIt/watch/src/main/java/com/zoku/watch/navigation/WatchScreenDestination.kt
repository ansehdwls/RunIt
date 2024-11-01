package com.zoku.watch.navigation


sealed class WatchScreenDestination(
    open val route: String
) {
    data object home : WatchScreenDestination(route = "home")
    data object running : WatchScreenDestination(route = "running")
    data object runningPause : WatchScreenDestination(route = "running/pause")
}