package com.zoku.running

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.zoku.running.service.LocationService
import com.zoku.running.util.formatTime
import com.zoku.running.util.meterToKilo
import com.zoku.running.util.timeToPace
import com.zoku.ui.BaseGrayBackground
import com.zoku.ui.BaseYellow
import com.zoku.ui.RoundButtonGray
import com.zoku.ui.componenet.RobotoText
import com.zoku.ui.componenet.RoundRunButton
import com.zoku.ui.model.RunningConnectionState
import com.zoku.ui.theme.BaseGrayBackground
import com.zoku.ui.theme.BaseYellow
import com.zoku.ui.theme.RoundButtonGray

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RunningPlayScreen(
    onPauseClick: () -> Unit,
    isFirstPlay: Boolean = true,
    runningViewModel: RunningViewModel,
    connectionState: RunningConnectionState,
) {
    val locationPermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.FOREGROUND_SERVICE,
            Manifest.permission.FOREGROUND_SERVICE_LOCATION
        )
    )
    LaunchedEffect(locationPermissionsState.allPermissionsGranted) {
        if (locationPermissionsState.allPermissionsGranted) {
            runningViewModel.startTimer()
            runningViewModel.startLocationService()
        } else {
            locationPermissionsState.launchMultiplePermissionRequest()
        }
    }
    val uiState by runningViewModel.uiState.collectAsState()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BaseGrayBackground),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(0.2f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val bpmTimePair = if (connectionState is RunningConnectionState.ConnectionSuccess) {
                connectionState.data.bpm?.let { runningViewModel.addBpm(it) }
                Pair(connectionState.data.bpm, connectionState.data.time)
            } else {
                Pair(uiState.bpm, uiState.time)
            }


            TopInfoWithText(
                topName = "${timeToPace(uiState.face)}",
                bottomName = "페이스"
            )
            TopInfoWithText(
                topName = "${bpmTimePair.first}",
                bottomName = "BPM"
            )
            TopInfoWithText(
                topName = formatTime(seconds = bpmTimePair.second ?: 0),
                bottomName = "시간"
            )
        }

        Spacer(modifier = Modifier.weight(0.35f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ) {
            RobotoText(
                text = meterToKilo(uiState.distance.toInt()),
                fontSize = 80.sp,
                color = BaseYellow,
                style = "Bold"
            )

            Spacer(modifier = Modifier.width(10.dp))


            RobotoText(
                text = "km",
                fontSize = 30.sp,
                modifier = Modifier.offset(y = (-10).dp)
            )
        }

        Spacer(modifier = Modifier.weight(0.15f))

        Spacer(modifier = Modifier.weight(0.2f))

        if (isFirstPlay) {
            RoundRunButton(
                containerColor = BaseYellow,
                resourceId = R.drawable.baseline_pause_24,
                resourceColor = Color.Black,
                onClick = {
                    runningViewModel.stopLocationService()
                    runningViewModel.stopTimer()
                    onPauseClick()
                }
            )
        } else {
            GatherButtonBox(
                onPauseClick = onPauseClick,
                runningViewModel = runningViewModel
            )
        }

        Spacer(modifier = Modifier.weight(0.12f))
    }
}

@Composable
fun GatherButtonBox(onPauseClick: () -> Unit, runningViewModel: RunningViewModel) {
    var spread by remember { mutableStateOf(true) }

    val offsetValue by animateDpAsState(
        targetValue = if (spread) 72.dp else 0.dp,
        animationSpec = tween(durationMillis = 500)
    )

    LaunchedEffect(Unit) {
        spread = false
    }

    Box(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        RoundRunButton(
            containerColor = RoundButtonGray,
            resourceId = R.drawable.baseline_stop_24,
            resourceColor = Color.White,
            modifier = Modifier
                .align(Alignment.Center)
                .offset(x = -offsetValue),
            onClick = { onPauseClick() }
        )

        RoundRunButton(
            containerColor = BaseYellow,
            resourceId = R.drawable.baseline_pause_24,
            resourceColor = Color.Black,
            modifier = Modifier
                .align(Alignment.Center)
                .offset(x = offsetValue),
            onClick = {
                runningViewModel.stopLocationService()
                runningViewModel.stopTimer()
                onPauseClick()
            })
    }
}

@Composable
fun TopInfoWithText(topName: String, bottomName: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        RobotoText(
            text = topName,
            color = BaseYellow,
            fontSize = 20.sp,
        )
        RobotoText(
            text = bottomName,
            color = Color.White,
            fontSize = 20.sp,
        )
    }
}

fun startLocationService(context: Context) {
    val intent = Intent(context, LocationService::class.java)
    context.startForegroundService(intent)
}

fun stopLocationService(context: Context) {
    val intent = Intent(context, LocationService::class.java)
    context.stopService(intent)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    com.zoku.ui.theme.RunItTheme {
        RunningPlayScreen(
            onPauseClick = { Log.d("Zz", "Zzz") },
            runningViewModel = hiltViewModel<RunningViewModel>(),
            connectionState = RunningConnectionState.ConnectionDefault
        )
    }
}

