package com.zoku.running.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.zoku.running.RunningPauseScreen
import com.zoku.running.RunningResultScreen
import com.zoku.running.RunningScreen
import com.zoku.util.ScreenDestinations

fun NavController.navigateToRunning() = navigate(route = ScreenDestinations.running.route)

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun NavGraphBuilder.runningScreen(modifier: Modifier = Modifier, onStartWearableActivityClick : () -> Unit) {
    composable(route = ScreenDestinations.running.route) {
        RunningScreen(modifier, onStartWearableActivityClick)
    }
}
