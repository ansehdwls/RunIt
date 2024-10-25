package com.zoku.watch.navigation


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.zoku.watch.screen.HomeScreen
import com.zoku.watch.screen.RunningScreen

@Composable
fun WatchNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = WatchScreenDestination.home.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(route = WatchScreenDestination.home.route) {
            HomeScreen(modifier) {
                navController.navigate(WatchScreenDestination.running.route)
            }
        }

        composable(route = WatchScreenDestination.running.route) {
            RunningScreen(modifier)

        }

    }


}