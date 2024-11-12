package com.zoku.home


import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.LargeValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.zoku.home.component.DropDownMenu
import com.zoku.network.model.response.RunToday
import com.zoku.network.model.response.RunWeekList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlin.random.Random

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

    val viewModel: InfoViewModel = hiltViewModel()
    val todayRecord by viewModel.todayRecord.collectAsState()
    val totalAllRecord by viewModel.totalAllRecord.collectAsState()
    val totalWeekRecord by viewModel.totalWeekRecord.collectAsState()
    val totalWeekList by viewModel.totalWeekList.collectAsState()

    viewModel.getRunToday()
    viewModel.getTotalRecord()
    viewModel.getWeekList()

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
                fontFamily = com.zoku.ui.ZokuFamily
            )
            TodayDashBoard(modifier.align(Alignment.CenterHorizontally), todayRecord)

            Spacer(modifier = modifier.height(20.dp))

            HomeTitle(
                modifier.padding(bottom = 5.dp),
                context.getString(R.string.running_diary),
                "거리",
                runningDiaryMenu
            ) { selectedOption ->
                if (selectedOption == context.getString(R.string.running_diary) + " 거리") listType = 0
                else if(selectedOption == context.getString(R.string.running_diary) + " 시간") listType = 1
                else listType = 2
            }

            RunningDiary(modifier.align(Alignment.CenterHorizontally), totalWeekList, listType)

            Spacer(modifier = modifier.height(20.dp))

            HomeTitle(modifier.padding(bottom = 5.dp), "러닝 기록", "전체", runningRecordMenu){
                 selectedOption ->
                    if (selectedOption == "러닝 기록 전체") isAllRecord = true
                    else isAllRecord = false

            }

            RunningRecord(modifier, if(isAllRecord) totalAllRecord.totalDistance else totalWeekRecord.weekDistance,
                if(isAllRecord) totalAllRecord.totalTime else totalWeekRecord.weekTime)

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

                HomeFunctionButton(modifier = buttonModifier
                    .background(Color.White), onClick = {
                    moveToHistory()
                }, icon = R.drawable.calendar_info_icon, "히스토리", iconModifier
                )

                Spacer(modifier = Modifier.width(20.dp))

                HomeFunctionButton(
                    modifier = buttonModifier
                        .background(com.zoku.ui.BaseYellow),
                    onClick = { moveToRunning() },
                    icon = R.drawable.run_info_icon,
                    "",
                    iconModifier
                        .height(60.dp)
                        .width(40.dp)
                )

                Spacer(modifier = Modifier.width(20.dp))

                HomeFunctionButton(modifier = buttonModifier
                    .background(Color.White), onClick = {
                    moveToRecordMode()
                }, icon = R.drawable.record_info_icon, "기록 갱신", iconModifier
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
            .background(com.zoku.ui.BaseWhiteBackground)
    ) {
        Row(
            Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .fillMaxHeight(), // Row가 Box의 전체 높이를 채우도록 설정
            verticalAlignment = Alignment.CenterVertically,
        ) {

            TodayRecord(
                modifier = Modifier.weight(1f),
                dir = Arrangement.End,
                iconModifier = iconModifier,
                result = "${todayRecord.distance}", unit = "Km",
                imgResource = R.drawable.run_home_icon
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
                result = "${todayRecord.time}", unit = "hr",
                imgResource = R.drawable.time_home_icon
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
                dir = Arrangement.Start,
                iconModifier = iconModifier,
                result = "${todayRecord.pace}", unit = "pace",
                imgResource = R.drawable.fire_home_icon
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
                fontFamily = com.zoku.ui.ZokuFamily
            )
            Text(
                text = unit,
                modifier = Modifier.align(Alignment.End), // 오른쪽 정렬
                fontSize = 12.sp,
                fontFamily = com.zoku.ui.ZokuFamily
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
    selectType : (String) -> Unit
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
            fontFamily = com.zoku.ui.ZokuFamily
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
                    fontFamily = com.zoku.ui.ZokuFamily
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
fun RunningDiary(modifier: Modifier = Modifier,
                 totalWeekList : RunWeekList,
                 listType : Int) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .height(200.dp)
            .background(com.zoku.ui.BaseWhiteBackground)
    ) {
        when(listType){
            0 -> if(totalWeekList.disList.isNotEmpty()) BarChartScreen(totalWeekList.disList)
            1 -> if(totalWeekList.timeList.isNotEmpty())BarChartScreen(totalWeekList.timeList)
            2 ->if(totalWeekList.paceList.isNotEmpty()) BarChartScreen(totalWeekList.paceList)
        }
    }
}

@Composable
fun RunningRecord(modifier: Modifier = Modifier, distance: Double, time: Double) {
    Row(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .padding(0.dp)
                .weight(1f)
                .height(110.dp)
                .background(com.zoku.ui.BaseWhiteBackground)
        ) {
            Column(
                Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.padding(top = 35.dp))
                Text(
                    text = "$distance",
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .background(com.zoku.ui.BaseWhiteBackground),
                    textAlign = TextAlign.Center,
                    fontSize = 26.sp,
                    fontFamily = com.zoku.ui.ZokuFamily
                )
                Text(
                    text = "거리/km", modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .background(com.zoku.ui.BaseWhiteBackground),
                    textAlign = TextAlign.Center, fontSize = 12.sp,
                    fontFamily = com.zoku.ui.ZokuFamily
                )
                Spacer(modifier = Modifier.padding(top = 20.dp))
            }
        }

        Spacer(
            modifier = modifier
                .padding(horizontal = 10.dp)
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(16.dp))
                .height(110.dp)
                .background(com.zoku.ui.BaseWhiteBackground)
        ) {
            Column(
                Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.padding(top = 35.dp))
                Text(
                    text = "$time",
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .background(com.zoku.ui.BaseWhiteBackground),
                    textAlign = TextAlign.Center,
                    fontSize = 26.sp,
                    fontFamily = com.zoku.ui.ZokuFamily
                )
                Text(
                    text = "시간/hr", modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .background(com.zoku.ui.BaseWhiteBackground),
                    textAlign = TextAlign.Center, fontSize = 12.sp,
                    fontFamily = com.zoku.ui.ZokuFamily
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
                    text = info, modifier = Modifier
                        .height(20.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center, fontSize = 12.sp,
                    fontFamily = com.zoku.ui.ZokuFamily
                )
            }
            Spacer(modifier = Modifier.padding(top = 20.dp))
        }
    }
}


@Composable
fun BarChartScreen( list : List<Double>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "10 월 1주", fontFamily = com.zoku.ui.ZokuFamily)

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
                        isEnabled = false
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
                                ) as BarDataSet).highLightColor = com.zoku.ui.BaseYellow.toArgb()
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

fun getBarData1(list : List<Double>): BarData {
    val values = ArrayList<BarEntry>()
    val days = arrayOf("월", "화", "수", "목", "금", "토", "일")

    for (i in days.indices) {
        values.add(BarEntry(i.toFloat(), list[i].toFloat()))
    }

    // 데이터셋 설정
    val set1 = BarDataSet(values, null).apply {
        color = com.zoku.ui.PurpleGrey80.toArgb()
    }

    return BarData(set1).apply {
        barWidth = 0.2f
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