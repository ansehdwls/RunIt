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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.zoku.ui.model.GroupInfoData
import com.zoku.ui.theme.Black
import com.zoku.ui.theme.CustomTypo

@Composable
fun ShowGroupInfo() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Black),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
            ,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    modifier = Modifier.padding(top = 30.dp),
                    text = "리그 정보",
                    style = CustomTypo().mapleBold.copy(
                        fontSize = 30.sp,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                )
            }
            items(GroupInfoData.DEFAULT) { data ->
                Spacer(modifier = Modifier.height(20.dp))
                ShowGroupItem(image = data.image, text = data.name)
            }
        }
    }
}

@Composable
fun ShowGroupItem(
    modifier: Modifier = Modifier.wrapContentWidth(),
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