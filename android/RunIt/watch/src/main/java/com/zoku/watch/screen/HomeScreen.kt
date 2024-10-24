package com.zoku.watch.screen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.tooling.preview.devices.WearDevices
import com.zoku.watch.component.PagerScreen
import com.zoku.watch.component.StartButton

@Composable
fun HomeScreen(modifier: Modifier) {

    val items: List<@Composable () -> Unit> =
        listOf({ StartButton(modifier) { } }, { StartButton(modifier) { } })
    val pagerState = rememberPagerState(pageCount = { items.size })
    PagerScreen(
        modifier = modifier,
        state = pagerState,
        items = items
    )
}


@Composable
fun userInfoRow(modifier : Modifier){

    Row(
        modifier = modifier
    ) {


    }


}



@Preview(device = WearDevices.SMALL_ROUND, showSystemUi = true, apiLevel = 33)
@Composable
fun homePreview() {
    HomeScreen(Modifier.fillMaxSize())
}

