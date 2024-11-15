package com.zoku.home


import android.graphics.Color.TRANSPARENT
import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.zoku.home.component.DropDownMenu
import com.zoku.home.viewmodel.InfoViewModel
import com.zoku.network.model.response.RunToday
import com.zoku.network.model.response.RunWeekList
import com.zoku.ui.theme.BaseWhiteBackground
import com.zoku.ui.theme.BaseYellow
import com.zoku.ui.theme.CustomTypo
import com.zoku.ui.theme.PurpleGrey80
import com.zoku.ui.theme.ZokuFamily
import java.time.LocalDate
import java.time.temporal.WeekFields
import java.util.Locale

@Composable
fun InfoScreen(
    modifier: Modifier = Modifier,
    moveToHistory: () -> Unit,
    moveToRecordMode: () -> Unit,
    moveToRunning: () -> Unit
) {
    val context = LocalContext.current
    val runningDiaryMenu = arrayOf("거리", "시간", "페이스")
    val runningRecordMenu = arrayOf("전체", "일주일")

    var isAllRecord by remember {
        mutableStateOf(true)
    }
    var listType by remember {
        mutableStateOf(0)
    }
    val isFlipped = remember { mutableStateOf(false) }

    val rotation by animateFloatAsState(
        targetValue = if (isFlipped.value) 360f else 0f,
        animationSpec = tween(durationMillis = 500)
    )

    val viewModel: InfoViewModel = hiltViewModel()
    val todayRecord by viewModel.todayRecord.collectAsState()
    val totalAllRecord by viewModel.totalAllRecord.collectAsState()
    val totalWeekRecord by viewModel.totalWeekRecord.collectAsState()
    val totalWeekList by viewModel.totalWeekList.collectAsState()

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Column(
            modifier = modifier

        ) {
            // 기본 패딩
            Spacer(modifier = modifier.height(20.dp))

            Text(
                text = context.getString(R.string.today),
                color = Color.White,
                modifier = modifier.padding(bottom = 5.dp),
                fontFamily = ZokuFamily
            )
            TodayDashBoard(modifier.align(Alignment.CenterHorizontally), todayRecord)

            Spacer(modifier = modifier.height(20.dp))

            HomeTitle(
                modifier.padding(bottom = 5.dp),
                context.getString(R.string.running_diary),
                "거리",
                runningDiaryMenu
            ) { selectedOption ->
                if (selectedOption == context.getString(R.string.running_diary) + " 거리") listType =
                    0
                else if (selectedOption == context.getString(R.string.running_diary) + " 시간") listType =
                    1
                else listType = 2
            }

            RunningDiary(modifier.align(Alignment.CenterHorizontally), totalWeekList, listType)

            Spacer(modifier = modifier.height(20.dp))

            HomeTitle(
                modifier.padding(bottom = 5.dp),
                "러닝 기록",
                "전체",
                runningRecordMenu
            ) { selectedOption ->
                if (selectedOption == "러닝 기록 전체") isAllRecord = true
                else isAllRecord = false
                isFlipped.value = !isFlipped.value
            }
            if (isAllRecord) {
                RunningRecord(
                    modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(BaseWhiteBackground)
                        .graphicsLayer {
                            rotationY = rotation
                            transformOrigin = TransformOrigin.Center

                        }, totalAllRecord.totalDistance,
                    totalAllRecord.totalTime,
                    BaseWhiteBackground
                )
            } else RunningRecord(
                modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(BaseYellow)
                    .graphicsLayer {
                        rotationY = rotation
                    },
                totalWeekRecord.weekDistance,
                totalWeekRecord.weekTime,
                BaseYellow
            )

            Spacer(modifier = modifier.height(30.dp))

            Row(
                modifier = modifier.fillMaxWidth()
            ) {
                val buttonModifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .height(90.dp)

                val iconModifier = Modifier
                    .height(80.dp)
                    .width(60.dp)
                    .weight(1f)

                HomeFunctionButton(
                    modifier = buttonModifier
                        .background(Color.White), onClick = {
                        moveToHistory()
                    }, icon = R.drawable.ic_calendar_info, "히스토리", iconModifier
                )

                Spacer(modifier = Modifier.width(20.dp))

                HomeFunctionButton(
                    modifier = buttonModifier
                        .background(BaseYellow),
                    onClick = { moveToRunning() },
                    icon = R.drawable.ic_run_info,
                    "",
                    iconModifier
                        .height(60.dp)
                        .width(40.dp)
                )

                Spacer(modifier = Modifier.width(20.dp))

                HomeFunctionButton(
                    modifier = buttonModifier
                        .background(Color.White), onClick = {
                        moveToRecordMode()
                    }, icon = R.drawable.ic_ranking_info, "기록 갱신", iconModifier
                )
            }
        }
    }

}

