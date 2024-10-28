package com.zoku.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.zoku.ui.componenet.RecordMap

@Composable
fun RunHistoryDetailScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        item {
            // 기록 지도 생성
            RecordMap()
            RecordDetailInfo(startDestination = 1)
        }
    }
}