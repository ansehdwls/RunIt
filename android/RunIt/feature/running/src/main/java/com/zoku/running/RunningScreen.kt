package com.zoku.running

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier


@Composable
fun RunningScreen(modifier: Modifier = Modifier) {

    var isPlay by remember { mutableStateOf(true) }

    Crossfade(targetState = isPlay, label = "") { isPlaying ->
        if (isPlaying) {
            RunningPlayScreen(onPauseClick = { isPlay = false })
        } else {
            RunningPauseScreen(onPlayClick = { isPlay = true })
        }
    }
}