@Composable
fun TodayDashBoard(modifier: Modifier = Modifier, todayRecord: RunToday) {

    val iconModifier = Modifier
        .height(30.dp)
        .width(30.dp)
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .fillMaxWidth()
            .height(80.dp)
            .background(BaseWhiteBackground)
    ) {
        Row(
            Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .fillMaxHeight(), // Row가 Box의 전체 높이를 채우도록 설정
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {

            TodayRecord(
                modifier = Modifier.weight(1f),
                dir = Arrangement.Center,
                iconModifier = iconModifier,
                result = "${todayRecord.distance}", unit = "Km",
                imgResource = R.drawable.ic_run_home
            )

            VerticalDivider(
                modifier = Modifier
                    .padding(vertical = 15.dp, horizontal = 10.dp)
                    .fillMaxHeight()
                    .width(1.dp), // 선의 두께를 1.dp로 설정
                color = Color.Gray
            )

            TodayRecord(
                modifier = Modifier.weight(1f),
                dir = Arrangement.Center,
                iconModifier = iconModifier,
                result = "${todayRecord.time.toInt()}", unit = "min",
                imgResource = R.drawable.ic_time_home
            )

            VerticalDivider(
                modifier = Modifier
                    .padding(vertical = 15.dp, horizontal = 10.dp)
                    .fillMaxHeight()
                    .width(1.dp), // 선의 두께를 1.dp로 설정
                color = Color.Gray
            )

            TodayRecord(
                modifier = Modifier.weight(1f),
                dir = Arrangement.Center,
                iconModifier = iconModifier,
                result = "${todayRecord.pace}", unit = "pace",
                imgResource = R.drawable.ic_fire_home
            )

        }


    }
}

// 오늘 기록 Custom Composable
@Composable
fun TodayRecord(
    modifier: Modifier = Modifier,
    dir: Arrangement.Horizontal,
    iconModifier: Modifier = Modifier,
    result: String,
    unit: String,
    imgResource: Int
) {

    Row(
        modifier = modifier,
        horizontalArrangement = dir,
    ) {
        Image(
            painter = painterResource(id = imgResource),
            contentDescription = null,
            modifier = iconModifier
        )
        Column {
            Text(
                text = result,
                modifier = Modifier.align(Alignment.Start), // 왼쪽 정렬
                fontSize = 16.sp,
                fontFamily = ZokuFamily
            )
            Text(
                text = unit,
                modifier = Modifier.align(Alignment.End), // 오른쪽 정렬
                fontSize = 12.sp,
                fontFamily = ZokuFamily
            )
        }
    }
}

@Composable
fun HomeTitle(
    modifier: Modifier = Modifier,
    title: String,
    firstSelect: String,
    menu: Array<String>,
    selectType: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(firstSelect) }

    Row(
        modifier = Modifier
            .fillMaxWidth(), // Row가 전체 너비를 차지하도록 설정
        verticalAlignment = Alignment.CenterVertically // 수직 가운데 정렬
    ) {
        Text(
            text = title,
            color = Color.White,
            modifier = modifier
                .weight(1f) // Text는 전체 너비 중 1의 비율로 공간을 차지
                .align(Alignment.CenterVertically),
            fontFamily = ZokuFamily
        )

        Box(
            modifier = modifier
                .weight(1f) // DropdownMenu는 나머지 1의 비율로 공간을 차지
                .align(Alignment.CenterVertically),
            contentAlignment = Alignment.CenterEnd // Box 내에서 DropdownMenu를 끝에 배치
        ) {
            Row {
                Text(
                    text = selectedOption,  // 선택된 옵션 텍스트로 표시
                    color = Color.White,
                    modifier = Modifier
                        .clickable { expanded = !expanded },  // 클릭 시 드롭다운 열기/닫기
                    fontFamily = ZokuFamily
                )
                DropDownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    onItemSelected = { option ->
                        selectedOption = option // 선택된 옵션 업데이트
                        selectType("$title $selectedOption")
                    },
                    itemList = menu
                )


            }

        }
    }
}

@Composable
fun RunningDiary(
    modifier: Modifier = Modifier,
    totalWeekList: RunWeekList,
    listType: Int
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .height(200.dp)
            .background(BaseWhiteBackground)
    ) {
        when (listType) {
            0 -> if (totalWeekList.disList.isNotEmpty()) BarChartScreen(totalWeekList.disList)
            1 -> if (totalWeekList.timeList.isNotEmpty()) BarChartScreen(totalWeekList.timeList)
            2 -> if (totalWeekList.paceList.isNotEmpty()) BarChartScreen(totalWeekList.paceList)
        }
    }
}

