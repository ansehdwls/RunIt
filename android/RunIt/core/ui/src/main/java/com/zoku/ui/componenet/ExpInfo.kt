package com.zoku.ui.componenet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.zoku.ui.model.ExpInfoData
import com.zoku.ui.theme.BaseGrayBackground
import com.zoku.ui.theme.CustomTypo


@Composable
fun ShowExpInfo(
    modifier: Modifier = Modifier,
    pagerState: PagerState = rememberPagerState(
        pageCount = { ExpInfoData.DEFAULT.size },
        initialPage = 0
    ),
    onOkClick : () -> Unit
) {
    val list = ExpInfoData.DEFAULT
    Column(
        modifier = Modifier.wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(state = pagerState) { page ->
            ShowExpInfoItem(image = list[page].image, text = list[page].text)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 30.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            repeat(list.size) { page ->
                val color = if (pagerState.currentPage == page) Color.White else Color.Gray
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .padding(4.dp)
                        .background(color = color, shape = CircleShape)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            modifier = Modifier.wrapContentWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = BaseGrayBackground,
                contentColor = BaseGrayBackground,
            ),
            onClick = { onOkClick() }) {
            Text(
                text = "확인",
                style = CustomTypo().mapleLight.copy(
                    color = Color.White,
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center
                )
            )
        }
        Spacer(modifier = Modifier.height(30.dp))
    }

}


@Composable
fun ShowExpInfoItem(
    modifier: Modifier = Modifier,
    image: Int,
    text: String
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(50.dp))
        Image(
            modifier = Modifier
                .height(250.dp)
                .width(250.dp),
            painter = painterResource(id = image),
            contentDescription = "expInfoImage"
        )
        Spacer(modifier = Modifier.height(50.dp))
        Text(
            text = text,
            style = CustomTypo().mapleLight.copy(
                color = Color.White,
                textAlign = TextAlign.Center,
                fontSize = 35.sp
            )
        )

    }
}


@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun PreviewShowExpInfoItem(
    modifier: Modifier = Modifier
        .background(Color.Black)
) {
    val list = ExpInfoData.DEFAULT
    val pagerState = rememberPagerState(
        pageCount = { list.size }
    )
    ShowExpInfo(pagerState = pagerState){

    }
}
