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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.zoku.ui.CustomTypo

@Composable
fun RecordModeScreen(modifier: Modifier = Modifier, moveToDetail :()->Unit){

    val viewModel : RecordModeViewModel = hiltViewModel()




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
            textAlign = TextAlign.Center,
            fontFamily = com.zoku.ui.ZokuFamily
        )

//      // 기록 리스트
//      if(runningAllList.isNotEmpty()){
//          RecordList(
//              modifier
//                  .weight(1f)
//                  .padding(horizontal = 20.dp)
//              , moveToDetail= moveToDetail,
//              runningAllList
//          )
//      }
  }
}
//
//@Composable
//fun RecordList(modifier: Modifier, moveToDetail :()->Unit,
//               runningAllList : List<RunningAllHistory>){
//    LazyColumn(
//        modifier = modifier
//    ) {
//
//        if(runningAllList.size == 0) {
//            item{
//                Text(text = "현재 날짜에는 기록이 없습니다",
//                    style = CustomTypo().jalnan.copy(
//                        color = Color.White
//                    ),
//                    )
//            }
//
//        }
//
//
//        items(runningAllList.size){ index ->
//            val item = runningAllList[index]
//            // 날짜
//            Text(text = item.startTime.substringBefore("T"),
//                color = Color.White,
//                modifier = Modifier.fillMaxWidth(),
//                fontFamily = com.zoku.ui.ZokuFamily)
//
//            RecordDataView(modifier = Modifier
//                .fillMaxWidth()
//                .height(200.dp)
//                .padding(top = 5.dp)
//                .clip(RoundedCornerShape(16.dp))
//                ,moveToDetail = moveToDetail,
//                item)
//        }
//
//    }
//}
//
//@Composable
//fun RecordDataView(modifier : Modifier = Modifier, moveToDetail : () -> Unit,
//                   item : RunningAllHistory){
//
//    val startTime = item.startTime.substringAfter("T").substring(0, 5)
//    val endTime = item.endTime.substringAfter("T").substring(0, 5)
//    val startHour = startTime.substringBefore(":").toInt()
//    val endHour = endTime.substringBefore(":").toInt()
//
//    //가록 Surface
//    Surface(
//        onClick = { moveToDetail() },
//        modifier = modifier
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .fillMaxHeight()
//        ) {
//            // 시간
//            Text(
//                text = if(startHour > 12) "오후 $startTime" else "오전 $startTime",
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(top = 10.dp, end = 25.dp),
//                textAlign = TextAlign.End,
//                fontFamily = com.zoku.ui.ZokuFamily
//            )
//
//            Text(
//                text = "~ ${if(endHour > 12) "오후 $endTime" else  "오전 $endTime" }",
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(end = 10.dp),
//                textAlign = TextAlign.End,
//                fontFamily = com.zoku.ui.ZokuFamily
//            )
//
//
//            // 지도 및 데이터
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .weight(1f)
//            ) {
//                Image(painter = rememberAsyncImagePainter(item.imageUrl),
//                    contentDescription = null,
//                    contentScale = ContentScale.Crop,
//                    modifier = Modifier
//                        .weight(3f)
//                        .padding(10.dp))
//                RecordTextView(
//                    modifier = Modifier.weight(1f),
//                    item
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun RecordTextView(modifier: Modifier,
//                   item: RunningAllHistory){
//    Column(
//        modifier = modifier
//    ) {
//        Box(modifier = Modifier.weight(2f)){
//            Text(text = item.name,
//                textAlign = TextAlign.Center,
//                modifier = Modifier.align(Alignment.Center),
//                fontFamily = com.zoku.ui.ZokuFamily)
//        }
//        Box(modifier = Modifier.weight(3f)){
//            Column {
//                Text(text = "거리 : ${item.distance}km",
//                    fontFamily = com.zoku.ui.ZokuFamily)
//                Text(text = "시간 : ${item.bpm}분",
//                    fontFamily = com.zoku.ui.ZokuFamily)
//            }
//        }
//    }
//}