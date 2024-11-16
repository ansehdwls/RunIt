package com.zoku.ui.componenet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.zoku.ui.theme.Black

@Composable
fun ShowGroupInfo() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
    ) {

    }
}

@Composable
fun ShowGroupItem(
    modifier : Modifier = Modifier,
    image : Int,
    text : String
){
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Image(painter = painterResource(id = image), contentDescription = text)

    }
}


@Preview(showBackground = true)
@Composable
fun PreviewShowGroupInfo() {
    ShowGroupInfo()
}