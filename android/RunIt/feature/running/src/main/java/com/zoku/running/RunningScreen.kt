package com.zoku.running

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color


@Composable
fun RunningScreen(modifier: Modifier = Modifier) {
    Box(modifier = Modifier
        .background(Color.Red)
        .fillMaxSize()) {
        Text(
            text = "러닝 스크린 확인",
            modifier = Modifier.align(Alignment.Center)
        )
    }
}