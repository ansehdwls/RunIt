package com.zoku.running

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
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
import com.zoku.ui.componenet.RobotoText
import com.zoku.ui.componenet.RoundRunButton

@Composable
fun RunningPlayScreen(onPauseClick: () -> Unit) {
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
                topName = "0",
                bottomName = "BPM"
            )
            TopInfoWithText(
                topName = "-'--'",
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
                text = "3.14",
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

        RoundRunButton(
            containerColor = BaseYellow,
            resourceId = R.drawable.baseline_pause_24,
            resourceColor = Color.Black,
            onClick = { onPauseClick() }
        )

        Spacer(modifier = Modifier.weight(0.3f))
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
        RunningScreen()
    }
}

