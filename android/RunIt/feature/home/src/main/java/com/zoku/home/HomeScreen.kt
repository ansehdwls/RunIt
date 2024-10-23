package com.zoku.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Column(
            modifier = modifier

        ) {
            // 기본 패딩
            Spacer(modifier = modifier.height(160.dp))

            Text(
                text = "오늘",
                color = androidx.compose.ui.graphics.Color.White,
                modifier = modifier
            )
            TodayDashBoard(modifier.align(Alignment.CenterHorizontally))

            Spacer(modifier = modifier.height(20.dp))

            HomeTitle(modifier,"러닝 일지","거리",1)

            RunningDiary(modifier.align(Alignment.CenterHorizontally))

            Spacer(modifier = modifier.height(20.dp))

            HomeTitle(modifier,"러닝 기록","전체",2)

            RunningRecord(modifier)
        }
    }

}

@Composable
fun TodayDashBoard(modifier: Modifier = Modifier){

    val iconModifier = Modifier
        .height(30.dp)
        .width(30.dp)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(com.zoku.ui.BaseWhiteBackground)
    ) {
        Row {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {


                Image(
                    painter = painterResource(id = R.drawable.run_home_icon),
                    contentDescription = "Run Icon",
                    modifier = iconModifier
                )
                Column {
                    Text(
                        text = "100",
                    )
                    Text(
                        text = "Km",
                    )
                }
            }

            Spacer(modifier = modifier.padding(start = 10.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {


                Image(
                    painter = painterResource(id = R.drawable.time_home_icon),
                    contentDescription = "Time Icon",
                    modifier = iconModifier
                        .height(40.dp)
                        .width(40.dp)
                )

                Column {
                    Text(
                        text = "16.9",
                    )
                    Text(
                        text = "hr",
                    )
                }
            }
            Spacer(modifier = modifier.padding(start =10.dp))
            Row (
                verticalAlignment = Alignment.CenterVertically
            ){
                Image(
                    painter = painterResource(id = R.drawable.fire_home_icon),
                    contentDescription = "Run Icon",
                    modifier = iconModifier
                )
                Column {
                    Text(
                        text = "1'20''",
                    )
                    Text(
                        text = "pace",
                    )
                }
            }

        }


    }
}

@Composable
fun HomeTitle(modifier: Modifier = Modifier, title: String, firstSelect: String, option: Int){
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(firstSelect) }

    Row(
        modifier = Modifier
            .fillMaxWidth(), // Row가 전체 너비를 차지하도록 설정
        verticalAlignment = Alignment.CenterVertically // 수직 가운데 정렬
    ) {
        Text(
            text = title,
            color = androidx.compose.ui.graphics.Color.White,
            modifier = modifier
                .weight(1f) // Text는 전체 너비 중 1의 비율로 공간을 차지
                .align(Alignment.CenterVertically)
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
                    color = androidx.compose.ui.graphics.Color.White,
                    modifier = Modifier
                        .clickable { expanded = !expanded }  // 클릭 시 드롭다운 열기/닫기
                )
                when(option){
                    1 -> RunningMDiaryMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        onItemSelected = { option ->
                            selectedOption = option // 선택된 옵션 업데이트
                        }
                    )
                    2->RunningRecordMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        onItemSelected = { option ->
                            selectedOption = option // 선택된 옵션 업데이트
                        }
                    )
                }


            }

        }
    }
}

@Composable
fun RunningDiary(modifier: Modifier = Modifier)
{
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(com.zoku.ui.BaseWhiteBackground)
    )
}

@Composable
fun RunningMDiaryMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,   // 메뉴가 닫힐 때 호출되는 콜백
    onItemSelected: (String) -> Unit  // 아이템 선택 시 호출되는 콜백
){
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest, // 메뉴 외부를 클릭하면 닫히도록 처리
        modifier = Modifier.background(com.zoku.ui.BaseDarkBackground)
    ) {
        DropdownMenuItem(
            text = { Text("거리" , color = Color.White) },
            onClick = {
                onItemSelected("거리") // 아이템이 선택되었을 때 콜백 호출
                onDismissRequest()     // 메뉴 닫기
            },
            modifier = Modifier.background(com.zoku.ui.BaseDarkBackground)
        )
        DropdownMenuItem(
            text = { Text("시간" , color = Color.White) },
            onClick = {
                onItemSelected("시간")
                onDismissRequest()
            },
            modifier = Modifier.background(com.zoku.ui.BaseDarkBackground)
        )
        DropdownMenuItem(
            text = { Text("페이스" , color = Color.White) },
            onClick = {
                onItemSelected("페이스")
                onDismissRequest()
            },
            modifier = Modifier.background(com.zoku.ui.BaseDarkBackground)
        )
    }
}

@Composable
fun RunningRecordMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,   // 메뉴가 닫힐 때 호출되는 콜백
    onItemSelected: (String) -> Unit  // 아이템 선택 시 호출되는 콜백
){
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest, // 메뉴 외부를 클릭하면 닫히도록 처리
        modifier = Modifier.background(com.zoku.ui.BaseDarkBackground)
    ) {
        DropdownMenuItem(
            text = { Text("전체" , color = Color.White) },
            onClick = {
                onItemSelected("전체") // 아이템이 선택되었을 때 콜백 호출
                onDismissRequest()     // 메뉴 닫기
            },
            modifier = Modifier.background(com.zoku.ui.BaseDarkBackground)
        )
        DropdownMenuItem(
            text = { Text("일주일" , color = Color.White) },
            onClick = {
                onItemSelected("일주일")
                onDismissRequest()
            },
            modifier = Modifier.background(com.zoku.ui.BaseDarkBackground)
        )
    }
}

@Composable
fun RunningRecord(modifier: Modifier = Modifier){
    Row(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .padding(0.dp)
                .weight(1f)
                .height(60.dp)
                .background(com.zoku.ui.BaseWhiteBackground)
        )

        Spacer(modifier = modifier
            .padding(start = 20.dp, end = 20.dp))

        Box(
            modifier = Modifier
                .weight(1f)
                .height(60.dp)
                .background(com.zoku.ui.BaseWhiteBackground)
        )
    }

}