@Composable
fun RunningRecord(modifier: Modifier = Modifier, distance: Double, time: Double, color: Color) {

    Row(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .fillMaxWidth()
    ) {
        Box(
            modifier = modifier
                .padding(0.dp)
                .weight(1f)
                .height(110.dp)
        ) {
            Column(
                Modifier
                    .background(color)
                    .fillMaxHeight()
                    .fillMaxWidth()
            ) {
                Spacer(
                    modifier = Modifier
                        .padding(top = 35.dp)
                )
                Text(
                    text = "$distance",
                    modifier = Modifier
                        .background(color)
                        .weight(1f)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = 26.sp,
                    fontFamily = ZokuFamily
                )
                Text(
                    text = "거리/km", modifier = Modifier
                        .background(color)
                        .weight(1f)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center, fontSize = 12.sp,
                    fontFamily = ZokuFamily
                )
                Spacer(modifier = Modifier.padding(top = 20.dp))
            }
        }

        Spacer(
            modifier = Modifier
                .padding(horizontal = 10.dp)
        )

        Box(
            modifier = modifier
                .weight(1f)
                .clip(RoundedCornerShape(16.dp))
                .height(110.dp)
        ) {
            Column(
                Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.padding(top = 35.dp))
                Text(
                    text = "${time.toInt()}",
                    modifier = Modifier
                        .background(color)
                        .weight(1f)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = 26.sp,
                    fontFamily = ZokuFamily
                )
                Text(
                    text = "분/min", modifier = Modifier
                        .background(color)
                        .weight(1f)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center, fontSize = 12.sp,
                    fontFamily = ZokuFamily
                )
                Spacer(modifier = Modifier.padding(top = 20.dp))
            }
        }
    }

}


@Composable
fun HomeFunctionButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    icon: Int,
    info: String,
    iconModifier: Modifier
) {
    Surface(
        modifier = modifier,
        color = Color.Transparent,
        onClick = onClick
    ) {
        Column {
            Spacer(modifier = Modifier.padding(top = 20.dp))
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = iconModifier
                    .align(Alignment.CenterHorizontally)
            )
            if (info.isNotEmpty()) {
                Text(
                    text = info,
                    modifier = Modifier
                        .wrapContentHeight()
                        .padding(top = 10.dp)
                        .fillMaxWidth(),
                    style = CustomTypo().mapleLight.copy(
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                )
            }
            Spacer(modifier = Modifier.padding(top = 20.dp))
        }
    }
}


@Composable
fun BarChartScreen(list: List<Double>) {

    val weekField = WeekFields.of(Locale.getDefault()).weekOfMonth()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "${LocalDate.now().monthValue} 월 ${LocalDate.now().get(weekField)}주",
            fontFamily = ZokuFamily
        )

        Spacer(modifier = Modifier.height(16.dp))

        // BarChart를 AndroidView로 사용
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                BarChart(context).apply {
                    description.isEnabled = false
                    setPinchZoom(false)
                    setDrawBarShadow(false)
                    setDrawGridBackground(false)

                    // X축 설정
                    xAxis.apply {
                        position = XAxis.XAxisPosition.BOTTOM
                        setDrawGridLines(false)
                        granularity = 1f // 1일 간격
                        labelCount = 7   // 7일 설정
                        valueFormatter = DayAxisFormatter()
                    }

                    // Y축 설정
                    axisLeft.apply {
                        axisMinimum = 0f  // Y축의 최소값을 0으로 설정
                        setDrawGridLines(false)
                        setDrawLabels(false)
                        axisLineColor = TRANSPARENT
                    }
                    axisRight.isEnabled = false // 오른쪽 Y축 비활성화
                    // 선택 리스너 추가 (막대 선택 시 Y값 표시 및 색상 변경)
                    setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                        override fun onValueSelected(e: Entry?, h: Highlight?) {
                            e?.let {
                                // 선택된 Y값을 로그로 출력
                                Log.d("BarChart", "Selected Y Value: ${e.y}")

                                // 강조된 항목 색상 변경
                                (data.getDataSetByIndex(
                                    h?.dataSetIndex ?: 0
                                ) as BarDataSet).highLightColor = BaseYellow.toArgb()
                                highlightValue(h)  // 선택한 항목 강조
                            }
                        }

                        override fun onNothingSelected() {
                            // 선택 해제 시 처리할 내용
                        }
                    })
                    // 랜덤 데이터 추가
                    val barData = getBarData1(list)
                    data = barData
                    legend.isEnabled = false
                    // 차트 업데이트
                    invalidate()
                }
            }
        )
    }
}

fun getBarData1(list: List<Double>): BarData {
    val values = ArrayList<BarEntry>()
    val days = arrayOf("월", "화", "수", "목", "금", "토", "일")

    for (i in days.indices) {
        values.add(BarEntry(i.toFloat(), if (list[i] == 0.0) 0.1f else list[i].toFloat()))
    }

    // 데이터셋 설정
    val set1 = BarDataSet(values, null).apply {
        color = PurpleGrey80.toArgb()
    }

    return BarData(set1).apply {
        barWidth = 0.6f
    }


}

// X축 레이블을 요일로 변환하는 Formatter
class DayAxisFormatter : ValueFormatter() {
    private val days = arrayOf("월", "화", "수", "목", "금", "토", "일")

    override fun getFormattedValue(value: Float): String {
        val index = value.toInt() % days.size
        return days[index]
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTodayDashBoard() {
    TodayDashBoard(todayRecord = RunToday(2.0, 2.0, 2.0))

}
