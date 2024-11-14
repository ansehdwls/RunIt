package com.zoku.home

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.zoku.ui.componenet.MenuButton
import com.zoku.ui.model.PhoneWatchConnection
import com.zoku.ui.theme.BaseGray
import com.zoku.ui.theme.BaseWhiteBackground
import com.zoku.ui.theme.BaseYellow


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    moveToHistory: () -> Unit,
    moveToRecordMode: () -> Unit,
    moveToRunning: () -> Unit,
    moveToExpHistory: () -> Unit,
    phoneWatchConnection: PhoneWatchConnection,

    ) {
    var isInfo by remember { mutableStateOf(true) }
    var isHistory by remember {
        mutableStateOf(false)
    }

    if (phoneWatchConnection == PhoneWatchConnection.SEND_BPM) {
        moveToRunning()
    }
    BackOnPressed()

    val baseModifier = Modifier
        .fillMaxSize()
        .background(BaseGray)
    Column(
        modifier = if (isInfo) baseModifier
            .verticalScroll(rememberScrollState())
        else baseModifier
    ) {
        Spacer(modifier = Modifier.height(15.dp))

        Row(
            modifier = Modifier
                .wrapContentWidth()
                .height(36.dp)
                .align(Alignment.CenterHorizontally)
                .background(
                    color = BaseGray,
                    shape = RoundedCornerShape(8.dp)
                ),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(8.dp))

            MenuButton(
                name = "정보",
                backgroundColor = if (isInfo) BaseYellow else BaseWhiteBackground
            ) {
                isInfo = true
            }

            Spacer(modifier = Modifier.width(8.dp))

            MenuButton(
                name = "랭킹",
                backgroundColor = if (isInfo) BaseWhiteBackground else BaseYellow
            ) {
                isInfo = false
            }

            Spacer(modifier = Modifier.width(8.dp))
        }
        Spacer(modifier = Modifier.height(12.dp))

        // InfoScreen 표시 조건
        if (isInfo) {
            InfoScreen(
                modifier = modifier
                    .background(BaseGray)
                    .padding(horizontal = 10.dp),
                moveToHistory = moveToHistory,
                moveToRecordMode = moveToRecordMode,
                moveToRunning = moveToRunning
            )
        } else {
            RankScreen(moveToExpHistory = moveToExpHistory)
        }

    }
}

@Composable
fun BackOnPressed() {
    val context = LocalContext.current
    var backPressedState by remember { mutableStateOf(true) }
    var backPressedTime = 0L

    BackHandler(enabled = backPressedState) {
        if (System.currentTimeMillis() - backPressedTime <= 1000L) {
            (context as Activity).finish()
        } else {
            backPressedState = true
            Toast.makeText(context, "한 번 더 누르시면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show()
        }
        backPressedTime = System.currentTimeMillis()
    }
}