package com.zoku.running

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.zoku.network.model.response.PaceRecord
import com.zoku.network.model.response.RunRecordDetail
import com.zoku.running.util.getIso8601TimeString
import com.zoku.ui.componenet.KakaoMapView
import com.zoku.ui.componenet.RecordDetailInfo
import com.zoku.ui.theme.BaseGrayBackground
import com.zoku.ui.theme.BaseYellow
import com.zoku.ui.theme.CustomTypo
import com.zoku.ui.theme.RoundButtonGray
import com.zoku.ui.theme.RunItTheme

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun RunningResultScreen(
    modifier: Modifier = Modifier,
    runningViewModel: RunningViewModel,
    moveToHome: () -> Unit
) {
    val context = LocalContext.current
    val totalRunningList by runningViewModel.totalRunningList.collectAsState()
    var isMapCompleted by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BaseGrayBackground)
    ) {

        Spacer(modifier = Modifier.weight(0.02f))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.4f)
        ) {
            KakaoMapView(
                totalLocationList = totalRunningList,
                onCaptureComplete = { file ->
                    if (runningViewModel.totalPaceList.isEmpty()) {
                        Toast.makeText(context, "최소 100m는 달려야 경험치가 저장됩니다!", Toast.LENGTH_LONG)
                            .show()
                    } else {
                        runningViewModel.postRunningRecord(
                            captureFile = file,
                            onSuccess = { exp, isAttend ->
                                Toast.makeText(context, "경험치가 ${exp} 증가했습니다!", Toast.LENGTH_SHORT)
                                    .show()
                                isMapCompleted = true
                            },
                            onFail = { message ->
                                Toast.makeText(context, "API 실패 ${message}", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        )
                    }
                },
                initialLocation = runningViewModel.getInitialLocationData(),
                isResult = true
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.7f),
            contentAlignment = Alignment.Center
        ) {
            RecordDetailInfo(
                startDestination = 1,
                runRecord = RunRecordDetail(
                    startTime = getIso8601TimeString(System.currentTimeMillis()),
                    endTime = getIso8601TimeString(System.currentTimeMillis()),
                    paceList = listOf(PaceRecord(10, 10), PaceRecord(20, 20))
                )
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.1f),
            contentAlignment = Alignment.TopCenter
        ) {
            Button(
                onClick = {
                    showDialog = true
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

    if (showDialog) {
        BasicAlertDialog(onDismissRequest = { showDialog = false }) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = { showDialog = false })
                    }
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.weight(0.4f))
                Surface(
                    modifier = Modifier
                        .weight(0.2f),
                    shape = RoundedCornerShape(8.dp),
//                    border = BorderStroke(0.1.dp, BaseYellow)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(BaseGrayBackground),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "경로를 저장하시겠습니까?",
                            style = CustomTypo().jalnan,
                            fontSize = 16.sp,
                            color = Color.White,
                            modifier = Modifier.padding(vertical = 32.dp)
                        )

                        HorizontalDivider(
                            color = RoundButtonGray,
                            thickness = 1.dp
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            TextButton(
                                onClick = { showDialog = false },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = "취소",
                                    style = CustomTypo().mapleLight,
                                    color = Color.White
                                )
                            }

                            VerticalDivider(
                                modifier = Modifier
                                    .width(1.dp),
                                color = RoundButtonGray
                            )

                            TextButton(
                                onClick = {
                                    showDialog = false
                                    moveToHome()
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = "확인",
                                    style = CustomTypo().mapleLight,
                                    color = BaseYellow
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.weight(0.4f))
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Preview
@Composable
fun RunningResultPreview() {
    RunItTheme {
        RunningResultScreen(runningViewModel = hiltViewModel<RunningViewModel>(),
            moveToHome = {})
    }
}