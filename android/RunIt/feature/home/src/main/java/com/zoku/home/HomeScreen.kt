package com.zoku.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zoku.ui.BaseWhiteBackground
import com.zoku.ui.componenet.MenuButton


@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(com.zoku.ui.BaseDarkBackground)
    ) {
        Spacer(modifier = Modifier.height(36.dp))

        Row(
            modifier = Modifier
                .wrapContentWidth()
                .height(36.dp)
                .align(Alignment.CenterHorizontally)
                .background(
                    color = com.zoku.ui.BaseGray,
                    shape = RoundedCornerShape(8.dp)
                ),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(8.dp))

            MenuButton(
                name = "정보",
                backgroundColor = BaseWhiteBackground
            ) {

            }

            Spacer(modifier = Modifier.width(8.dp))

            MenuButton(
                name = "랭킹",
                backgroundColor = BaseWhiteBackground
            ) {

            }

            Spacer(modifier = Modifier.width(8.dp))
        }
        Spacer(modifier = Modifier.height(12.dp))

    }
}