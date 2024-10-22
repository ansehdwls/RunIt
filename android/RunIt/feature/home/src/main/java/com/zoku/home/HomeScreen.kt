package com.zoku.home

import androidx.compose.foundation.background


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Color
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Text(
        text = "홈 스크린",
        color = androidx.compose.ui.graphics.Color.Black,
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF3A3A3B))
    )


}
