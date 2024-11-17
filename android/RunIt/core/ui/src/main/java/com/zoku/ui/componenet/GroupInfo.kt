package com.zoku.ui.componenet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zoku.ui.R
import com.zoku.ui.theme.Black
import com.zoku.ui.theme.CustomTypo

@Composable
fun ShowGroupInfo() {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
            .verticalScroll(scrollState)
        ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp),
            text = "리그 정보",
            style = CustomTypo().mapleBold.copy(
                fontSize = 30.sp,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        )
        Spacer(modifier = Modifier.height(50.dp))
        ShowGroupItem(
            modifier = Modifier.wrapContentWidth(),
            image = R.drawable.ic_egg,
            text = "알"
        )

        Spacer(modifier = Modifier.height(20.dp))
        ShowGroupItem(
            modifier = Modifier.wrapContentWidth(),
            image = R.drawable.ic_sloth,
            text = "나무늘보"
        )

        Spacer(modifier = Modifier.height(20.dp))
        ShowGroupItem(
            modifier = Modifier.wrapContentWidth(),
            image = R.drawable.ic_turtle,
            text = "거북이"
        )
        Spacer(modifier = Modifier.height(20.dp))
        ShowGroupItem(
            modifier = Modifier.wrapContentWidth(),
            image = R.drawable.ic_rabbit,
            text = "토끼"
        )
        Spacer(modifier = Modifier.height(20.dp))
        ShowGroupItem(
            modifier = Modifier.wrapContentWidth(),
            image = R.drawable.ic_horse,
            text = "말"
        )
        Spacer(modifier = Modifier.height(20.dp))
        ShowGroupItem(
            modifier = Modifier.wrapContentWidth(),
            image = R.drawable.ic_cheetah,
            text = "치타"
        )
        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun ShowGroupItem(
    modifier: Modifier = Modifier,
    image: Int,
    text: String
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier,
            painter = painterResource(id = image), contentDescription = text
        )
        Spacer(modifier = Modifier.width(100.dp))

        Text(
            modifier = Modifier,
            text = text,
            style = CustomTypo().mapleLight.copy(
                color = Color.White,
                fontSize = 20.sp
            )
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewShowGroupInfo() {
    ShowGroupInfo()
}