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


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun RunningScreen(
    modifier: Modifier = Modifier,
) {

    val runningViewModel = hiltViewModel<RunningViewModel>()
    var isPlay by remember { mutableStateOf(true) }
    var isFirstPlay by remember { mutableStateOf(true) }
    var isResult by remember { mutableStateOf(false) }

    if (isResult) {
        RunningResultScreen()
    } else {
        if (isPlay) {
            RunningPlayScreen(
                onPauseClick = { isPlay = false },
                isFirstPlay = isFirstPlay,
                runningViewModel = runningViewModel
            )
        } else {
            RunningPauseScreen(
                onPlayClick = {
                    isPlay = true
                    isFirstPlay = false
                },
                onStopLongPress = {
                    isResult = true
                },
                runningViewModel = runningViewModel
            )
        }
    }
}
