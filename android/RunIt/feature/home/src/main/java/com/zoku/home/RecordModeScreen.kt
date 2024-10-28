package com.zoku.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RecordModeScreen(modifier: Modifier = Modifier, moveToDetail :()->Unit){
  Column(
      modifier = modifier
          .fillMaxWidth()
          .fillMaxHeight()
          .background(com.zoku.ui.BaseGray)
          .systemBarsPadding()
  ) {
      // 타이틀
        Text(text = "기록갱신",
            color = Color.White,
            fontSize = 32.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            textAlign = TextAlign.Center
        )

      // 기록 리스트
      RecordList(
          modifier
              .weight(1f)
              .padding(horizontal = 20.dp)
          , moveToDetail= moveToDetail
      )
  }
}

@Composable
fun RecordList(modifier: Modifier, moveToDetail :()->Unit){
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
    ) {

        // 날짜
        Text(text = "2024-10-27",
            color = Color.White,
            modifier = Modifier.fillMaxWidth())

        //가록 Surface
        Surface(
            onClick = { moveToDetail() },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(top = 5.dp)
                .clip(RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                // 시간
                Text(
                    text = "오후 3:37 ~ 오후 3:52",
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.End
                )

                // 지도 및 데이터
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Image(painter = painterResource(id = R.drawable.sample_map_history_icon),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .weight(3f)
                            .padding(10.dp))
                    RecordTextView(
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun RecordTextView(modifier: Modifier){
    Column(
        modifier = modifier
    ) {
        Box(modifier = Modifier.weight(2f)){
            Text(text = "시용지 이름",
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center))
        }
        Box(modifier = Modifier.weight(3f)){
            Column {
                Text(text = "거리 : 3km")
                Text(text = "시간 : 15분")
            }
        }
    }
}