package com.zoku.running

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.PixelCopy
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.zoku.ui.BaseDarkBackground
import com.zoku.ui.BaseYellow
import com.zoku.ui.CustomTypo
import com.zoku.ui.componenet.KakaoMapView
import com.zoku.ui.componenet.RecordDetailInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun RunningResultScreen(
    modifier: Modifier = Modifier,
    runningViewModel: RunningViewModel
) {
    val context = LocalContext.current
    val view = LocalView.current
    val totalRunningList by runningViewModel.totalRunningList.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BaseDarkBackground)
    ) {

        Spacer(modifier = Modifier.weight(0.02f))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.4f)
        ) {
            KakaoMapView(
                totalLocationList = totalRunningList,
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.7f)
        ) {
            RecordDetailInfo(startDestination = 1)
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.1f),
            contentAlignment = Alignment.TopCenter
        ) {
            Button(
                onClick = {
//                    runningViewModel.submitTestSum(TestSumRequest(1, 3))

//                    captureRequested = true
                },

                colors = ButtonDefaults.buttonColors(BaseYellow),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth(0.3f)
            ) {
                Text(
                    text = "확인",
                    style = CustomTypo().jalnan,
                    color = Color.Black
                )
            }
        }
    }
}



@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Preview
@Composable
fun RunningResultPreview() {
    com.zoku.ui.RunItTheme {
        RunningResultScreen(runningViewModel = hiltViewModel<RunningViewModel>())
    }
}