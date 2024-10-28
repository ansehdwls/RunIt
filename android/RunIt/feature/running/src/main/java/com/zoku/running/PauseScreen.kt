package com.zoku.running

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.zoku.ui.BaseDarkBackground

@Composable
fun PauseScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BaseDarkBackground)
    ) {
        Text(
            text = "퍼즈 화면",
            color = Color.White
        )
    }
}