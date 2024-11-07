package com.zoku.runit

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.zoku.runit.navigation.RunItMainNavHost

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    onStartWearableActivityClick: (String) -> Unit,
    onPauseWearableActivityClick: (String) -> Unit,
    onResumeWearableActivityClick: (String) -> Unit,
    onStopWearableActivityClick: (String) -> Unit,
) {
    val navController = rememberNavController()
    RunItMainNavHost(
        modifier = modifier,
        navController = navController,
        onStartWearableActivityClick = onStartWearableActivityClick,
        onPauseWearableActivityClick = onPauseWearableActivityClick,
        onResumeWearableActivityClick = onResumeWearableActivityClick,
        onStopWearableActivityClick = onStopWearableActivityClick
    )
}