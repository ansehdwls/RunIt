package com.zoku.running

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
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zoku.ui.BaseDarkBackground
import com.zoku.ui.BaseYellow
import com.zoku.ui.RoundButtonGray
import com.zoku.ui.componenet.RobotoText
import com.zoku.ui.componenet.RoundRunButton

@Composable
fun RunningPauseScreen(onPlayClick: () -> Unit) {
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
                ValueColumn()
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                RoundRunButton(
                    containerColor = RoundButtonGray,
                    resourceId = R.drawable.baseline_stop_24,
                    resourceColor = Color.White,
                    onClick = { onPlayClick() }
                )

                Spacer(modifier = Modifier.width(24.dp))

                RoundRunButton(
                    containerColor = BaseYellow,
                    resourceId = R.drawable.baseline_play_arrow_24,
                    resourceColor = Color.Black,
                    onClick = { onPlayClick() }
                )
            }
        }

        Spacer(modifier = Modifier.weight(0.05f))
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
fun ValueColumn(modifier: Modifier = Modifier) {
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
            text = "05:24",
            color = BaseYellow,
            fontSize = 40.sp
        )

        RobotoText(
            text = "97",
            color = BaseYellow,
            fontSize = 40.sp
        )
    }
}

