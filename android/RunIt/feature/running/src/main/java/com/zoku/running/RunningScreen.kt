package com.zoku.running

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.zoku.ui.model.PhoneWatchConnection


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun RunningScreen(
    modifier: Modifier = Modifier,
    onPauseWearableActivityClick: (String) -> Unit,
    onResumeWearableActivityClick: (String) -> Unit,
    onStopWearableActivityClick: (String) -> Unit
) {

    val runningViewModel = hiltViewModel<RunningViewModel>()
    var isPlay by remember { mutableStateOf(true) }
    var isFirstPlay by remember { mutableStateOf(true) }
    var isResult by remember { mutableStateOf(false) }

    if (isResult) {
        RunningResultScreen(runningViewModel = runningViewModel)
    } else {
        if (isPlay) {
            RunningPlayScreen(
                onPauseClick = {
                    onPauseWearableActivityClick(PhoneWatchConnection.PAUSE_RUNNING.route)
                    isPlay = false
                },
                isFirstPlay = isFirstPlay,
                runningViewModel = runningViewModel
            )
        } else {
            RunningPauseScreen(
                onPlayClick = {
                    onResumeWearableActivityClick(PhoneWatchConnection.RESUME_RUNNING.route)
                    isPlay = true
                    isFirstPlay = false
                },
                onStopLongPress = {
                    onStopWearableActivityClick(PhoneWatchConnection.STOP_RUNNING.route)
                    isResult = true
                },
                runningViewModel = runningViewModel
            )
        }
    }
}
