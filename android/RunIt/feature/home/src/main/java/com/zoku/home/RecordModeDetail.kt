package com.zoku.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.zoku.home.viewmodel.RecordModeViewModel
import com.zoku.home.viewmodel.RunHistoryViewModel
import com.zoku.network.model.response.RunRecordDetail
import com.zoku.ui.componenet.RecordDetailInfo
import com.zoku.ui.componenet.RecordMap
import com.zoku.ui.theme.BaseGray
import com.zoku.ui.theme.BaseGrayBackground
import com.zoku.ui.theme.BaseYellow
import com.zoku.ui.theme.CustomTypo
import com.zoku.ui.theme.RoundButtonGray
import com.zoku.ui.theme.ZokuFamily
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordModeDetail(
    modifier: Modifier = Modifier,
    recordId: Int,
    moveToPractice: () -> Unit,
    moveToRunning: (recordDto: RunRecordDetail) -> Unit,
    onBackClick: () -> Unit
) {
    val viewModel: RunHistoryViewModel = hiltViewModel()
    val runRecord by viewModel.historyRunRecord.collectAsState()
    val recordViewModel: RecordModeViewModel = hiltViewModel()
    val routeList by recordViewModel.routeList.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    var mapShow by remember { mutableStateOf(true) }
    with(viewModel) {
        getRunRecordDetail(recordId)

    }
    with(recordViewModel) {
        getRouteList(recordId)
    }
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BaseGray)
    ) {
        // title
        RecordDetailTitle(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp)
        ) {
            showDialog = true
        }

        // 지도 경로 표시
        Box(
            modifier.weight(3f)
        ) {
            RecordMap(routeList = routeList, mapShow = mapShow)
        }

        // 세부 기록 표시
        Box(
            modifier.weight(7f)
        ) {
            RecordDetailInfo(
                runRecord = runRecord,
                moveToRunning = moveToRunning
            )
        }
    }
    if (showDialog) {
        ShowDialog(
            onClickText = {
                recordViewModel.updatePracticeRecord(recordId)
                moveToPractice()
            },
            onShowDialog = { change ->
                showDialog = change
            }
        )
    }


    BackHandler {
        coroutineScope.launch {
            mapShow = false
            onBackClick()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowDialog(
    modifier: Modifier = Modifier,
    onShowDialog: (Boolean) -> Unit,
    onClickText: () -> Unit
) {
    BasicAlertDialog(onDismissRequest = { onShowDialog(false) }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(onTap = { onShowDialog(false) })
                }
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.weight(0.45f))
            Surface(
                modifier = Modifier
                    .weight(0.2f),
                shape = RoundedCornerShape(8.dp),
//                    border = BorderStroke(0.1.dp, BaseYellow)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(BaseGrayBackground),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "연습 경로를 삭제하시겠습니까?",
                        style = CustomTypo().jalnan,
                        fontSize = 16.sp,
                        color = Color.White,
                        modifier = Modifier.padding(vertical = 32.dp)
                    )

                    HorizontalDivider(
                        color = RoundButtonGray,
                        thickness = 1.dp
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextButton(
                            onClick = { onShowDialog(false) },
                            modifier = Modifier
                                .weight(1f)
                                .padding(8.dp)
                        ) {
                            Text(
                                text = "취소",
                                style = CustomTypo().mapleLight,
                                color = Color.White
                            )
                        }

                        VerticalDivider(
                            modifier = Modifier
                                .width(1.dp),
                            color = RoundButtonGray
                        )

                        TextButton(
                            onClick = {
                                onShowDialog(false)
                                onClickText()

                            },
                            modifier = Modifier
                                .weight(1f)
                                .padding(8.dp)
                        ) {
                            Text(
                                text = "확인",
                                style = CustomTypo().mapleLight,
                                color = BaseYellow
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.weight(0.45f))
        }
    }
}


@Composable
fun RecordDetailTitle(modifier: Modifier = Modifier, updateRecord: () -> Unit) {
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
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 32.sp,
                    textAlign = TextAlign.Center,
                    fontFamily = ZokuFamily
                )

            }
        }

        IconButton(onClick = {
            updateRecord()
        }) {
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
