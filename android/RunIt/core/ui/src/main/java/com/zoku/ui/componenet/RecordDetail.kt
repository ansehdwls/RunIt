package com.zoku.ui.componenet

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import kotlin.random.Random
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.zoku.network.model.response.PaceRecord
import com.zoku.network.model.response.RunRecordDetail
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField

@Composable
fun RecordDetailInfo(
    modifier: Modifier = Modifier, startDestination: Int = 0,
    runRecord: RunRecordDetail?
) {
    if(runRecord != null){
        Log.d("확인", "RecordDetailInfo: ${runRecord.startTime}")
        val startTime = runRecord.startTime.toString()
            .substringAfter("T")
            .take(5) // 시간 부분에서 앞 5자 (예: "06:26")만 가져옴

        val endTime = runRecord.endTime.toString()
            .substringAfter("T")
            .take(5)

// 안전하게 파싱
        val startHour = startTime.substringBefore(":").toIntOrNull() ?: 0
        val endHour = endTime.substringBefore(":").toIntOrNull() ?: 0
        // 전체 box
        Box(
            modifier = modifier
                .padding(10.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
        ) {
            // 길이가 길어 지면 scroll
            Column(
                modifier = if (startDestination == 0) Modifier
                    .verticalScroll(rememberScrollState())
                else Modifier
            ) {
                // 날짜 및 시간
                RecordDate(
                    runRecord.startTime.substringBefore("T"),
                    if(startHour > 12) "오후 $startTime" else "오전 $startTime ~ " +
                            if(endHour > 12) "오후 $endTime" else  "오전 $endTime"
                )

                // 평균 기록 데이터
                RecordData(
                    modifier.fillMaxWidth(), runRecord.distance, calculateHoursDifference(
                        runRecord.startTime,
                        runRecord.endTime
                    ), runRecord.bpm
                )

                // 그래프
                RecordGraph("구간별 심박수", runRecord.paceList, 1)

                RecordGraph("구간별 페이스", runRecord.paceList, 2)

                if (startDestination == 0) {
                    // 도전하기 버튼
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = {},
                            modifier = Modifier
                        ) {
                            Text(text = "도전하기", fontFamily = com.zoku.ui.ZokuFamily)
                        }
                    }
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
        Text(
            text = today,
            textAlign = TextAlign.Start,
            modifier = Modifier.weight(1f),
            fontFamily = com.zoku.ui.ZokuFamily
        )
        Text(
            text = time,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f),
            fontFamily = com.zoku.ui.ZokuFamily
        )
    }
}

@Composable
fun RecordData(modifier: Modifier = Modifier, distance: Double, time: String, bpm: Int) {
    Row(
        modifier = modifier
            .padding(10.dp)
    ) {
        AverageData(modifier.weight(1f), data = "$distance", "km")
        AverageData(modifier.weight(1f), data = time, "")
        AverageData(modifier.weight(1f), data = "$bpm", "bpm")
    }
}

@Composable
fun AverageData(modifier: Modifier = Modifier, data: String, type: String) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = data,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            fontFamily = com.zoku.ui.ZokuFamily
        )
        Text(
            text = type,
            modifier = Modifier.padding(start = 5.dp),
            fontFamily = com.zoku.ui.ZokuFamily
        )
    }
}


@Composable
fun RecordGraph(title: String, list: List<PaceRecord>, type: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp)
    ) {
        Text(text = title, fontFamily = com.zoku.ui.ZokuFamily)
        // 예시 데이터
        LineChartView(list, type)
    }
}

@Composable
fun LineChartView(list: List<PaceRecord>, type: Int) {
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
            val entries = (0..list.size-1).map { Entry((it+1).toFloat(),
                if(type == 1 ) list[it].bpmList.toFloat() else list[it].durationList?.toFloat() ?: 0.0f) }
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

fun calculateHoursDifference(startTime: String, endTime: String): String {
    // DateTimeFormatterBuilder를 사용하여 1~3자리 밀리초 지원
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

    // 차이를 시간으로 변환하여 소수점 첫째 자리까지 형식화
    val hours = duration.toMinutes().toDouble() / 60
    return String.format("%.1f hr", hours)
}
