package com.zoku.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.zoku.ui.componenet.RecordDetailInfo
import com.zoku.ui.componenet.RecordMap

@Composable
fun RunHistoryDetailScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .verticalScroll(rememberScrollState()) // 스크롤 가능하도록 설정
    ) {
        // 기록 지도 생성
        Box(modifier = Modifier.weight(3f).fillMaxWidth()) {
            RecordMap()
        }
        Box(modifier = Modifier.weight(7f).fillMaxWidth()) {
            RecordDetailInfo(startDestination = 1)
        }
    }
}