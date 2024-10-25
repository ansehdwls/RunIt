package com.zoku.ui.componenet

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.zoku.ui.R

@Composable
fun RobotoText(
    modifier: Modifier = Modifier,
    text: String,
    fontSize: TextUnit = 10.sp,
    color: Color = Color.White,
    style: String = "Medium",
) {
    Text(
        modifier = modifier,
        text = text,
        style = TextStyle(
            fontFamily = FontFamily(
                Font(if (style == "Bold") R.font.roboto_bold else R.font.roboto_medium)
            ),
            fontSize = fontSize
        ),
        color = color,
    )
}