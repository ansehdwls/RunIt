package com.zoku.watch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.wear.tooling.preview.devices.WearDevices
import com.google.android.horologist.compose.layout.AppScaffold
import com.zoku.watch.ui.MainScreen
import com.zoku.watch.util.PermissionHelper
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        PermissionHelper(this, PERMISSIONS, ::finish).launchPermission()
        super.onCreate(savedInstanceState)
        setContent {
            AppScaffold {
                MainScreen(Modifier.fillMaxSize())
            }

        }
    }
}


@Preview(device = WearDevices.SMALL_ROUND, showSystemUi = true, apiLevel = 33)
@Composable
fun DefaultPreview() {
    MainScreen()
}