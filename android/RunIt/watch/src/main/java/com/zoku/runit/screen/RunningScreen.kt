package com.zoku.runit.screen


import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.health.services.client.data.ExerciseState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Text
import androidx.wear.tooling.preview.devices.WearDevices
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.health.composables.ActiveDurationText
import com.zoku.runit.component.button.RunningButton
import com.zoku.runit.component.text.BpmText
import com.zoku.runit.component.text.PaceText
import com.zoku.runit.component.text.RunningTimeText
import com.zoku.runit.model.ExerciseResult
import com.zoku.runit.model.ExerciseScreenState
import com.zoku.runit.model.toExerciseResult
import com.zoku.runit.util.formatDistanceKm
import com.zoku.runit.viewmodel.RunViewModel
import com.zoku.ui.model.PhoneWatchConnection
import com.zoku.ui.theme.BaseYellow
import com.zoku.ui.theme.CustomTypo
import kotlinx.coroutines.delay
import timber.log.Timber
import java.time.Duration

@Composable
fun RunningScreen(
    modifier: Modifier = Modifier,
    onPauseClick: (ExerciseResult) -> Unit,
    sendBpm: (Int, Int, Double, PhoneWatchConnection) -> Unit
) {
    val viewModel = hiltViewModel<RunViewModel>()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Timber.tag("RunningScreen RunningStatus").d("운동 State ${uiState.exerciseState}")

    RunningStatus(uiState = uiState, onPauseClick = { state, duration ->
        Timber.tag("RunningScreen RunningStatus").d("$duration")
        viewModel.pauseRunning()
        onPauseClick(state.toExerciseResult(duration))
    },
        sendBpm = { bpm, duration, distance, connection ->
            sendBpm(bpm, duration, distance, connection)
        }
    )

    BackHandler {}
}


@OptIn(ExperimentalHorologistApi::class)
@Composable
fun RunningStatus(
    modifier: Modifier = Modifier,
    uiState: ExerciseScreenState,
    onPauseClick: (ExerciseScreenState, Duration?) -> Unit,
    sendBpm: (Int, Int, Double, PhoneWatchConnection) -> Unit
) {
    val lastActiveDurationCheckpoint = uiState.exerciseState?.activeDurationCheckpoint
    val exerciseState = uiState.exerciseState?.exerciseState
    val metrics = uiState.exerciseState?.exerciseMetrics
    var duration by remember { mutableStateOf<Duration?>(null) }
    var flag by remember { mutableStateOf(false) }

    Timber.tag("RunningStatus").d("exerciseState ${exerciseState}")
    Timber.tag("RunningStatus").d("duration 맨 윗 부분 $duration")
    if (exerciseState == ExerciseState.USER_PAUSING && !flag) {
        flag = true
        onPauseClick(uiState, duration)
    } else if (exerciseState == ExerciseState.ACTIVE) {
        LaunchedEffect(metrics?.heartRate, metrics?.distance) {
            while (true) {
                delay(1000L)
                sendBpm(
                    metrics?.heartRate?.toInt() ?: 0,
                    duration?.seconds?.toInt() ?: 0,
                    metrics?.distance ?: 0.0,
                    PhoneWatchConnection.SEND_BPM
                )
            }
        }
    }




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
                onPauseClick(uiState, duration)
            }
        )
    }

}


@Preview(device = WearDevices.LARGE_ROUND, showSystemUi = true, apiLevel = 33)
@Composable
fun RunningPreview() {
    val viewModel = hiltViewModel<RunViewModel>()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

}



