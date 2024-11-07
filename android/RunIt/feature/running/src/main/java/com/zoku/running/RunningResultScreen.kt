package com.zoku.running

import android.os.Build
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.zoku.ui.BaseDarkBackground
import com.zoku.ui.BaseYellow
import com.zoku.ui.CustomTypo
import com.zoku.ui.componenet.KakaoMapView
import com.zoku.ui.componenet.RecordDetailInfo
import java.io.File

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun RunningResultScreen(
    modifier: Modifier = Modifier,
    runningViewModel: RunningViewModel
) {
    val context = LocalContext.current
    val totalRunningList by runningViewModel.totalRunningList.collectAsState()
    var captureFile by remember { mutableStateOf<File?>(null) }
    var isMapCompleted by remember { mutableStateOf(false) }

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
                isResult = true,
                onCaptureComplete = { file ->
                    captureFile = file
                    isMapCompleted = true
                }
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
                    captureFile?.let { file ->
                        runningViewModel.postRunningRecord(
                            captureFile = file,
                            onSuccess = {
                                Toast.makeText(context, "통신 성공", Toast.LENGTH_SHORT).show()
                            },
                            onFail = { message ->
                                Toast.makeText(context, "API 실패 ${message}", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        )
                    }
                },

                colors = ButtonDefaults.buttonColors(BaseYellow),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth(0.3f),
                enabled = isMapCompleted
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