package com.zoku.home

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.Locale


@Composable
fun RunHistoryScreen(modifier: Modifier = Modifier) {

    var selectHistory by remember {
        mutableStateOf(false)
    }
    var selectedDay by remember { mutableStateOf(LocalDate.now()) }
    var challengeList by remember {
        mutableStateOf(
            mutableListOf(
                true,
                false,
                true,
                false,
                true,
                false,
                true
            )
        )
    }
    // 주간 시작일을 고정하여 현재 주간의 날짜를 유지
    val weekStart = selectedDay.with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 1)
    val weekDates = getWeekDates(weekStart)
    val monthName = weekDates.first().month.getDisplayName(TextStyle.SHORT, Locale.getDefault())
    val baseModifier = Modifier
        .padding(horizontal = 10.dp)
        .fillMaxWidth()

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(com.zoku.ui.BaseGray)
    ) {
        // ex) < 10 월 >
        RunHistoryTitle(baseModifier
            ,monthName,
            preWeek = {
                selectedDay = selectedDay.minusWeeks(1)
            },
            postWeek = {
                selectedDay = selectedDay.plusWeeks(1)
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
                Log.d("확인", "RunHistoryScreen: $day")
                selectHistory = false
            },
            challengeList = challengeList
        )
        if (!selectHistory) {
            RunHistoryRecordListScreen(selectedDay = selectedDay,
                onClick = { selectItem ->
                    selectHistory = true

                })
        }
        else {
            RunHistoryDetailScreen()
        }
    }
}

@Composable
fun RunHistoryTitle(modifier: Modifier = Modifier,
                    monthName  : String,
                    preWeek : () -> Unit,
                    postWeek : () -> Unit
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
            onClick = {preWeek()},
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
                fontFamily = com.zoku.ui.ZokuFamily
            )
        }

        RunHistoryTitleBtn(
            modifier = titleBtnModifier,
            onClick = {postWeek()},
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
        color = com.zoku.ui.BaseGray,

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
                fontFamily = com.zoku.ui.ZokuFamily
            )
        }
    }
}

@Composable
fun WeeklyDateView(
    modifier: Modifier,
    weekDates: List<LocalDate>,
    onDateClick: (LocalDate) -> Unit,
    challengeList: MutableList<Boolean>
) {
    Row(modifier = modifier) {
        for ((index, date) in weekDates.withIndex()) {
            Surface(
                onClick = { onDateClick(date)},
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
                    .padding(top = 10.dp),
                color = com.zoku.ui.BaseGray
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = R.drawable.fire_history_icon),
                        contentDescription = null,
                        alignment = Alignment.Center,
                        modifier = Modifier
                            .width(25.dp)
                            .height(25.dp)
                            .alpha(if (challengeList[index]) 1f else 0f)
                    )
                    Text(
                        text = date.dayOfMonth.toString(),
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(top = 5.dp),
                        fontFamily = com.zoku.ui.ZokuFamily
                    )
                }
            }
        }
    }
}

fun getWeekDates( today : LocalDate = LocalDate.now()): List<LocalDate> {

    val weekStart = today.with(WeekFields.of(Locale.getDefault()).firstDayOfWeek)
    return (0..6).map { weekStart.plusDays(it.toLong()) }
}