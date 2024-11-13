package com.zoku.runit

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.wear.tooling.preview.devices.WearDevices
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.Wearable
import com.google.android.horologist.compose.layout.AppScaffold
import com.zoku.runit.ui.MainScreen
import com.zoku.runit.util.PermissionHelper
import com.zoku.ui.model.PhoneWatchConnection
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import kotlin.coroutines.cancellation.CancellationException


@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    private val messageClient by lazy { Wearable.getMessageClient(this) }
    private val capabilityClient by lazy { Wearable.getCapabilityClient(this) }

    private lateinit var navController: NavHostController

    var InitphoneWatchConnection = PhoneWatchConnection.EMPTY

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        PermissionHelper(this, PERMISSIONS, ::finish).launchPermission()
        setContent {
            navController = rememberNavController()
            AppScaffold {
                MainScreen(
                    modifier = Modifier.fillMaxSize(),
                    navController = navController,
                    sendBpm = ::sendBpm
                )
            }
        }
    }


    override fun onResume() {
        super.onResume()
        isActivityActive = true
    }


    private fun sendBpm(bpm: Int? = 0, time: Int? = 0, phoneWatchConnection: PhoneWatchConnection) {
        lifecycleScope.launch {
            try {
                if (phoneWatchConnection != InitphoneWatchConnection || phoneWatchConnection ==
                    PhoneWatchConnection.SEND_BPM
                ) {
                    val nodes = capabilityClient
                        .getCapability(PHONE_CAPABILITY, CapabilityClient.FILTER_REACHABLE)
                        .await()
                        .nodes

                    val bpmTimeData = "${bpm}:${time}".toByteArray(Charsets.UTF_8)


                    Timber.tag("sendPhone").d("노드 확인 $bpmTimeData , ${phoneWatchConnection.route}")
                    nodes.map { node ->
                        async {
                            InitphoneWatchConnection = phoneWatchConnection
                            messageClient.sendMessage(
                                node.id,
                                phoneWatchConnection.route,
                                bpmTimeData
                            )
                                .await()
                        }
                    }.awaitAll()
                }
            } catch (cancellationException: CancellationException) {
                Timber.tag("sendPhone").d("핸드폰에 데이터 보내기 취소!")
                throw cancellationException
            } catch (exception: Exception) {
                Timber.tag("sendPhone").d("핸드폰에 데이터 보내기 오류 ${exception}")
            }
        }
    }

    override fun onStop() {
        super.onStop()
        isActivityActive = false
    }


    companion object {
        private const val PHONE_CAPABILITY = "phone"
        private const val SEND_BPM = "/send-bpm"
        var isActivityActive = false
    }

}


@Preview(device = WearDevices.SMALL_ROUND, showSystemUi = true, apiLevel = 33)
@Composable
fun DefaultPreview() {

}