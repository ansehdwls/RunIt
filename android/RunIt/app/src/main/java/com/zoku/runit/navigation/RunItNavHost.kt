package com.zoku.runit.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.zoku.running.navigation.navigateToRunning
import com.zoku.running.navigation.runningScreen
import com.zoku.util.ScreenDestinations

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun RunItMainNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onStartWearableActivityClick : () -> Unit,
    startDestination: String = ScreenDestinations.login.route
) {

    var isUserLoggedIn by remember { mutableStateOf(false) }
    val loginViewModel: LoginViewModel = hiltViewModel()
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {

        this.homeScreen(
            moveToHistory = { navController.navigateToRunHistory() },
            moveToRecordMode = {
                navController.navigateToRecordModeScreen() },
            moveToRunning = {
                onStartWearableActivityClick()
                navController.navigateToRunning() },
            moveToExpHistory = {navController.navigateToExpHistory()}
        )
        this.runHistory()
        this.recordMode(
            moveToDetail = { navController.navigateToRecordModeDetail() }
        )
        this.loginScreen(onLoginSuccess = {
            // 로그인 성공 시, 상태 업데이트
//            isUserLoggedIn = true
            navController.navigate("home")
        },viewModel = loginViewModel)
        this.recordDetail()
        this.runningScreen(modifier = modifier)
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


