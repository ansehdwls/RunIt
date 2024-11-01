package com.zoku.running

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zoku.running.model.RunningUIState
import com.zoku.running.util.formatTime
import com.zoku.ui.BaseDarkBackground
import com.zoku.ui.BaseYellow
import com.zoku.ui.RoundButtonGray
import com.zoku.ui.componenet.RobotoText
import com.zoku.ui.componenet.RoundRunButton
import com.zoku.ui.componenet.RoundStopButton

@Composable
fun RunningPauseScreen(
    onPlayClick: () -> Unit,
    onStopLongPress: () -> Unit,
    runningViewModel: RunningViewModel
) {
    val uiState by runningViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BaseDarkBackground),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.3f)
        ) {
            KakaoMapImage()
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.7f),
        ) {
            Spacer(modifier = Modifier.weight(0.3f))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(3f),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                InfoColumn()
                ValueColumn(uiState = uiState)
            }

            SpreadButtonBox(
                onPlayClick = onPlayClick,
                onStopLongPress = onStopLongPress
            )
        }

        Spacer(modifier = Modifier.weight(0.05f))
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SpreadButtonBox(onPlayClick: () -> Unit, onStopLongPress: () -> Unit) {

    var spread by remember { mutableStateOf(false) }

    val offsetValue by animateDpAsState(
        targetValue = if (spread) 72.dp else 0.dp,
        animationSpec = tween(durationMillis = 500)
    )

    LaunchedEffect(Unit) {
        spread = true
    }

    val haptics = LocalHapticFeedback.current

    Box(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        RoundStopButton(
            containerColor = RoundButtonGray,
            resourceId = R.drawable.baseline_stop_24,
            resourceColor = Color.White,
            modifier = Modifier
                .align(Alignment.Center)
                .offset(x = -offsetValue),
            onStopLongPress = {
                onStopLongPress()
            }
        )

        RoundRunButton(
            containerColor = BaseYellow,
            resourceId = R.drawable.baseline_play_arrow_24,
            resourceColor = Color.Black,
            modifier = Modifier
                .align(Alignment.Center)
                .offset(x = offsetValue),
            onClick = { onPlayClick() }
        )
    }
}

@Composable
fun KakaoMapImage() {
    Image(
        painter = painterResource(id = R.drawable.temp_map_image),
        contentDescription = "image",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun InfoColumn(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        RobotoText(
            text = "페이스",
            fontSize = 24.sp
        )

        RobotoText(
            text = "시간",
            fontSize = 24.sp
        )

        RobotoText(
            text = "BPM",
            fontSize = 24.sp
        )
    }
}

@Composable
fun ValueColumn(
    modifier: Modifier = Modifier,
    uiState: RunningUIState
) {
    Column(
        modifier = modifier
            .fillMaxHeight(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        RobotoText(
            text = "-'--'",
            color = BaseYellow,
            fontSize = 40.sp
        )

        RobotoText(
            text = formatTime(uiState.time),
            color = BaseYellow,
            fontSize = 40.sp
        )

        RobotoText(
            text = "${uiState.bpm}",
            color = BaseYellow,
            fontSize = 40.sp
        )
    }
}

