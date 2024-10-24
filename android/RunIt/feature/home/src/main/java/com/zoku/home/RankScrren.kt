package com.zoku.home

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun RankScreen(modifier: Modifier = Modifier){
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ){
        Text(text = "랭킹")
    }
}