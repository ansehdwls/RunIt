package com.zoku.navigatinon

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.zoku.home.HomeScreen
import com.zoku.util.ScreenDestinations


fun NavController.navigateToHome() = navigate(route = ScreenDestinations.home.route)

fun NavGraphBuilder.homeScreen() {
    composable(route = ScreenDestinations.home.route) {
        HomeScreen()
    }
}