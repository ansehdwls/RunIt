package com.zoku.running

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun RunningResultScreen() {
    Column {
        Text("텍스트 확인", color = Color.White)
    }
}

@Preview
@Composable
fun RunningResultPreview() {
    com.zoku.ui.RunItTheme {
        RunningResultScreen()
    }
}