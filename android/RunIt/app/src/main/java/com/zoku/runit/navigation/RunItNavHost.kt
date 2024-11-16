package com.zoku.runit.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.zoku.login.LoginViewModel
import com.zoku.login.navigation.loginScreen
import com.zoku.navigatinon.expHistory
import com.zoku.navigatinon.homeScreen
import com.zoku.navigatinon.navigateToExpHistory
import com.zoku.navigatinon.navigateToHome
import com.zoku.navigatinon.navigateToRecordModeDetail
import com.zoku.navigatinon.navigateToRecordModeScreen
import com.zoku.navigatinon.navigateToRunHistory
import com.zoku.navigatinon.recordDetail
import com.zoku.navigatinon.recordMode
import com.zoku.navigatinon.runHistory
import com.zoku.network.model.response.RunRecordDetail
import com.zoku.running.navigation.navigateToRunning
import com.zoku.running.navigation.runningScreen
import com.zoku.ui.base.ClientDataViewModel
import com.zoku.ui.model.PhoneWatchConnection
import com.zoku.util.ScreenDestinations
import timber.log.Timber

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun RunItMainNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onHomeScreen: () -> Unit,
    onStartWearableActivityClick: (String) -> Unit,
    onPauseWearableActivityClick: (String) -> Unit,
    onResumeWearableActivityClick: (String) -> Unit,
    onStopWearableActivityClick: (String) -> Unit,
    startDestination: String = ScreenDestinations.login.route,
    viewModel: ClientDataViewModel,
    sendHeartBeat: () -> Unit
) {
    var isUserLoggedIn by remember { mutableStateOf(false) }
    val loginViewModel: LoginViewModel = hiltViewModel()
    val phoneWatchData by viewModel.phoneWatchData.collectAsState()

    Timber.tag("RunItNavHost").d("폰 데이터 $phoneWatchData")
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        this.homeScreen(
            moveToHistory = { navController.navigateToRunHistory() },
            moveToRecordMode = {
                navController.navigateToRecordModeScreen()
            },
            moveToRunning = {
                onStartWearableActivityClick(PhoneWatchConnection.START_RUNNING.route)
                viewModel.updateMessageType(PhoneWatchConnection.START_RUNNING)
                navController.navigateToRunning(
                    recordDetail =
                    RunRecordDetail(0, 0.0, 0, "", "", emptyList())
                )
            },
            moveToExpHistory = { navController.navigateToExpHistory() },
            phoneWatchConnection = phoneWatchData,
            sendHeartBeat = sendHeartBeat
        )
        this.runHistory()
        this.recordMode(
            moveToDetail = { recordId ->
                navController.navigateToRecordModeDetail(recordId)
            },
            onBackButtonClick = {
                navController.popBackStack()
            }
        )
        this.loginScreen(onLoginSuccess = {
            navController.navigate(ScreenDestinations.home.route)
            onHomeScreen()
        }, viewModel = loginViewModel)
        this.recordDetail(
            moveToPractice = {
                navController.popBackStack()
            },
            moveToRunning = { runRecordDto ->
                navController.navigateToRunning(runRecordDto)
            }
        )
        this.runningScreen(
            modifier = modifier,
            onPauseWearableActivityClick = onPauseWearableActivityClick,
            onResumeWearableActivityClick = onResumeWearableActivityClick,
            onStopWearableActivityClick = onStopWearableActivityClick,
            moveToHome = {
                navController.navigate(ScreenDestinations.home.route) {
                    // 모든 기존 스택을 제거
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true // 루트 화면까지 모두 제거
                    }
                    launchSingleTop = true // 중복된 홈 화면 방지
                }
            },
            viewModel
        )
//        this.runningResultScreen(modifier = modifier)

        this.expHistory()

    }

    // 로그인 성공 시 홈 화면으로 이동
    LaunchedEffect(isUserLoggedIn) {
        if (isUserLoggedIn) {
            navController.navigateToHome()
        }
    }

}


