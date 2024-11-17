package com.zoku.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.zoku.home.viewmodel.RecordModeViewModel
import com.zoku.home.viewmodel.RunHistoryViewModel
import com.zoku.ui.componenet.RecordDetailInfo
import com.zoku.ui.componenet.RecordMap
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun RunHistoryDetailScreen(
    selectRecordId: Int,
    viewModel: RunHistoryViewModel,
    onBackClick: () -> Unit
) {

    val runRecord by viewModel.historyRunRecord.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val recordViewModel: RecordModeViewModel = hiltViewModel()
    val routeList by recordViewModel.routeList.collectAsState()

    recordViewModel.getRouteList(selectRecordId)
    viewModel.getRunRecordDetail(recordId = selectRecordId)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .verticalScroll(rememberScrollState()) // 스크롤 가능하도록 설정
    ) {
        // 기록 지도 생성
        Box(
            modifier = Modifier
                .weight(3f)
                .fillMaxWidth()
        ) {
            RecordMap(routeList = routeList)
        }
        Box(
            modifier = Modifier
                .weight(7f)
                .fillMaxWidth()
        ) {
            RecordDetailInfo(startDestination = 1, runRecord = runRecord)
        }
        BackHandler {
            coroutineScope.launch {
                delay(100L)
                onBackClick()
            }
        }
    }
}