package com.zoku.running

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.zoku.ui.BaseDarkBackground
import com.zoku.ui.BaseYellow
import com.zoku.ui.CustomTypo
import com.zoku.ui.componenet.KakaoMapView
import com.zoku.ui.componenet.RecordDetailInfo

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
                onCaptureComplete = { file ->
                    //TODO 이거 테스트용임 지워야함 , 누가 이 주석을 본다면 지우라고 해주세요;;
                    isMapCompleted = true
//                    runningViewModel.postRunningRecord(
//                        captureFile = file,
//                        onSuccess = {
//                            Toast.makeText(context, "통신 성공", Toast.LENGTH_SHORT).show()
//                            isMapCompleted = true
//                        },
//                        onFail = { message ->
//                            Toast.makeText(context, "API 실패 ${message}", Toast.LENGTH_SHORT)
//                                .show()
//                        }
//                    )
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
                    .clickable {
                        showDialog = false
                    },
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.weight(0.4f))
                Surface(modifier = Modifier.weight(0.2f)) {
                    Text(
                        text = "경로를 저장하시겠습니까?",
                        style = CustomTypo().jalnan,
                        fontSize = 16.sp,
                        color = Color.White,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }
        }
//        AlertDialog(
//            onDismissRequest = { showDialog = false },
//            shape = RoundedCornerShape(16.dp),
//            containerColor = BaseDarkBackground,
//            text = {
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth(),
//                    contentAlignment = Alignment.Center,
//                ) {
//                    Text(
//                        text = "경로를 저장하시겠습니까?",
//                        style = CustomTypo().jalnan,
//                        fontSize = 16.sp,
//                        color = Color.White,
//                        modifier = Modifier.padding(top = 16.dp)
//                    )
//                }
//            },
//            confirmButton = {
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(8.dp),
//                    horizontalArrangement = Arrangement.SpaceEvenly
//                ) {
//                    TextButton(
//                        onClick = { showDialog = false },
//                        modifier = Modifier
//                            .weight(1f)
//                            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
//                    ) {
//                        Text(
//                            text = "취소",
//                            style = CustomTypo().mapleLight,
//                            color = Color.White
//                        )
//                    }
//                    Spacer(modifier = Modifier.width(8.dp))
//                    TextButton(
//                        onClick = {
//                            showDialog = false
//                            moveToHome()
//                        },
//                        modifier = Modifier
//                            .weight(1f)
//                            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
//                    ) {
//                        Text(
//                            text = "확인",
//                            style = CustomTypo().mapleLight,
//                            color = BaseYellow
//                        )
//                    }
//                }
//            }
//        )
    }
}


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Preview
@Composable
fun RunningResultPreview() {
    com.zoku.ui.RunItTheme {
        RunningResultScreen(runningViewModel = hiltViewModel<RunningViewModel>(),
            moveToHome = {})
    }
}