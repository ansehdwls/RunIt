package com.zoku.ui.componenet

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun RoundRunButton(
    modifier: Modifier = Modifier,
    containerColor: Color,
    resourceId: Int,
    resourceColor: Color,
) {
    Button(
        onClick = { /*TODO: Add functionality*/ },
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(containerColor = containerColor),
        modifier = modifier.size(100.dp)
    ) {
        Image(
            painter = painterResource(id = resourceId),
            contentDescription = "Button",
            modifier = Modifier.size(44.dp),
            contentScale = ContentScale.Fit,
            colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(resourceColor)
        )
    }
}