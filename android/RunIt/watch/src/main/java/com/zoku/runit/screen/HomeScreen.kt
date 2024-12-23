package com.zoku.runit.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
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
import androidx.health.services.client.data.ExerciseState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.material.Text
import androidx.wear.tooling.preview.devices.WearDevices
import com.zoku.runit.component.button.StartButton
import com.zoku.runit.viewmodel.HomeViewModel
import com.zoku.runit.viewmodel.MainViewModel
import com.zoku.runit.viewmodel.RunViewModel
import com.zoku.ui.theme.CustomTypo
import timber.log.Timber

@Composable
fun HomeScreen(
    modifier: Modifier,
    mainViewModel: MainViewModel,
    onStartClick: () -> Unit
) {
    val homeViewModel = hiltViewModel<HomeViewModel>()
    val runningViewModel = hiltViewModel<RunViewModel>()
    val isPhoneActive by mainViewModel.isPhoneActive.collectAsStateWithLifecycle()

    val uiState by runningViewModel.uiState.collectAsStateWithLifecycle()
    var flag by remember { mutableStateOf(false) }
    Timber.tag("HomeScreen").d("ExerciseState $flag , ${ExerciseState.ACTIVE}")
    if (uiState.exerciseState?.exerciseState == ExerciseState.USER_STARTING && !flag) {
        Timber.tag("HomeScreen").d("ExerciseState ${ExerciseState.ACTIVE}")
        flag = true
        onStartClick()
        homeViewModel.startRunning()
    }
    Timber.tag("HomeScreen").d("isPhoneActive $isPhoneActive")
    StartButton(modifier) {
        if (isPhoneActive) {
            onStartClick()
            homeViewModel.startRunning()
        }
    }
}


@Composable
fun UserInfoRow(modifier: Modifier) {
    Column(
        modifier = modifier
            .padding(vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.padding(top = 10.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                "콱씨",
                style = CustomTypo().mapleBold.copy(fontSize = 11.sp),
                color = Color.White
            )
            Text(
                "님",
                fontSize = 11.sp
            )
        }

        Text(
            text = "현재 골드 등급!",
            modifier = Modifier.padding(top = 5.dp),
        )


        Row(
            modifier = Modifier.padding(top = 20.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            StatusTextColumn(title = "획득 경험치", value = "34XP")
            StatusTextColumn(title = "현재 순위", value = "1위")
            StatusTextColumn(title = "누적 거리", value = "20km")
        }
    }
}

@Composable
fun StatusTextColumn(
    modifier: Modifier = Modifier,
    title: String,
    value: String
) {
    Column(
        modifier = Modifier.padding(horizontal = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier,
            text = title,
            fontSize = 12.sp
        )
        Text(
            modifier = Modifier,
            text = value
        )
    }

}


@Preview(device = WearDevices.LARGE_ROUND, showSystemUi = true, apiLevel = 33)
@Composable
fun HomePreview() {
    UserInfoRow(Modifier.fillMaxSize())
}

