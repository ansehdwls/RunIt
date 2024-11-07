package com.zoku.runit.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.zoku.runit.navigation.WatchNavHost


@Composable
fun MainScreen(modifier: Modifier = Modifier , navController : NavHostController) {
    WatchNavHost(navController, modifier)
}


