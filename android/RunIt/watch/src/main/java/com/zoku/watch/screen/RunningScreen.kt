package com.zoku.watch.screen


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.OutlinedButton
import androidx.wear.compose.material.Text
import androidx.wear.tooling.preview.devices.WearDevices
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.health.composables.ActiveDurationText
import com.zoku.ui.BaseYellow
import com.zoku.ui.CustomTypo
import com.zoku.watch.component.text.BpmText
import com.zoku.watch.component.text.RunningTimeText
import com.zoku.watch.model.ExerciseScreenState
import com.zoku.watch.util.formatDistanceKm
import com.zoku.watch.viewmodel.RunViewModel
import timber.log.Timber

@Composable
fun RunningScreen(
    modifier: Modifier = Modifier
) {
    val viewModel = hiltViewModel<RunViewModel>()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val scrollState = rememberScrollState()
    RunningStatus(uiState = uiState)
}


@OptIn(ExperimentalHorologistApi::class)
@Composable
fun RunningStatus(
    modifier: Modifier = Modifier,
    uiState: ExerciseScreenState
) {
    val lastActiveDurationCheckpoint = uiState.exerciseState?.activeDurationCheckpoint
    val exerciseState = uiState.exerciseState?.exerciseState
    val metrics = uiState.exerciseState?.exerciseMetrics
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
        Spacer(Modifier.height(15.dp))
        if (lastActiveDurationCheckpoint != null && exerciseState != null) {
            ActiveDurationText(
                checkpoint = lastActiveDurationCheckpoint,
                state = exerciseState
            ) {
                RunningTimeText(duration = lastActiveDurationCheckpoint.activeDuration)
            }
        }
        Spacer(Modifier.height(15.dp))
        BpmText(heartRate = metrics?.heartRate)


    }

}


@Composable
fun RunningButton(
    modifier: Modifier = Modifier,
    icon: ImageVector
) {
    OutlinedButton(
        modifier = Modifier.size(ButtonDefaults.LargeButtonSize),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Black,
            contentColor = BaseYellow
        ),
        border = ButtonDefaults.outlinedButtonBorder(
            borderColor = BaseYellow
        ),
        onClick = {}
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "button trigger",
            modifier = modifier
        )
    }
}


@Preview(device = WearDevices.LARGE_ROUND, showSystemUi = true, apiLevel = 33)
@Composable
fun RunningPreview() {
    val viewModel = hiltViewModel<RunViewModel>()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    RunningStatus(uiState = uiState)
}



