package com.zoku.watch.screen

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zoku.watch.component.text.StatusText
import com.zoku.watch.model.ExerciseScreenState

@Composable
fun RunningPauseScreen(
    modifier: Modifier = Modifier
) {

}


@Composable
fun RunningPause(
    modifier: Modifier = Modifier,
    scrollState: ScrollState,
    uiState: ExerciseScreenState
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            RunningButton(icon = Icons.Rounded.Stop)

            RunningButton(icon = Icons.Rounded.PlayArrow)
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            StatusText(value = "6.39", type = "km")
            StatusText(value = "5`28", type = "페이스")
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            StatusText(value = "6:30", type = "시간")
            StatusText(
                value = uiState.exerciseState?.exerciseMetrics?.heartRate.toString(),
                type = "BPM"
            )
        }
        Spacer(modifier = Modifier.height(100.dp))
    }
}