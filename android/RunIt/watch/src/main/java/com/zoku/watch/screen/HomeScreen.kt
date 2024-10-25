package com.zoku.watch.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text
import androidx.wear.tooling.preview.devices.WearDevices
import com.zoku.watch.component.PagerScreen
import com.zoku.watch.component.StartButton

@Composable
fun HomeScreen(
    modifier: Modifier,
    onStartClick: () -> Unit
) {

    val items: List<@Composable () -> Unit> =
        listOf({
            StartButton(modifier) {
                onStartClick()
            }
        }, { UserInfoRow(modifier) })
    val pagerState = rememberPagerState(pageCount = { items.size })
    PagerScreen(
        modifier = modifier,
        state = pagerState,
        items = items
    )
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
                textAlign = TextAlign.Start,
                fontSize = 13.sp,
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

