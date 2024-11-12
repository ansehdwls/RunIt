package com.zoku.running

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zoku.running.model.RunningUIState
import com.zoku.ui.base.ClientDataViewModel
import com.zoku.ui.model.PhoneWatchConnection
import com.zoku.ui.model.RunningConnectionState
import timber.log.Timber


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun RunningScreen(
    modifier: Modifier = Modifier,
    onPauseWearableActivityClick: (String) -> Unit,
    onResumeWearableActivityClick: (String) -> Unit,
    onStopWearableActivityClick: (String) -> Unit,
    moveToHome: () -> Unit,
    phoneWatchData: PhoneWatchConnection,
    viewModel: ClientDataViewModel,
    runningViewModel: RunningViewModel = hiltViewModel(),
) {

    val currentCheck by viewModel.runningConnectionState.collectAsStateWithLifecycle()


    var isPlay by remember { mutableStateOf(true) }
    var isFirstPlay by remember { mutableStateOf(true) }
    var isResult by remember { mutableStateOf(false) }

    when (phoneWatchData) {
        PhoneWatchConnection.PAUSE_RUNNING -> {
            isPlay = false
        }

        PhoneWatchConnection.RESUME_RUNNING -> {
            isPlay = true
            isFirstPlay = false
        }

        PhoneWatchConnection.STOP_RUNNING -> {
            isResult = true
            moveToHome()
        }

        PhoneWatchConnection.SEND_BPM -> {
            isPlay = true
            isFirstPlay = false
        }

        else -> {}
    }
    HandleRunningState(
        runningViewModel = runningViewModel,
        moveToHome = moveToHome,
        onPauseWearableActivityClick = onPauseWearableActivityClick,
        onResumeWearableActivityClick = onResumeWearableActivityClick,
        onStopWearableActivityClick = onStopWearableActivityClick,
        currentCheck = currentCheck,
        isPlay = isPlay,
        isFirstPlay = isFirstPlay,
        isResult = isResult,
        onIsPlayChange = { isPlay = it },
        onIsFirstPlayChange = { isFirstPlay = it },
        onIsResultChange = { isResult = it }
    )
}


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun HandleRunningState(
    runningViewModel: RunningViewModel = hiltViewModel(),
    moveToHome: () -> Unit = {},
    onPauseWearableActivityClick: (String) -> Unit,
    onResumeWearableActivityClick: (String) -> Unit,
    onStopWearableActivityClick: (String) -> Unit,
    currentCheck: RunningConnectionState,
    isPlay: Boolean ,
    isFirstPlay: Boolean ,
    isResult: Boolean ,
    onIsPlayChange: (Boolean) -> Unit,
    onIsFirstPlayChange: (Boolean) -> Unit,
    onIsResultChange: (Boolean) -> Unit,
) {

    Timber.tag("RunningScreen").d("상태 확인 $isPlay $isFirstPlay $isResult")

    if (isResult) {
        RunningResultScreen(
            runningViewModel = runningViewModel,
            moveToHome = moveToHome
        )
    } else {
        if (isPlay) { // 재개된 상태
            RunningPlayScreen(
                onPauseClick = {
                    onPauseWearableActivityClick(PhoneWatchConnection.PAUSE_RUNNING.route)
                    onIsPlayChange(false)
                },
                isFirstPlay = isFirstPlay,
                runningViewModel = runningViewModel,
                connectionState = currentCheck
            )
        } else { // 중지 된 상태
            RunningPauseScreen(
                onPlayClick = {
                    onResumeWearableActivityClick(PhoneWatchConnection.RESUME_RUNNING.route)
                    onIsPlayChange(true)
                    onIsFirstPlayChange(false)

                },
                onStopLongPress = {
                    onStopWearableActivityClick(PhoneWatchConnection.STOP_RUNNING.route)
                    onIsResultChange(true)
                },
                runningViewModel = runningViewModel
            )
        }
    }
}