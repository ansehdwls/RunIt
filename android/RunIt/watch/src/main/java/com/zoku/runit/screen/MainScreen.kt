package com.zoku.runit.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.zoku.runit.navigation.WatchNavHost
import com.zoku.runit.viewmodel.MainViewModel
import com.zoku.ui.model.PhoneWatchConnection


@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    mainViewModel : MainViewModel,
    sendBpm: (Int, Int, Double, PhoneWatchConnection) -> Unit
) {
    WatchNavHost(navController, modifier, mainViewModel = mainViewModel) { bpm, duration, distance, connection ->
        sendBpm(bpm, duration, distance,connection)
    }
}


