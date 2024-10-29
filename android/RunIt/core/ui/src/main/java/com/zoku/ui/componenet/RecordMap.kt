package com.zoku.ui.componenet

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.zoku.ui.R

@Composable
fun RecordMap(modifier: Modifier = Modifier) {

    Image(
        painter = painterResource(id = R.drawable.sample_map_history_icon),
        contentDescription = null,
        modifier = modifier
            .fillMaxWidth(),
        contentScale = ContentScale.Crop
    )
}