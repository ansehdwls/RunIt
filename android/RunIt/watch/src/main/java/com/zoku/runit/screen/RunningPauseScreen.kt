package com.zoku.runit.screen

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.health.services.client.data.ExerciseState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.tooling.preview.devices.WearDevices
import com.zoku.runit.component.button.LongClickStopButton
import com.zoku.runit.component.button.RunningButton
import com.zoku.runit.component.text.StatusText
import com.zoku.runit.model.ExerciseResult
import com.zoku.runit.navigation.WatchScreenDestination
import com.zoku.runit.util.formatBpm
import com.zoku.runit.util.formatDistanceKm
import com.zoku.runit.util.formatElapsedTime
import com.zoku.runit.util.formatPace
import com.zoku.runit.viewmodel.RunViewModel
import timber.log.Timber
import java.time.Duration

@Composable
fun RunningPauseScreen(
    modifier: Modifier = Modifier,
    runningData: ExerciseResult?,
    onStopClick: () -> Unit,
    onResumeClick: () -> Unit
) {
    val viewModel = hiltViewModel<RunViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Timber.tag("RunningPauseScreen").d("${uiState.exerciseState?.exerciseState}")
    Timber.tag("RunningPauseScreen").d("${runningData?.time}")


//    if (uiState.exerciseState?.exerciseState == ExerciseState.USER_RESUMING) {
//        onResumeClick()
//    } else if (uiState.exerciseState?.exerciseState == ExerciseState.ENDED) {
//        onStopClick()
//    }

    val scrollState = rememberScrollState()
    RunningPause(
        modifier, scrollState, runningData,
        onStopClick = {
            viewModel.stopRunning()
            onStopClick()
        },
        onResumeClick = {
            viewModel.resumeRunning()
            onResumeClick()
        }
    )

}


@Composable
fun RunningPause(
    modifier: Modifier = Modifier,
    scrollState: ScrollState,
    runningData: ExerciseResult?,
    onStopClick: () -> Unit,
    onResumeClick: () -> Unit,
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
            LongClickStopButton(
                onStop = {
                    onStopClick()
                },
                buttonSize = ButtonDefaults.LargeButtonSize
            )

            RunningButton(icon = Icons.Rounded.PlayArrow, size = ButtonDefaults.LargeButtonSize) {
                onResumeClick()
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            StatusText(
                buildString = formatDistanceKm(
                    runningData?.distance,
                    screenType = WatchScreenDestination.runningPause
                ),
                type = "km"
            )
            StatusText(buildString = formatPace(runningData?.pace), type = "페이스")
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Timber.tag("RunningPauseScreen").d("${runningData?.time}")

            StatusText(
                buildString = formatElapsedTime(
                    Duration.ofMillis(runningData?.time ?: 0L),
                    screenType = WatchScreenDestination.runningPause,
                    includeSeconds = true
                ), type = "시간"
            )
            StatusText(
                buildString = formatBpm(
                    runningData?.bpm,
                    screenType = WatchScreenDestination.runningPause
                ),
                type = "BPM"
            )
        }
        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Preview(device = WearDevices.LARGE_ROUND, showSystemUi = true, apiLevel = 33)
@Composable
fun RunningPausePreview() {
    RunningPauseScreen(
        runningData = null,
        onStopClick = {},
        onResumeClick = {}
    )

}