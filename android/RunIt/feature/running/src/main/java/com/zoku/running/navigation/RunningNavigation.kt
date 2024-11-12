package com.zoku.running.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.zoku.running.RunningScreen
import com.zoku.ui.base.ClientDataViewModel
import com.zoku.ui.model.PhoneWatchConnection
import com.zoku.util.ScreenDestinations

fun NavController.navigateToRunning() = navigate(route = ScreenDestinations.running.route)

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun NavGraphBuilder.runningScreen(
    modifier: Modifier = Modifier,
    onPauseWearableActivityClick: (String) -> Unit,
    onResumeWearableActivityClick: (String) -> Unit,
    onStopWearableActivityClick: (String) -> Unit,
    moveToHome: () -> Unit,
    phoneWatchData: PhoneWatchConnection,
    viewModel: ClientDataViewModel
) {
    composable(route = ScreenDestinations.running.route) {
        RunningScreen(
            modifier,
            onPauseWearableActivityClick = onPauseWearableActivityClick,
            onResumeWearableActivityClick = onResumeWearableActivityClick,
            onStopWearableActivityClick = onStopWearableActivityClick,
            moveToHome = moveToHome,
            phoneWatchData = phoneWatchData,
            viewModel = viewModel
        )
    }
}
