package com.zoku.navigatinon

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.zoku.home.ExpHistory
import com.zoku.home.HomeScreen
import com.zoku.home.RecordModeDetail
import com.zoku.home.RecordModeScreen
import com.zoku.home.RunHistoryScreen
import com.zoku.ui.model.PhoneWatchData
import com.zoku.util.ScreenDestinations


fun NavController.navigateToHome() = navigate(route = ScreenDestinations.home.route)
fun NavController.navigateToRunHistory() = navigate(route = ScreenDestinations.runHistory.route)
fun NavController.navigateToRecordModeScreen() =
    navigate(route = ScreenDestinations.RecordMode.route)

fun NavController.navigateToRecordModeDetail() =
    navigate(route = ScreenDestinations.RecordModeDetail.route)

fun NavController.navigateToExpHistory() = navigate(route = ScreenDestinations.expHistory.route)


fun NavGraphBuilder.homeScreen(
    moveToHistory: () -> Unit,
    moveToRecordMode: () -> Unit,
    moveToRunning: () -> Unit,
    moveToExpHistory: () -> Unit,
    phoneWatchData: PhoneWatchData
) {
    composable(route = ScreenDestinations.home.route) {
        HomeScreen(
            moveToHistory = moveToHistory,
            moveToRecordMode = moveToRecordMode,
            moveToRunning = moveToRunning,
            moveToExpHistory = moveToExpHistory,
            phoneWatchData = phoneWatchData
        )
    }
}

fun NavGraphBuilder.runHistory() {
    composable(route = ScreenDestinations.runHistory.route) {
        RunHistoryScreen()
    }
}

fun NavGraphBuilder.recordMode(moveToDetail: () -> Unit) {
    composable(route = ScreenDestinations.RecordMode.route) {
        RecordModeScreen(moveToDetail = moveToDetail)
    }
}

fun NavGraphBuilder.recordDetail() {
    composable(route = ScreenDestinations.RecordModeDetail.route) {
        RecordModeDetail()
    }
}

fun NavGraphBuilder.expHistory() {
    composable(route = ScreenDestinations.expHistory.route) {
        ExpHistory()
    }
}