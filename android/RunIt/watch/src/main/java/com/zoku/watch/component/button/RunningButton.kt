package com.zoku.watch.component.button

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.OutlinedButton
import com.zoku.ui.BaseYellow

@Composable
fun RunningButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    size: Dp
) {
    OutlinedButton(
        modifier = Modifier.size(size),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Black,
            contentColor = BaseYellow
        ),
        border = ButtonDefaults.outlinedButtonBorder(
            borderColor = BaseYellow
        ),
        onClick = {}
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "button trigger",
            modifier = modifier
        )
    }
}

