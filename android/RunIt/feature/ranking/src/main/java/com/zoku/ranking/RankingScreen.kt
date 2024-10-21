package com.zoku.ranking

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun RankingScreen(modifier: Modifier = Modifier) {
    Text(
        text = "랭킹 스크린",
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF3A3A3B))
    )
}