package com.zoku.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zoku.home.viewmodel.RankViewModel
import com.zoku.network.model.response.ExpDataHistory
import com.zoku.ui.theme.BaseDarkBackground
import com.zoku.ui.theme.BaseGray
import com.zoku.ui.theme.CustomTypo
import com.zoku.ui.theme.RunItTheme
import com.zoku.ui.theme.ZokuFamily

@Composable
fun ExpHistory(modifier: Modifier = Modifier) {
    val rankViewModel: RankViewModel = hiltViewModel()

    LaunchedEffect(Unit) {
        rankViewModel.getAllExpHistory()
    }
    val list by rankViewModel.allExpHistoryDataList.collectAsStateWithLifecycle()
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(BaseGray),
    ) {
        if (list.isEmpty()) {
            Text(
                text = "획득한 경험치가 없습니다",
                modifier
                    .fillMaxSize()
                    .padding(top = 50.dp),
                style =
                    CustomTypo().jalnan.copy(
                        textAlign = TextAlign.Center,
                        color = Color.White,
                    ),
            )
        } else {
            Text(
                text = "획득경험치",
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                fontSize = 30.sp,
                fontFamily = ZokuFamily,
                textAlign = TextAlign.Center,
                color = Color.White,
            )
        }
        LazyColumn(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            items(list) { item ->
                ExpHistoryItem(item = item)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun ExpHistoryItem(item: ExpDataHistory) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(100.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(BaseDarkBackground),
        // Row 간 간격 조정
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            modifier =
                Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .padding(start = 10.dp),
            painter =
                if (item.activity == "거리"
                ) {
                    painterResource(id = R.drawable.ic_exp_run)
                } else {
                    painterResource(id = R.drawable.ic_exp_attend)
                },
            contentDescription = "",
        )

        Text(
            text = item.createAt,
            modifier =
                Modifier
                    .padding(start = 8.dp)
                    .weight(2f),
            textAlign = TextAlign.Start,
            fontFamily = ZokuFamily,
            fontSize = 16.sp,
            color = Color.White,
        )
        Text(
            text = item.activity,
            modifier =
                Modifier
                    .weight(1f)
                    .padding(start = 8.dp),
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            fontFamily = ZokuFamily,
            color = Color.White,
        )
        Text(
            text = "+${item.changed}xp",
            modifier =
                Modifier
                    .padding(end = 10.dp)
                    .weight(1f),
            textAlign = TextAlign.End,
            fontFamily = ZokuFamily,
            fontSize = 16.sp,
            color = Color.Yellow,
        )
    }
}

@Preview
@Composable
fun preview() {
    RunItTheme {
        ExpHistoryItem(
            item =
                ExpDataHistory(
                    "달리기",
                    100,
                    "2024-11-15",
                ),
        )
    }
}
