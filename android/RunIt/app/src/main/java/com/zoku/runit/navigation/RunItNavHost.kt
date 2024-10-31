package com.zoku.runit.navigation

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
import com.zoku.navigatinon.homeScreen
import com.zoku.navigatinon.navigateToHome
import com.zoku.navigatinon.navigateToRecordModeDetail
import com.zoku.navigatinon.navigateToRecordModeScreen
import com.zoku.navigatinon.navigateToRunHistory
import com.zoku.navigatinon.recordDetail
import com.zoku.navigatinon.recordMode
import com.zoku.navigatinon.runHistory
import com.zoku.running.navigation.runningResultScreen
import com.zoku.running.navigation.runningScreen
import com.zoku.util.ScreenDestinations

@Composable
fun RunItMainNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
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
            moveToRecordMode = { navController.navigateToRecordModeScreen() }
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
        this.runningResultScreen(modifier = modifier)

    }

    // 로그인 성공 시 홈 화면으로 이동
    LaunchedEffect(isUserLoggedIn) {
        if (isUserLoggedIn) {
            navController.navigateToHome()
        }
    }

}


