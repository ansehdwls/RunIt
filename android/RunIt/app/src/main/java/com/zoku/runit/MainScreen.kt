package com.zoku.runit

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.zoku.runit.navigation.RunItMainNavHost
import com.zoku.ui.base.ClientDataViewModel
import com.zoku.ui.model.PhoneWatchConnection

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    onHomeScreen : () -> Unit,
    onStartWearableActivityClick: (String) -> Unit,
    onPauseWearableActivityClick: (String) -> Unit,
    onResumeWearableActivityClick: (String) -> Unit,
    onStopWearableActivityClick: (String) -> Unit,
    viewModel : ClientDataViewModel,
    sendHeartBeat : () -> Unit
) {
    val navController = rememberNavController()
    RunItMainNavHost(
        modifier = modifier,
        navController = navController,
        onHomeScreen = onHomeScreen,
        onStartWearableActivityClick = onStartWearableActivityClick,
        onPauseWearableActivityClick = onPauseWearableActivityClick,
        onResumeWearableActivityClick = onResumeWearableActivityClick,
        onStopWearableActivityClick = onStopWearableActivityClick,
        viewModel = viewModel,
        sendHeartBeat = sendHeartBeat
    )
}