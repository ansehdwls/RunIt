package com.zoku.runit.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.zoku.login.navigation.loginScreen
import com.zoku.navigatinon.homeScreen
import com.zoku.util.ScreenDestinations

@Composable
fun RunItMainNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = ScreenDestinations.login.route
) {

    var isUserLoggedIn by remember { mutableStateOf(false) }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {

        this.homeScreen()
        this.loginScreen(onLoginSuccess = {
            // 로그인 성공 시, 상태 업데이트
            isUserLoggedIn = true
            navController.navigate("home")
        })
    }

    if(isUserLoggedIn) navController.navigate("home")
}


