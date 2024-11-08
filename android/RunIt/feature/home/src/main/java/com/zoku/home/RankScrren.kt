package com.zoku.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.zoku.network.model.response.AttendanceDay
import com.zoku.network.model.response.GroupMember
import com.zoku.ui.CustomTypo
import java.time.LocalDate
import kotlin.math.ceil
import kotlin.math.floor

@Composable
fun RankScreen(modifier: Modifier = Modifier, moveToExpHistory: () -> Unit) {
    val rankMenu = arrayOf("종합 순위", "페이스 순위", "거리 순위")
    val rankViewModel: RankViewModel = hiltViewModel()

    // 이번주 출석 현황
    val attendanceList by rankViewModel.attendanceWeekInfo.collectAsState()

    // 이번주 획득 경험치
    val weekExp by rankViewModel.currentExp.collectAsState()

    // 그룹 리스트
    val groupList by rankViewModel.groupInfo.collectAsState()

    with(rankViewModel) {
        getAllExpHistory()
        getWeekExp()
        getGroupList()
        getAttendance()
    }
    Column(
        modifier = modifier
            .padding(horizontal = 10.dp)
            .fillMaxSize()
    ) {
        RankingInfo(moveToExpHistory, weekExp, attendanceList)

        HomeTitle(modifier.padding(top = 10.dp, bottom = 5.dp), "그룹 내 순위", "종합 순위", rankMenu)

        if (groupList.isNotEmpty()) UserRanking(groupList)
    }
}

@Composable
fun RankingInfo(
    moveToExpHistory: () -> Unit,
    weekExp: Int,
    attendanceList: List<AttendanceDay>
) {
    val baseModifier = Modifier.fillMaxWidth()
    Box(
        modifier = baseModifier
            .clip(RoundedCornerShape(15.dp))
            .background(com.zoku.ui.BaseDarkBackground)
    )
    {
        Column(
            modifier = baseModifier
        ) {
            InfoIconButton("리그 정보", onClick = {})

            UserProfile()
            if (attendanceList.isNotEmpty()) {
                DailyCheckView(
                    baseModifier
                        .padding(top = 10.dp),
                    attendanceList
                )
            }

            ExpView(baseModifier, moveToExpHistory, weekExp)

            InfoIconButton("경험치 획득 방법", onClick = {})
        }
    }
}

@Composable
fun UserProfile() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Column {
            RankText(
                text = "환영합니다! 오늘도 화이팅 :)",
                fontSize = 12.sp
            )
            Row(
                modifier = Modifier.padding(top = 20.dp)
            ) {
                Text(
                    text = "콱씨",
                    style = CustomTypo().mapleBold.copy(
                        color = Color.White,
                        fontSize = 25.sp
                    )
                )
                RankText(
                    text = "님",
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.Bottom)
                )
            }
        }
        Text(
            text = "현재",
            modifier = Modifier
                .weight(1f)
                .align(Alignment.Bottom),
            style = CustomTypo().jalnan.copy(
                fontSize = 12.sp,
                color = Color.White,
                textAlign = TextAlign.End,
            )
        )
        Text(
            text = "거북이", modifier = Modifier
                .align(Alignment.Bottom),
            textAlign = TextAlign.End,
            fontSize = 12.sp,
            color = Color.Green,
            fontFamily = com.zoku.ui.ZokuFamily
        )
        Image(
            painter = painterResource(id = R.drawable.tutle_rank_icon),
            contentDescription = null,
            modifier = Modifier
                .width(60.dp)
                .height(60.dp)
                .align(Alignment.Bottom),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun InfoIconButton(title: String, onClick: () -> Unit) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            fontSize = 10.sp,
            fontFamily = com.zoku.ui.ZokuFamily,
            color = Color.White,
            textAlign = TextAlign.End
        )
        IconButton(
            onClick = { onClick() }) {
            Image(
                painter = painterResource(id = R.drawable.info_rank_icon),
                contentDescription = null,
                modifier = Modifier
                    .width(13.dp)
                    .height(13.dp),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
fun DailyCheckView(
    modifier: Modifier = Modifier,
    attendanceList: List<AttendanceDay>
) {
    val today = LocalDate.now().dayOfWeek.value - 1
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        for (i in 0..today) {
            DailyCheck(
                Modifier.weight(1f),
                day = attendanceList[i].day,
                type = if (attendanceList[i].attended) 1 else 2
            )
        }
        for (i in today+1 until 7) {
            DailyCheck(
                Modifier.weight(1f), day = attendanceList[i].day, type = 0
            )
        }
    }
}

@Composable
fun DailyCheck(modifier: Modifier = Modifier, type: Int = 0, day: String) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(
                id = when (type) {
                    0 -> R.drawable.none_rank_icon
                    1 -> R.drawable.success_rank_icon
                    else -> R.drawable.fail_rank_icon
                }
            ), contentDescription = null,
            modifier = Modifier
                .width(25.dp)
                .height(35.dp)
                .padding(bottom = 10.dp)
        )
        RankText(text = day)
    }
}

