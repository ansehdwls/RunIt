package com.zoku.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ExpHistory(modifier: Modifier = Modifier){

    LazyColumn(
        modifier.fillMaxSize()
    ) {
        item {
            Text(text = "획득경험치", modifier = Modifier
                .fillMaxWidth().padding(vertical = 20.dp),
                fontSize = 30.sp,
                fontFamily = com.zoku.ui.ZokuFamily,
                textAlign = TextAlign.Center)
        }

        items(3) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp, vertical = 12.dp) // Box 외부에 padding 적용
                    .height(100.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(com.zoku.ui.BaseDarkBackground)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(), // Row가 Box의 크기에 맞도록 설정
                    verticalAlignment = Alignment.CenterVertically // Row의 내용물 가운데 정렬
                ) {
                    Text(
                        text = "2024-10-28",
                        modifier = Modifier.padding(start = 8.dp, top = 8.dp)
                            .align(Alignment.Top),
                        textAlign = TextAlign.Start,
                        fontFamily = com.zoku.ui.ZokuFamily
                    )
                    Text(
                        text = "1KM 달성!",
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp),
                        textAlign = TextAlign.Start,
                        fontSize = 30.sp,
                        fontFamily = com.zoku.ui.ZokuFamily
                    )
                    Text(
                        text = "+50xp",
                        modifier = Modifier.padding(end = 8.dp),
                        textAlign = TextAlign.End,
                        fontFamily = com.zoku.ui.ZokuFamily
                    )
                }
            }
        }
    }
}
