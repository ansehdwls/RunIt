package com.zoku.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ExpHistory(){

    LazyColumn {
        item(1){
            Text(text = "획득경험치")
        }

        items(3){
            Box(modifier = Modifier.fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(com.zoku.ui.BaseDarkBackground)){
                Row {
                    Text(text = "2024-10-28")
                    Text(text = "1KM 달성!")
                    Text(text = "+50xp")
                }
            }
        }
    }
}