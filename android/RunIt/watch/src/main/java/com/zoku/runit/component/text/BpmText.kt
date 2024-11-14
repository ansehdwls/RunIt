package com.zoku.runit.component.text

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text
import androidx.wear.tooling.preview.devices.WearDevices
import com.zoku.ui.theme.CustomTypo
import com.zoku.runit.util.formatBpm

@Composable
fun BpmText(
    modifier: Modifier = Modifier,
    heartRate: Double?
) {
    Text(
        text = formatBpm(heartRate = heartRate),
        style = CustomTypo().jalnan.copy(fontSize = 20.sp)
    )
}


@Preview(device = WearDevices.LARGE_ROUND, showSystemUi = true, apiLevel = 33)
@Composable
fun BpmTextPreview() {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BpmText(heartRate = 80.0)
    }

}