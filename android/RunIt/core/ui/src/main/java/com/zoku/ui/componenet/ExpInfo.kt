package com.zoku.ui.componenet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
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
import com.zoku.ui.theme.CustomTypo


@Composable
fun ShowExpInfo(
    modifier: Modifier = Modifier,
    pagerState : PagerState
) {
    HorizontalPager(state = pagerState) { page ->

        ShowExpInfoItem(image = , text = )

    }
}


@Composable
fun ShowExpInfoItem(
    modifier: Modifier = Modifier,
    image: Int,
    text: String
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = image),
            contentDescription = "expInfoImage"
        )
        Spacer(modifier = Modifier.height(50.dp))
        Text(
            text = text,
            style = CustomTypo().mapleLight.copy(
                color = Color.White,
                textAlign = TextAlign.Center,
                fontSize = 30.sp
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
    ShowExpInfoItem(
        modifier = modifier,
        image = R.drawable.ic_exp_attend,
        text = "경험치 +1xp"
    )
}
