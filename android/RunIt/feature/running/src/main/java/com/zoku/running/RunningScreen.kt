package com.zoku.running

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier


@Composable
fun RunningScreen(modifier: Modifier = Modifier) {

    var isPlay by remember { mutableStateOf(true) }
    var isFirstPlay by remember { mutableStateOf(true) }

    if (isPlay) {
        RunningPlayScreen(onPauseClick = { isPlay = false }, isFirstPlay = isFirstPlay)
    } else {
        RunningPauseScreen(onPlayClick = {
            isPlay = true
            isFirstPlay = false
        })
    }
}
