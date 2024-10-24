package com.zoku.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RunHistoryScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .padding(0.dp)
            .fillMaxWidth()
            .fillMaxHeight()
            .background(com.zoku.ui.BaseGray)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp)
                .height(60.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .fillMaxHeight()  // 부모의 높이를 채워 가운데 정렬
                    .wrapContentWidth()
                    .align(Alignment.CenterVertically)
                    .padding(horizontal = 10.dp),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.pre_run_history_icon),
                    contentDescription = null,
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                        .align(Alignment.Center)  // 가로, 세로 모두 가운데 정렬
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxHeight()  // 부모의 높이를 채워 가운데 정렬
                    .wrapContentWidth()
                    .align(Alignment.CenterVertically),
            ) {
                Text(
                    text = "10월",
                    color = Color.White,
                    fontSize = 30.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxHeight()  // 부모의 높이를 채워 가운데 정렬
                    .wrapContentWidth()
                    .align(Alignment.CenterVertically)
                    .padding(horizontal = 10.dp),

            ) {
                Image(
                    painter = painterResource(id = R.drawable.next_run_history_icon),
                    contentDescription = null,
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                        .align(Alignment.Center)  // 가로, 세로 모두 가운데 정렬
                )
            }

            Spacer(modifier = Modifier.weight(1f))
        }

    }
}