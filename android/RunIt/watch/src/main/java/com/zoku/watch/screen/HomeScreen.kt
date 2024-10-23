package com.zoku.watch.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.tooling.preview.devices.WearDevices
import com.zoku.watch.component.StartButton
import com.zoku.watch.ui.MainScreen

@Composable
fun HomeScreen(modifier : Modifier){
    StartButton(modifier) {  }
}


@Preview(device = WearDevices.SMALL_ROUND, showSystemUi = true, apiLevel = 33)
@Composable
fun homePreview() {
    HomeScreen(Modifier.fillMaxSize())
}