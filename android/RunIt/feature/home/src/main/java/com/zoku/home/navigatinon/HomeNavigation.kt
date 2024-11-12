package com.zoku.navigatinon

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.zoku.home.ExpHistory
import com.zoku.home.HomeScreen
import com.zoku.home.RecordModeDetail
import com.zoku.home.RecordModeScreen
import com.zoku.home.RunHistoryScreen
import com.zoku.network.model.response.RunRecordDetail
import com.zoku.ui.model.PhoneWatchConnection
import com.zoku.ui.model.PhoneWatchData
import com.zoku.util.ScreenDestinations


fun NavController.navigateToHome() = navigate(route = ScreenDestinations.home.route)
fun NavController.navigateToRunHistory() = navigate(route = ScreenDestinations.runHistory.route)
fun NavController.navigateToRecordModeScreen() =
    navigate(route = ScreenDestinations.RecordMode.route)

fun NavController.navigateToRecordModeDetail(recordId :Int) =
    navigate(route = ScreenDestinations.RecordModeDetail.createRoute(recordId))

fun NavController.navigateToExpHistory() = navigate(route = ScreenDestinations.expHistory.route)


fun NavGraphBuilder.homeScreen(
    moveToHistory: () -> Unit,
    moveToRecordMode: () -> Unit,
    moveToRunning: () -> Unit,
    moveToExpHistory: () -> Unit,
    phoneWatchData: PhoneWatchConnection
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

fun NavGraphBuilder.recordMode(moveToDetail: (Int) -> Unit) {
    composable(route = ScreenDestinations.RecordMode.route) {
        RecordModeScreen(moveToDetail = moveToDetail)
    }
}

fun NavGraphBuilder.recordDetail(
    moveToPractice : () -> Unit,
    moveToRunning: (runRecordDto : RunRecordDetail) -> Unit
) {
    composable(route = ScreenDestinations.RecordModeDetail.route,
        arguments = listOf(navArgument("recordId") {  type = NavType.IntType })) {
            backStackEntry ->
        val recordId = backStackEntry.arguments?.getInt("recordId") ?: 0

        RecordModeDetail(recordId = recordId,
            moveToPractice = moveToPractice,
            moveToRunning = moveToRunning
        )
    }
}

fun NavGraphBuilder.expHistory() {
    composable(route = ScreenDestinations.expHistory.route) {
        ExpHistory()
    }
}