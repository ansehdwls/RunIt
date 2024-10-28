package com.zoku.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlin.random.Random

@Composable
fun RecordModeDetail(modifier: Modifier = Modifier) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(com.zoku.ui.BaseGray)
            .systemBarsPadding()
    ) {
        // title
        RecordDetailTitle(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp)
        )

        // 지도 경로 표시
        RecordMap()

        // 세부 기록 표시
        RecordDetailInfo(
            modifier.weight(1f)
        )
    }
}

@Composable
fun RecordDetailTitle(modifier: Modifier = Modifier) {
    var isEditing by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("사용자 코스") }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 수정 버튼 클릭 시
                if (isEditing) {
                    BasicTextField(
                        value = title,
                        onValueChange = { title = it },
                        singleLine = true,
                        textStyle = TextStyle(
                            color = Color.White,
                            fontSize = 32.sp,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier.background(Color.Transparent),
                        decorationBox = { innerTextField ->
                            Box(
                                modifier = Modifier
                                    .background(Color.Transparent)
                                    .padding(4.dp)
                            ) {
                                if (title.isEmpty()) {
                                    Text(
                                        text = "코스명을 입력하세요",
                                        color = Color.Gray,
                                        style = TextStyle(fontSize = 20.sp)
                                    )
                                }
                                innerTextField()  // 실제 입력 필드
                            }
                        }
                    )
                    IconButton(onClick = { isEditing = false }) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            tint = Color.White,
                            contentDescription = null
                        )
                    }
                }
                else {
                    Text(
                        text = title,
                        color = Color.White,
                        fontSize = 32.sp,
                        textAlign = TextAlign.Center
                    )
                    IconButton(
                        onClick = { isEditing = true }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.edit_record_detail_icon),
                            tint = Color.White,
                            modifier = Modifier
                                .width(25.dp)
                                .height(25.dp),
                            contentDescription = null
                        )
                    }
                }


            }
        }

        IconButton(onClick = {}) {
            Icon(
                painter = painterResource(id = R.drawable.delete_record_detail_icon),
                contentDescription = null,
                modifier = Modifier
                    .width(25.dp)
                    .height(25.dp),
                tint = Color.Red
            )
        }
    }
}

@Composable
fun RecordMap(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.sample_map_history_icon),
        contentDescription = null,
        modifier = modifier
            .fillMaxWidth(),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun RecordDetailInfo(modifier: Modifier = Modifier) {
    // 전체 box
    Box(
        modifier = modifier
            .padding(10.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
    ) {
        // 길이가 길어 지면 scroll
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ) {
            // 날짜 및 시간
            RecordDate(
                "2024-10-27",
                "오후 3:37 ~ 오후 3:52"
            )

            // 평균 기록 데이터
            RecordData(modifier.fillMaxWidth(), 20.1f, "20", 100)

            // 그래프
            RecordGraph("구간별 심박수")

            RecordGraph("구간별 페이스")

            // 도전하기 버튼
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {},
                    modifier = Modifier
                ) {
                    Text(text = "도전하기")
                }
            }
        }
    }
}

@Composable
fun RecordDate(today: String, time: String) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Text(text = today, textAlign = TextAlign.Start, modifier = Modifier.weight(1f))
        Text(text = time, textAlign = TextAlign.End, modifier = Modifier.weight(1f))
    }
}

@Composable
fun RecordData(modifier: Modifier = Modifier, distance: Float, time: String, bpm: Int) {
    Row(
        modifier = modifier
            .padding(10.dp)
    ) {
        AverageData(modifier.weight(1f), data = "$distance", "km")
        AverageData(modifier.weight(1f), data = "$time", "hr")
        AverageData(modifier.weight(1f), data = "$bpm", "bpm")
    }
}

@Composable
fun AverageData(modifier: Modifier = Modifier, data: String, type: String) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(text = data, fontSize = 20.sp, textAlign = TextAlign.Center)
        Text(text = type, modifier = Modifier.padding(start = 5.dp))
    }
}

@Composable
fun RecordGraph(title: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp)
    ) {
        Text(text = title)
        // 예시 데이터
        LineChartView()
    }
}

@Composable
fun LineChartView() {
    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(16.dp),
        factory = { context ->
            LineChart(context).apply {
                // X축 설정
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.setDrawGridLines(false)
                xAxis.granularity = 1f  // X축 레이블 간격

                // Y축 설정
                axisRight.isEnabled = false
                axisLeft.setDrawGridLines(false)

                // 기타 설정
                description.isEnabled = false
                setTouchEnabled(true)
                setPinchZoom(true)
                animateX(1000)  // X축 애니메이션
            }
        },
        update = { lineChart ->
            val entries = (1..10).map { Entry(it.toFloat(), Random.nextFloat() * 10) }
            val lineDataSet = LineDataSet(entries, "Sample Data").apply {
                color = com.zoku.ui.BaseYellow.toArgb()
                lineWidth = 2f
                circleRadius = 4f
                setDrawCircleHole(false)
                setDrawCircles(false)
                setDrawValues(false)  // 각 점의 값 숨기기
            }
            lineChart.data = LineData(lineDataSet)
            lineChart.invalidate()  // 차트 갱신
        }
    )
}