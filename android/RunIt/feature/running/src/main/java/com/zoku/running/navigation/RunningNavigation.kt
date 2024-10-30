package com.zoku.running.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.zoku.running.RunningPauseScreen
import com.zoku.running.RunningResultScreen
import com.zoku.running.RunningScreen
import com.zoku.util.ScreenDestinations

fun NavController.navigateToRunning() = navigate(route = ScreenDestinations.running.route)

fun NavController.navigateToRunningResult() = navigate(route = ScreenDestinations.runningResult.route)

fun NavGraphBuilder.runningScreen(modifier: Modifier = Modifier) {
    composable(route = ScreenDestinations.running.route) {
        RunningScreen(modifier)
    }
}

fun NavGraphBuilder.runningResultScreen(modifier: Modifier = Modifier){
    composable(route = ScreenDestinations.runningResult.route){
        RunningResultScreen()
    }
}