package com.zoku.watch.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.zoku.watch.navigation.WatchNavHost


@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    WatchNavHost(navController, modifier)
}


