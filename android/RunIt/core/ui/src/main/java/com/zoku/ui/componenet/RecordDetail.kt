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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.zoku.network.model.response.PaceRecord
import com.zoku.network.model.response.RunRecordDetail
import com.zoku.ui.theme.BaseYellow
import com.zoku.ui.theme.ZokuFamily
import java.time.Duration
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField

@Composable
fun RecordDetailInfo(
    modifier: Modifier = Modifier, startDestination: Int = 0,
    runRecord: RunRecordDetail?,
    moveToRunning: (recordDto: RunRecordDetail) -> Unit = {}
) {
    if (runRecord != null) {
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
                modifier = Modifier
                    .verticalScroll(rememberScrollState())

            ) {
                val startString = if (startHour >= 12) "오후 $startTime ~ " else "오전 $startTime ~ "
                val endString = if (endHour >= 12) "오후 $endTime" else "오전 $endTime"
                // 날짜 및 시간
                RecordDate(
                    runRecord.startTime.substringBefore("T"),
                    startString + endString
                )

                // 평균 기록 데이터
                RecordData(
                    modifier.fillMaxWidth(), runRecord.distance, calculateHoursDifference(
                        runRecord.startTime,
                        runRecord.endTime,
                        runRecord.id
                    ), runRecord.paceList.map { it.durationList ?: 0 }
                        .average().toInt()
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
                            onClick = {
                                moveToRunning(runRecord)
                            },
                            modifier = Modifier
                        ) {
                            Text(text = "도전하기", fontFamily = ZokuFamily)
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
            fontFamily = ZokuFamily
        )
        Text(
            text = time,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f),
            fontFamily = ZokuFamily
        )
    }
}

@Composable
fun RecordData(modifier: Modifier = Modifier, distance: Double, time: String, pace: Int) {
    Row(
        modifier = modifier
            .padding(10.dp)
    ) {
        AverageData(modifier.weight(1f), data = "$distance", "km")
        AverageData(modifier.weight(1f), data = time, "")
        AverageData(modifier.weight(1f), data = "${pace / 60}'${pace % 60}", "pace")
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
            fontFamily = ZokuFamily
        )
        Text(
            text = type,
            modifier = Modifier.padding(start = 5.dp),
            fontFamily = ZokuFamily
        )
    }
}


@Composable
fun RecordGraph(title: String, list: List<PaceRecord>, type: Int) {
    Log.d("씹버그", "${list}")
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp)
    ) {
        Text(text = title, fontFamily = ZokuFamily)
        // 예시 데이터
        LineChartView(list, type)
    }
}

@Composable
fun LineChartView(list: List<PaceRecord>, type: Int) {
    var maxY by remember {
        mutableStateOf(0)
    }
    list.forEach { l ->
        maxY = maxY.coerceAtLeast(l.durationList ?: 0)
    }
    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .padding(16.dp),
        factory = { context ->
            LineChart(context).apply {
                // X축 설정
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.setDrawGridLines(false)
                xAxis.granularity = 100f  // X축 레이블 간격

                // Y축 설정
                axisRight.isEnabled = false

                with(axisLeft) {
                    setDrawGridLines(false)

                    // 레이블의 마진 조정
                    xOffset = 10f // Y축 레이블과 차트 축 간의 간격 (수평)
                    yOffset = 10f // 레이블과 레이블 간 간격 (수직)
                    axisMinimum =
                        if (type == 1) 60f else 0f  // bpmList 최소값: 60, durationList 최소값: 0
                    axisMaximum =
                        if (type == 1) 160f else (maxY / 60f) + 1 // bpmList 최대값: 160, durationList 최대값: 1200
                }


                // 기타 설정
                description.isEnabled = false
                setTouchEnabled(true)
                setPinchZoom(true)
                animateX(1000)  // X축 애니메이션
            }
        },
        update = { lineChart ->
            val entries = if (list.size == 1) {
                // 데이터가 하나일 경우 가상의 두 번째 점 추가
                val singleEntry = Entry(
                    100f, if (type == 1) {
                        list[0].bpmList.coerceIn(60, 160).toFloat()
                    } else {
//                        list[0].durationList?.coerceIn(0, maxY/60f+1).toFloat()
                        (list[0].durationList?.coerceAtMost(maxY)?.toFloat() ?: 0.0f) / 60f
                    }
                )
                listOf(singleEntry, Entry(200f, singleEntry.y)) // 동일한 Y값으로 두 번째 점 추가
            } else {
                // 데이터가 여러 개일 경우 일반적인 처리
                list.indices.map { index ->
                    val value = if (type == 1) {
                        list[index].bpmList.coerceIn(60, 160).toFloat()
                    } else {
                        (list[index].durationList?.coerceAtMost(maxY)?.toFloat() ?: 0.0f) / 60f
                    }
                    Entry(((index + 1) * 100).toFloat(), value)
                }
            }

            val lineDataSet = LineDataSet(entries, if (type == 1) "심박수 bpm" else "페이스 m/h").apply {
                color = BaseYellow.toArgb()
                lineWidth = 2f
                circleRadius = 4f
                setDrawCircleHole(false)
                setDrawCircles(false)
                setDrawValues(false)  // 각 점의 값 숨기기
            }
            lineChart.data = LineData(lineDataSet)
        }
    )
}

