package com.zoku.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zoku.home.component.DropDownMenu

@Composable
fun RankScreen(modifier: Modifier = Modifier){
    val rankMenu = arrayOf("종합 순위", "페이스 순위", "거리 순위")
    Column(
        modifier = modifier
            .padding(horizontal = 10.dp)
            .fillMaxSize()
    ){
        RankingInfo()

        HomeTitle(modifier.padding(top = 10.dp, bottom = 5.dp), "그룹 내 순위", "종합 순위", rankMenu)

        UserRanking()
    }
}

@Composable
fun RankingInfo(){
    val baseModifier = Modifier.fillMaxWidth()
    Box(modifier = baseModifier
        .clip(RoundedCornerShape(15.dp))
        .background(com.zoku.ui.BaseDarkBackground)
    )
    {
        Column(
            modifier = baseModifier
        ) {
            InfoIconButton("리그 정보", onClick = {})

            UserProfile()

            DailyCheckView(
                baseModifier
                    .padding(top = 10.dp)
            )

            ExpView(baseModifier)

            InfoIconButton("경험치 획득 방법", onClick = {})
        }
    }
}

@Composable
fun UserProfile(){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Column {
            Text(text = "환영합니다! 오늘도 화이팅 :)",
                color = Color.White,
                fontSize = 12.sp
            )
            Row(
                modifier = Modifier.padding(top = 20.dp)
            ) {
                Text(text = "콱씨",
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 25.sp)
                Text(text = "님",
                    color = Color.White,
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.Bottom))
            }
        }
        Text(text = "현재",
            modifier = Modifier
                .weight(1f)
                .align(Alignment.Bottom),
            textAlign = TextAlign.End,
            fontSize = 12.sp,
            color = Color.White)
        Text(text = "거북이", modifier = Modifier
            .align(Alignment.Bottom),
            textAlign = TextAlign.End,
            fontSize = 12.sp,
            color = Color.Green)
        Image(painter = painterResource(id = R.drawable.tutle_rank_icon),
            contentDescription = null,
            modifier = Modifier
                .width(60.dp)
                .height(60.dp)
                .align(Alignment.Bottom),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun InfoIconButton(title: String, onClick : ()->Unit){

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f),
            color = Color.White
            , fontSize = 10.sp)
        IconButton(
            onClick = {onClick()}) {
            Image(painter = painterResource(id = R.drawable.info_rank_icon),
                contentDescription = null,
                modifier = Modifier
                    .width(13.dp)
                    .height(13.dp),
                contentScale = ContentScale.Crop)
        }
    }
}

@Composable
fun DailyCheckView(modifier: Modifier = Modifier){
    val list = arrayListOf("월","화","수","목","금","토",'일')
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        for(i in 0..6 ){
            DailyCheck(Modifier.weight(1f) ,day = list[i].toString(),type = when(i){
                0-> 2
                1-> 1
                2-> 1
                3 -> 2
                4 -> 1
                else -> 0
            }
            )
        }
    }
}

@Composable
fun DailyCheck(modifier: Modifier = Modifier, type: Int = 0,day : String){
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(
                id = when (type) {
                    0 -> R.drawable.none_rank_icon
                    1 -> R.drawable.success_rank_icon
                    else -> R.drawable.fail_rank_icon
                }
        )
            , contentDescription = null,
            modifier = Modifier
                .width(25.dp)
                .height(35.dp)
                .padding(bottom = 10.dp)
        )
        Text(text = day, color = Color.White, textAlign = TextAlign.Center)
    }
}

@Composable
fun ExpView(modifier: Modifier = Modifier){
    Row (
        modifier = modifier
            .padding(vertical = 5.dp)
    ){

            Column(
                modifier = Modifier.padding(horizontal = 10.dp)
            ) {

                    Row {
                        Text(text = "획득경험치", color = Color.White, textAlign = TextAlign.Center)
                        Image(
                            painter = painterResource(id = R.drawable.next_run_history_icon),
                            contentDescription = null,
                            modifier = Modifier
                                .width(20.dp)
                                .height(20.dp)
                                .align(Alignment.CenterVertically)
                        )
                    }

                    Text(text = "34 xp", color = Color.White, textAlign = TextAlign.Center)

            }
        
        Spacer(modifier = Modifier.weight(1f))
        
        Column(
            modifier = Modifier.padding(horizontal = 10.dp)
        ) {
            Text(text = "현재순위" ,color = Color.White, textAlign = TextAlign.Center)
            Text(text = "2위", color = Color.White, textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun UserRanking(){
    LazyColumn  (
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
    ){
        items(3){
            UserRankingProfile(
                Modifier
                    .fillMaxWidth())
        }

        item(1){
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(vertical = 20.dp)
            ) {
                Box(modifier = Modifier.weight(1f)
                    .align(Alignment.CenterVertically),
                    contentAlignment = Alignment.CenterStart){
                    Image(painter = painterResource(id = R.drawable.up_rank_icon), contentDescription = null,
                        modifier = Modifier.width(20.dp).height(20.dp))
                }
                Text(text = "승급", color = Color.Green, fontSize = 24.sp)
                Box(modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterEnd){
                    Image(painter = painterResource(id = R.drawable.up_rank_icon), contentDescription = null,
                        modifier = Modifier.width(20.dp).height(20.dp))
                }

            }
        }


        items(2){
            UserRankingProfile(
                Modifier
                    .fillMaxWidth())
        }

        item(2){
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(vertical = 20.dp)
            ) {
                Box(modifier = Modifier.weight(1f)
                    .align(Alignment.CenterVertically),
                    contentAlignment = Alignment.CenterStart){
                    Image(painter = painterResource(id = R.drawable.down_rank_icon), contentDescription = null,
                        modifier = Modifier.width(20.dp).height(20.dp))
                }
                Text(text = "강등", color = Color.Red, fontSize = 24.sp)
                Box(modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterEnd){
                    Image(painter = painterResource(id = R.drawable.down_rank_icon), contentDescription = null,
                        modifier = Modifier.width(20.dp).height(20.dp))
                }

            }
        }


        items(3){
            UserRankingProfile(
                Modifier
                    .fillMaxWidth())
        }
    }
}

@Composable
fun UserRankingProfile(modifier: Modifier =Modifier){
    val baseModifier = Modifier.fillMaxHeight()
    Surface(modifier = modifier
        .height(80.dp)
        .padding(vertical = 8.dp)
        .clip(RoundedCornerShape(10.dp))){
        Row {
            Box(modifier = baseModifier
                .padding(start = 10.dp),
                contentAlignment = Alignment.Center){
                Text(text = "1",
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center
                )
            }
            Image(painter = painterResource(id = R.drawable.profile_example_rank_icon),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                 modifier = baseModifier
                     .padding(horizontal = 10.dp, vertical = 20.dp))
            Box(modifier = baseModifier.weight(1f),
                contentAlignment = Alignment.CenterStart) {
                Text(text = "진평동 슈마허", fontSize = 20.sp)
            }
            Box(modifier = baseModifier,
                contentAlignment = Alignment.CenterEnd) {
                Image(
                    painter = painterResource(id = R.drawable.run_info_icon),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = baseModifier
                        .padding(horizontal = 15.dp, vertical = 20.dp)
                )
            }
            Box(modifier = baseModifier
                .padding(end = 10.dp),
                contentAlignment = Alignment.CenterEnd) {
                Text(
                    text = "100xp", fontSize = 24.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}