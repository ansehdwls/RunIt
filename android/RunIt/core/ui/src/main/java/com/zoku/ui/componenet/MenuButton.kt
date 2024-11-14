package com.zoku.ui.componenet

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.zoku.ui.theme.ZokuFamily


@Composable
fun MenuButton(modifier : Modifier = Modifier, name : String, backgroundColor : Color, onClick: () -> Unit){
    Button(
        modifier = modifier
            .height(32.dp)
            .padding(horizontal = 16.dp)
        ,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = Color.Black
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(text = name,fontFamily = ZokuFamily)
    }
}