/*
@Composable
fun LineChartView(list: List<PaceRecord>, type: Int) {
    var maxY = 1
    list.forEach { l -> maxY = maxY.coerceAtLeast(l.durationList ?: 0) }

    val entries = remember(list) {
        list.indices.map { index ->
            val value = if (type == 1) {
                list[index].bpmList.coerceIn(60, 160).toFloat()
            } else {
                (list[index].durationList?.toFloat() ?: 0.0f) / 60f
            }
            Entry(((index + 1) * 100).toFloat(), value)
        }
    }

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .padding(16.dp),
        factory = { context ->
            LineChart(context).apply {
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.setDrawGridLines(false)
                axisRight.isEnabled = false
                axisLeft.setDrawGridLines(false)
                axisLeft.axisMinimum = if (type == 1) 60f else 0f
                axisLeft.axisMaximum = if (type == 1) 160f else (maxY / 60f) + 1
                description.isEnabled = false
                setTouchEnabled(true)
                animateX(1000)
            }
        },
        update = { lineChart ->
            val lineDataSet = LineDataSet(entries, if (type == 1) "심박수 bpm" else "페이스 min/km").apply {
                color = BaseYellow.toArgb()
                lineWidth = 2f
                setDrawCircleHole(false)
                setDrawCircles(true)
                setDrawValues(false)
            }
            lineChart.data = LineData(lineDataSet)
            lineChart.notifyDataSetChanged()
            lineChart.invalidate()
        }
    )
}
 */



fun calculateHoursDifference(startTime: String, endTime: String, id: Int): String {
    // DateTimeFormatterBuilder로 밀리초 지원
    val formatter = DateTimeFormatterBuilder()
        .appendPattern("yyyy-MM-dd'T'HH:mm:ss")
        .optionalStart()
        .appendFraction(ChronoField.MILLI_OF_SECOND, 1, 3, true)
        .optionalEnd()
        .toFormatter()

    // ISO 8601 형식 파싱
    val formatterZ = DateTimeFormatter.ISO_OFFSET_DATE_TIME


    // 문자열을 LocalDateTime으로 변환
    val startDateTime = try {
        OffsetDateTime.parse(startTime, formatterZ)
    } catch (e: Exception) {
        LocalDateTime.parse(startTime, formatter).atOffset(ZoneOffset.UTC) // Z 없는 경우 UTC로 가정
    }

    val endDateTime = try {
        OffsetDateTime.parse(endTime, formatterZ)
    } catch (e: Exception) {
        LocalDateTime.parse(endTime, formatter).atOffset(ZoneOffset.UTC) // Z 없는 경우 UTC로 가정
    }


    // 두 시간 사이의 차이 계산
    val duration = Duration.between(startDateTime, endDateTime)

    // 시간과 분으로 변환
    val hours = duration.toHours()
    val minutes = duration.toMinutes() % 60
    if (hours < 0) return "0시간 0분"
    else return "${hours}시간 ${minutes}분"
}