package com.zoku.watch.screen


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Text
import androidx.wear.tooling.preview.devices.WearDevices
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.health.composables.ActiveDurationText
import com.zoku.ui.BaseYellow
import com.zoku.ui.CustomTypo
import com.zoku.watch.component.button.RunningButton
import com.zoku.watch.component.text.BpmText
import com.zoku.watch.component.text.PaceText
import com.zoku.watch.component.text.RunningTimeText
import com.zoku.watch.model.ExerciseResult
import com.zoku.watch.model.ExerciseScreenState
import com.zoku.watch.model.toExerciseResult
import com.zoku.watch.util.formatDistanceKm
import com.zoku.watch.viewmodel.RunViewModel
import timber.log.Timber
import java.time.Duration

@Composable
fun RunningScreen(
    modifier: Modifier = Modifier,
    onPauseClick: (ExerciseResult) -> Unit
) {
    val viewModel = hiltViewModel<RunViewModel>()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    RunningStatus(uiState = uiState) { state, duration ->
        Timber.tag("RunningScreen RunningStatus").d("$duration")
        viewModel.pauseRunning()
        onPauseClick(state.toExerciseResult(duration))
    }
}


@OptIn(ExperimentalHorologistApi::class)
@Composable
fun RunningStatus(
    modifier: Modifier = Modifier,
    uiState: ExerciseScreenState,
    onPauseClick: (ExerciseScreenState, Duration?) -> Unit
) {
    val lastActiveDurationCheckpoint = uiState.exerciseState?.activeDurationCheckpoint
    val exerciseState = uiState.exerciseState?.exerciseState
    val metrics = uiState.exerciseState?.exerciseMetrics
    var duration : Duration? = null
    Timber.tag("runningStatus").d("$lastActiveDurationCheckpoint")
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(35.dp))
        Text(
            modifier = Modifier,
            style = CustomTypo().jalnan.copy(fontSize = 35.sp),
            text = formatDistanceKm(metrics?.distance),
            color = BaseYellow
        )
        Spacer(Modifier.height(10.dp))
        if (lastActiveDurationCheckpoint != null && exerciseState != null) {
            ActiveDurationText(
                checkpoint = lastActiveDurationCheckpoint,
                state = exerciseState
            ) {
                duration = it
                Timber.tag("RunningScreen").d("duration $it")
                RunningTimeText(duration = it)
            }
        }
        Spacer(Modifier.height(10.dp))
        BpmText(heartRate = metrics?.heartRate)
        Spacer(Modifier.height(10.dp))
        PaceText(pace = metrics?.pace)
        Spacer(Modifier.height(10.dp))
        RunningButton(icon = Icons.Rounded.Pause,
            size = ButtonDefaults.ExtraSmallButtonSize,
            clickEvent = {
                Timber.tag("RunningScreen Stop").d("$duration")
                onPauseClick(uiState, duration) }
        )
    }

}


@Preview(device = WearDevices.LARGE_ROUND, showSystemUi = true, apiLevel = 33)
@Composable
fun RunningPreview() {
    val viewModel = hiltViewModel<RunViewModel>()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

}



