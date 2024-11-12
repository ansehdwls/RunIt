package com.zoku.runit.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.zoku.runit.navigation.WatchNavHost
import com.zoku.ui.model.PhoneWatchConnection


@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    sendBpm: (Int, Int, PhoneWatchConnection) -> Unit
) {
    WatchNavHost(navController, modifier) { bpm, duration, connection ->
        sendBpm(bpm, duration, connection)
    }
}


