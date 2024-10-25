package com.zoku.watch.screen


import android.graphics.drawable.Icon
import com.zoku.watch.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.tooling.preview.devices.WearDevices
import com.zoku.ui.BaseYellow
import java.sql.Time

@Composable
fun RunningScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        Spacer(modifier = Modifier.height(50.dp))
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            RunningButton(icon = Icons.Rounded.Stop)

            RunningButton(icon = Icons.Rounded.PlayArrow)
        }
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            StatusText()

        }


    }


}


@Composable
fun RunningButton(
    modifier: Modifier = Modifier,
    icon : ImageVector
) {
    Button(
        modifier = Modifier.size(ButtonDefaults.LargeButtonSize)

        ,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Black,
            contentColor = BaseYellow
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
@Composable
fun StatusText(modifier : Modifier = Modifier){
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            "6.39",
            color = BaseYellow)
        Spacer(Modifier.height(5.dp))
        Text("km",
            color = Color.White)

    }
}

@Preview(device = WearDevices.LARGE_ROUND, showSystemUi = true, apiLevel = 33)
@Composable
fun RunningPreview() {
    RunningScreen()
}

@Preview(device = WearDevices.LARGE_ROUND, showSystemUi = true, apiLevel = 33)
@Composable
fun StatusTextPreview() {
    StatusText()
}
