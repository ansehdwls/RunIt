package com.zoku.navigatinon

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.zoku.home.HomeScreen
import com.zoku.home.RecordModeScreen
import com.zoku.home.RunHistoryScreen
import com.zoku.util.ScreenDestinations


fun NavController.navigateToHome() = navigate(route = ScreenDestinations.home.route)
fun NavController.navigateToRunHistory() = navigate(route = ScreenDestinations.runHistory.route)
fun NavController.navigateToRecordModeScreen() = navigate(route = ScreenDestinations.RecordMode.route)

fun NavGraphBuilder.homeScreen( moveToHistory :()->Unit , moveToRecordMode : ()->Unit ) {
    composable(route = ScreenDestinations.home.route) {
        HomeScreen(moveToHistory = moveToHistory, moveToRecordMode = moveToRecordMode)
    }
}

fun NavGraphBuilder.runHistory() {
    composable(route = ScreenDestinations.runHistory.route) {
        RunHistoryScreen()
    }
}

fun NavGraphBuilder.recordMode(){
    composable(route = ScreenDestinations.RecordMode.route) {
        RecordModeScreen()
    }
}