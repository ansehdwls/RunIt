package com.zoku.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.zoku.home.viewmodel.RecordModeViewModel
import com.zoku.network.model.response.RunPractice
import com.zoku.ui.theme.BaseGray
import com.zoku.ui.theme.CustomTypo
import com.zoku.ui.theme.ZokuFamily
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField

@Composable
fun RecordModeScreen(
    modifier: Modifier = Modifier,
    viewModel: RecordModeViewModel = hiltViewModel(),
    moveToDetail: (Int) -> Unit,
    onBackButtonClick: () -> Unit
) {

    val practiceList by viewModel.practiceList.collectAsState()

    viewModel.getPracticeList()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(BaseGray)
            .systemBarsPadding()
    ) {
        // 타이틀

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                onClick = {
                    onBackButtonClick()
                },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back_white),
                    contentDescription = "back",
                    tint = Color.White
                )
            }
            Text(
                text = "기록갱신",
                style = CustomTypo().mapleLight.copy(
                    color = Color.White,
                    fontSize = 32.sp,
                ),
                modifier = Modifier.align(Alignment.Center)
            )


        }


        // 기록 리스트
        if (practiceList.isNotEmpty()) {
            RecordList(
                modifier
                    .weight(1f)
                    .padding(horizontal = 20.dp), moveToDetail = moveToDetail,
                practiceList
            )
        }
    }
}

@Composable
fun RecordList(
    modifier: Modifier, moveToDetail: (Int) -> Unit,
    runningAllList: List<RunPractice>
) {
    var today = ""
    LazyColumn(
        modifier = modifier
    ) {

        if (runningAllList.isEmpty()) {
            item {
                Text(
                    text = "현재 기록이 없습니다",
                    style = CustomTypo().jalnan.copy(
                        color = Color.White
                    ),
                )
            }
        }


        items(runningAllList.size) { index ->
            val item = runningAllList[index]
            if (today != item.startTime.substringBefore("T")) {
                // 날짜
                Text(
                    text = item.startTime.substringBefore("T"),
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth(),
                    fontFamily = ZokuFamily
                )
                today = item.startTime.substringBefore("T")
            }

            RecordDataView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(top = 10.dp)
                    .clip(RoundedCornerShape(16.dp)), moveToDetail = moveToDetail,
                item
            )
        }

    }
}

@Composable
fun RecordDataView(
    modifier: Modifier = Modifier, moveToDetail: (Int) -> Unit,
    item: RunPractice
) {

    val startTime = item.startTime.substringAfter("T").substring(0, 5)
    val endTime = item.endTime.substringAfter("T").substring(0, 5)
    val startHour = startTime.substringBefore(":").toInt()
    val endHour = endTime.substringBefore(":").toInt()

    //가록 Surface
    Surface(
        onClick = { moveToDetail(item.id) },
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            // 시간
            Text(
                text = if (startHour > 12) "오후 $startTime ~ " else "오전 $startTime ~ " + if (endHour > 12) "오후 $endTime" else "오전 $endTime",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, end = 25.dp),
                fontFamily = ZokuFamily
            )


            // 지도 및 데이터
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(item.imageUrl),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .weight(3f)
                        .padding(10.dp)
                )
                RecordTextView(
                    modifier = Modifier.weight(1f),
                    item
                )
            }
        }
    }
}

@Composable
fun RecordTextView(
    modifier: Modifier,
    item: RunPractice
) {
    Column(
        modifier = modifier
    ) {
        Box(modifier = Modifier.weight(2f)) {
            Text(
                text = item.name,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center),
                fontFamily = ZokuFamily
            )
        }
        Box(modifier = Modifier.weight(3f)) {
            Column {
                Text(
                    text = "${item.distance}km",
                    fontFamily = ZokuFamily
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = calculateHoursDifference(
                        item.startTime,
                        item.endTime
                    ),
                    fontFamily = ZokuFamily
                )
                Spacer(modifier = Modifier.height(5.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRecord() {
    RecordModeScreen(
        Modifier,
        viewModel = hiltViewModel(),
        onBackButtonClick = {},
        moveToDetail = {}
    )
}

fun calculateHoursDifference(startTime: String, endTime: String): String {
    // DateTimeFormatterBuilder로 밀리초 지원
    val formatter = DateTimeFormatterBuilder()
        .appendPattern("yyyy-MM-dd'T'HH:mm:ss")
        .optionalStart()
        .appendFraction(ChronoField.MILLI_OF_SECOND, 1, 3, true)
        .optionalEnd()
        .toFormatter()

    // 문자열을 LocalDateTime으로 변환
    val startDateTime = LocalDateTime.parse(startTime, formatter)
    val endDateTime = LocalDateTime.parse(endTime, formatter)

    // 두 시간 사이의 차이 계산
    val duration = Duration.between(startDateTime, endDateTime)

    // 시간과 분으로 변환
    val hours = duration.toHours()
    val minutes = duration.toMinutes() % 60

    // "0 시간 10 분" 형태로 반환
    return "${hours}시간 ${minutes}분"
}