package com.zoku.running

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.zoku.network.model.response.RunRecordDetail
import com.zoku.ui.model.PhoneWatchConnection
import timber.log.Timber


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun RunningScreen(
    modifier: Modifier = Modifier,
    onPauseWearableActivityClick: (String) -> Unit,
    onResumeWearableActivityClick: (String) -> Unit,
    onStopWearableActivityClick: (String) -> Unit,
    moveToHome : () -> Unit,
    phoneWatchData: PhoneWatchConnection,
    runRecordDetail: RunRecordDetail
) {

    val runningViewModel = hiltViewModel<RunningViewModel>()
    var isPlay by remember { mutableStateOf(true) }
    var isFirstPlay by remember { mutableStateOf(true) }
    var isResult by remember { mutableStateOf(false) }

    runningViewModel.getPracticeRecord(runRecordDetail)

    Log.d("확인", "RunningScreen: $runRecordDetail")

    Timber.tag("RunningScreen").d("phoneWatchData $phoneWatchData")
    when(phoneWatchData){
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


    if (isResult) {
        RunningResultScreen(runningViewModel = runningViewModel,
            moveToHome = moveToHome)
    } else {
        if (isPlay) { // 재개된 상태
            RunningPlayScreen(
                onPauseClick = {
                    onPauseWearableActivityClick(PhoneWatchConnection.PAUSE_RUNNING.route)
                    isPlay = false
                },
                isFirstPlay = isFirstPlay,
                runningViewModel = runningViewModel
            )
        } else { // 중지 된 상태
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