@Composable
fun ExpView(
    modifier: Modifier = Modifier,
    moveToExpHistory: () -> Unit,
    weekExp: Int
) {
    Row(
        modifier = modifier
            .padding(vertical = 5.dp)
    ) {

        Column(
            modifier = Modifier.padding(horizontal = 10.dp)
        ) {

            Row {
                Surface(
                    color = Color.Transparent,
                    onClick = {
                        moveToExpHistory()
                    }
                ) {
                    RankText(text = "획득경험치")
                }
                Image(
                    painter = painterResource(id = R.drawable.next_run_history_icon),
                    contentDescription = null,
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                        .align(Alignment.CenterVertically)
                )
            }

            RankText(text = "$weekExp xp")

        }

        Spacer(modifier = Modifier.weight(1f))

        Column(
            modifier = Modifier.padding(horizontal = 10.dp)
        ) {
            RankText(text = "현재순위")
            RankText(text = "2위")
        }
    }
}

@Composable
fun UserRanking(groupList: List<GroupMember>) {

    val groupSize = groupList.size
    val promoteCount = ceil(groupSize* 0.3).toInt()
    val demoteCount = floor(groupSize * 0.3).toInt()


    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
    ) {

        // 승급 인원
        items(promoteCount) { index ->
            val item = groupList[index]
            UserRankingProfile(
                Modifier
                    .fillMaxWidth(),
                item,
                index
            )
        }

        // 승급 표시
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.up_rank_icon),
                        contentDescription = null,
                        modifier = Modifier
                            .width(20.dp)
                            .height(20.dp)
                    )
                }
                Text(
                    text = "승급",
                    style = CustomTypo().jalnan.copy(
                        color = Color.Green, fontSize = 24.sp,
                    )
                )
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.up_rank_icon),
                        contentDescription = null,
                        modifier = Modifier
                            .width(20.dp)
                            .height(20.dp)
                    )
                }

            }
        }
        // 3명 이상 이면 적용
        if (demoteCount > 0) {

            // 유지 인원
            items(groupSize - promoteCount - demoteCount) { index ->
                val item = groupList[promoteCount + index]
                UserRankingProfile(
                    Modifier
                        .fillMaxWidth(),
                    item,
                    promoteCount + index
                )
            }
        }
        if (groupSize - promoteCount - demoteCount > 0) {
            // 강등
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .align(Alignment.CenterVertically),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.down_rank_icon),
                            contentDescription = null,
                            modifier = Modifier
                                .width(20.dp)
                                .height(20.dp)
                        )
                    }
                    Text(
                        text = "강등",
                        style = CustomTypo().jalnan.copy(
                            color = Color.Red,
                            fontSize = 24.sp,
                        )
                    )
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.down_rank_icon),
                            contentDescription = null,
                            modifier = Modifier
                                .width(20.dp)
                                .height(20.dp)
                        )
                    }

                }
            }

            // 강등 인원
            items(demoteCount) { index ->
                val item = groupList[groupSize - demoteCount + index]
                UserRankingProfile(
                    Modifier
                        .fillMaxWidth(),
                    item,
                    groupSize - demoteCount + index
                )
            }


        }
    }
}

@Composable
fun UserRankingProfile(
    modifier: Modifier = Modifier,
    item: GroupMember, index: Int
) {
    val baseModifier = Modifier.fillMaxHeight()
    Surface(
        color = com.zoku.ui.BaseDarkBackground,
        modifier = modifier
            .height(80.dp)
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(com.zoku.ui.BaseDarkBackground)
    ) {
        Row {
            Box(
                modifier = baseModifier
                    .padding(start = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                RankText(
                    text = "${index + 1}",
                    fontSize = 24.sp,
                    color = Color.Black
                )
            }
            Image(
                painter = rememberAsyncImagePainter(item.imageUrl),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = baseModifier
                    .padding(horizontal = 20.dp, vertical = 14.dp)
                    .clip(RoundedCornerShape(50.dp))

            )
            Box(
                modifier = baseModifier.weight(1f),
                contentAlignment = Alignment.CenterStart
            ) {
                RankText(text = item.userName, fontSize = 20.sp, color = Color.Black)
            }
            Box(
                modifier = baseModifier,
                contentAlignment = Alignment.CenterEnd
            ) {
                Image(
                    painter = painterResource(id = R.drawable.run_info_icon),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = baseModifier
                        .padding(horizontal = 15.dp, vertical = 20.dp)
                )
            }
            Box(
                modifier = baseModifier
                    .padding(end = 10.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                RankText(
                    text = "${item.exp}xp", fontSize = 24.sp
                )
            }
        }
    }
}

@Composable
fun RankText(
    modifier: Modifier = Modifier,
    text: String,
    fontSize: TextUnit = 15.sp,
    color: Color = Color.White
) {
    Text(
        text = text,
        style = CustomTypo().jalnan.copy(
            fontSize = fontSize,
            textAlign = TextAlign.Center,
            color = color
        ),
    )
}