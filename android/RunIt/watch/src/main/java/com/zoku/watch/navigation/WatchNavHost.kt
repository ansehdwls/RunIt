package com.zoku.watch.navigation


import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.toRoute
import com.zoku.watch.model.ExerciseResult
import com.zoku.watch.screen.HomeScreen
import com.zoku.watch.screen.RunningPauseScreen
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
            RunningScreen(modifier) { exerciseResult ->
                navController.run {
                    this.navigate(WatchScreenDestination.runningPause.createRoute(exerciseResult))
                }
            }
        }
        composable(route = WatchScreenDestination.runningPause.route,
            WatchScreenDestination.runningPause.arguments
        ) { backStackEntry ->
            val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                backStackEntry.arguments?.getParcelable("result", ExerciseResult::class.java)
            } else {
                backStackEntry.arguments?.getParcelable("result")
            }
            RunningPauseScreen(modifier, data)
        }

    }


}