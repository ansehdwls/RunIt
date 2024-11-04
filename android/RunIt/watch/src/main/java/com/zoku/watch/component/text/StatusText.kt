package com.zoku.watch.component.text

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Text
import com.zoku.ui.BaseYellow

@Composable
fun StatusText(modifier: Modifier = Modifier, value: String, type: String) {
    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            value,
            color = BaseYellow
        )
        Spacer(Modifier.height(5.dp))
        Text(
            type,
            color = Color.White
        )
    }
}