package com.zoku.running

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zoku.ui.BaseDarkBackground
import com.zoku.ui.BaseYellow
import com.zoku.ui.RoundButtonGray
import com.zoku.ui.componenet.RobotoText
import com.zoku.ui.componenet.RoundRunButton

@Composable
fun RunningPlayScreen(
    onPauseClick: () -> Unit,
    isFirstPlay: Boolean = true,
    runningViewModel: RunningViewModel
) {

    LaunchedEffect(Unit) {
        runningViewModel.startMeasuringDistance()
    }
    val uiState by runningViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BaseDarkBackground),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(0.2f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TopInfoWithText(
                topName = "-'--'",
                bottomName = "페이스"
            )
            TopInfoWithText(
                topName = "${uiState.bpm}",
                bottomName = "BPM"
            )
            TopInfoWithText(
                topName = "${uiState.time}",
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
                text = "${uiState.distance / 1000}.${(uiState.distance/10)%100}",
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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Start)
        ) {
            Spacer(modifier = Modifier.weight(0.15f))

            RobotoText(
                text = "심박수",
                fontSize = 20.sp,
            )

            Spacer(modifier = Modifier.weight(0.9f))
        }

        Spacer(modifier = Modifier.weight(0.3f))

        TempHeartGraph()

        Spacer(modifier = Modifier.weight(0.3f))

        if (isFirstPlay) {
            RoundRunButton(
                containerColor = BaseYellow,
                resourceId = R.drawable.baseline_pause_24,
                resourceColor = Color.Black,
                onClick = {
                    runningViewModel.pauseMeasuringDistance()
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
fun GatherButtonBox(onPauseClick: () -> Unit, runningViewModel: RunningViewModel?) {
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
                runningViewModel?.pauseMeasuringDistance()
                onPauseClick()
            })
    }
}


@Composable
fun TempHeartGraph() {
    Image(
        painter = painterResource(id = R.drawable.temp_chart),
        contentDescription = "Temperature Chart",
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 50.dp),
        contentScale = ContentScale.Crop
    )
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

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    com.zoku.ui.RunItTheme {
        RunningPlayScreen(
            onPauseClick = { Log.d("Zz", "Zzz") },
            runningViewModel = RunningViewModel()
        )
    }
}

