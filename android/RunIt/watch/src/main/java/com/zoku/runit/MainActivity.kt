package com.zoku.runit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.wear.tooling.preview.devices.WearDevices
import com.google.android.gms.wearable.Wearable
import com.google.android.horologist.compose.layout.AppScaffold
import com.zoku.runit.ui.MainScreen
import com.zoku.runit.util.PermissionHelper
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val dataClient by lazy { Wearable.getDataClient(this) }
    private val messageClient by lazy { Wearable.getMessageClient(this) }
    private val capabilityClient by lazy { Wearable.getCapabilityClient(this) }


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

    override fun onResume() {
        super.onResume()
        dataClient.addListener(mainViewModel)
        messageClient.addListener(mainViewModel)
        capabilityClient.addListener(
            mainViewModel,
            Uri.parse("wear://"),
            CapabilityClient.FILTER_REACHABLE
        )
    }

    override fun onPause() {
        super.onPause()
        dataClient.removeListener(mainViewModel)
        messageClient.removeListener(mainViewModel)
        capabilityClient.removeListener(mainViewModel)
    }

    companion object {
        private const val TAG = "MainActivity"

        const val CAMERA_CAPABILITY = "camera"
        const val WEAR_CAPABILITY = "wear"
        const val MOBILE_CAPABILITY = "mobile"
    }

}


@Preview(device = WearDevices.SMALL_ROUND, showSystemUi = true, apiLevel = 33)
@Composable
fun DefaultPreview() {
    MainScreen()
}