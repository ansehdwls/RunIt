package com.zoku.ui

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

val ZokuFamily = FontFamily(
    Font(R.font.maple_story_bold, FontWeight.Bold),
    Font(R.font.maple_story_light, FontWeight.Light),
    Font(R.font.jalnan, FontWeight.ExtraBold)
)


data class CustomTypo(
    val mapleBold: TextStyle = TextStyle(
        fontFamily = ZokuFamily,
        fontWeight = FontWeight.Bold
    ),

    val mapleLight: TextStyle = TextStyle(
        fontFamily = ZokuFamily,
        fontWeight = FontWeight.Light
    ),

    val jalnan: TextStyle = TextStyle(
        fontFamily = ZokuFamily,
        fontWeight = FontWeight.ExtraBold
    )
)
