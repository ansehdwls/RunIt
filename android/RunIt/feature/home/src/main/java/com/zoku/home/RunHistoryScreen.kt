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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.zoku.home.viewmodel.RunHistoryViewModel
import com.zoku.network.model.response.WeekList
import com.zoku.ui.theme.BaseGray
import com.zoku.ui.theme.ZokuFamily
import timber.log.Timber
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.TextStyle
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields
import java.util.Locale


@Composable
fun RunHistoryScreen(
    modifier: Modifier = Modifier,
    viewModel : RunHistoryViewModel = hiltViewModel()
) {

    var selectHistory by remember {
        mutableStateOf(false)
    }
    var selectedDay by remember {
        mutableStateOf(
            LocalDate.now(ZoneId.systemDefault())
        )
    }
    Timber.tag("RunHistoryScreen").d("선택 날짜 $selectedDay")
    var selectRecordId by remember {
        mutableStateOf(0)
    }
    // 주간 시작일을 고정하여 현재 주간의 날짜를 유지
    val weekDates = getWeekDates(selectedDay)
    Timber.tag("RunHistoryScreen").d("weekDates $weekDates")
    // 표기 되는 월은 그 주의 월요일 기준
    val monthName = weekDates.first().month.getDisplayName(TextStyle.SHORT, Locale.getDefault())

    val historyWeekList by viewModel.historyWeekList.collectAsState()
    val isFirst by viewModel.isFirst.collectAsState()
    // 초기 1회만 실행
    if (!isFirst) {
        viewModel.getWeekList(selectedDay.toString())
    }

    // 선택한 요일이 일주일 중 몇번째 인덱스 인지
    val selectedIndex = weekDates.indexOfFirst { it == selectedDay }
    Timber.tag("RunHistoryScreen").d("selectedIndex $selectedIndex")
    val baseModifier = Modifier
        .padding(horizontal = 10.dp)
        .fillMaxWidth()

    if (historyWeekList.isNotEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .background(BaseGray)
        ) {
            // ex) < 10 월 >
            RunHistoryTitle(baseModifier, monthName,
                preWeek = {
                    selectedDay = selectedDay.minusWeeks(1)
                    viewModel.getWeekList(selectedDay.toString())
                },
                postWeek = {
                    selectedDay = selectedDay.plusWeeks(1)
                    viewModel.getWeekList(selectedDay.toString())
                })

            // 월 화 수 목 금 토 일
            WeekView(baseModifier)

            // ex) 불 icon / 날짜
            WeeklyDateView(
                baseModifier,
                weekDates = weekDates,
                onDateClick = { day ->
                    // 클릭한 날짜를 그대로 selectedDay로 설정하여 주간이 변경되지 않도록 함
                    selectedDay = day// 클릭된 날짜 업데이트
                    selectHistory = false
                },
                challengeList = historyWeekList
            )
            if (!selectHistory) {
                RunHistoryRecordListScreen(
                    selectedDay = selectedDay,
                    list = historyWeekList[selectedIndex],
                    onClick = { selectItem ->
                        selectHistory = true
                        selectRecordId = selectItem
                    })
            } else {
                RunHistoryDetailScreen(selectRecordId, viewModel){
                    selectHistory = false
                }
            }
        }
    }
}

@Composable
fun RunHistoryTitle(
    modifier: Modifier = Modifier,
    monthName: String,
    preWeek: () -> Unit,
    postWeek: () -> Unit
) {

    Row(
        modifier = modifier
            .padding(top = 30.dp)
            .height(60.dp)
    ) {
        Spacer(modifier = Modifier.weight(1f))

        val titleBtnModifier = Modifier
            .fillMaxHeight()  // 부모의 높이를 채워 가운데 정렬
            .wrapContentWidth()
            .align(Alignment.CenterVertically)
            .padding(horizontal = 10.dp)

        val titleBtnImageModifier = Modifier
            .width(20.dp)
            .height(20.dp)
            .align(Alignment.CenterVertically)

        RunHistoryTitleBtn(
            modifier = titleBtnModifier,
            onClick = { preWeek() },
            icon = R.drawable.pre_run_history_icon,
            iconModifier = titleBtnImageModifier
        )

        Box(
            modifier = Modifier
                .fillMaxHeight()  // 부모의 높이를 채워 가운데 정렬
                .wrapContentWidth()
                .align(Alignment.CenterVertically),
        ) {
            Text(
                text = monthName,
                color = Color.White,
                fontSize = 30.sp,
                modifier = Modifier.align(Alignment.Center),
                fontFamily = ZokuFamily
            )
        }

        RunHistoryTitleBtn(
            modifier = titleBtnModifier,
            onClick = { postWeek() },
            icon = R.drawable.next_run_history_icon,
            iconModifier = titleBtnImageModifier
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun RunHistoryTitleBtn(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    icon: Int,
    iconModifier: Modifier
) {
    Surface(
        modifier = modifier,
        onClick = onClick,
        color = BaseGray,

        ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = iconModifier
        )
    }
}

@Composable
fun WeekView(modifier: Modifier) {
    val week = arrayOf("월", "화", "수", "목", "금", "토", "일")

    Row(
        modifier = modifier
    ) {
        for (day in week) {
            Text(
                text = day, modifier = Modifier
                    .weight(1f),
                color = Color.White,
                textAlign = TextAlign.Center,
                fontFamily = ZokuFamily
            )
        }
    }
}

@Composable
fun WeeklyDateView(
    modifier: Modifier,
    weekDates: List<LocalDate>,
    onDateClick: (LocalDate) -> Unit,
    challengeList: List<List<WeekList>>
) {
    Row(modifier = modifier) {
        for ((index, date) in weekDates.withIndex()) {
            Surface(
                onClick = { onDateClick(date) },
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
                    .padding(top = 10.dp),
                color = BaseGray
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = if (challengeList[index].isNotEmpty()) R.drawable.fire_history_icon else R.drawable.fire_off_history_icon),
                        contentDescription = null,
                        alignment = Alignment.Center,
                        modifier = Modifier
                            .width(25.dp)
                            .height(25.dp)
                    )
                    Text(
                        text = date.dayOfMonth.toString(),
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(top = 5.dp),
                        fontFamily = ZokuFamily
                    )
                }
            }
        }
    }
}

fun getWeekDates(today: LocalDate = LocalDate.now()): List<LocalDate> {

    val weekStart = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    Timber.tag("RunHistoryScreen").d("weekStart $weekStart")
    return (0..6).map { weekStart.plusDays(it.toLong()) }
}