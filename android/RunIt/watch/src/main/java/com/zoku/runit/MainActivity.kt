package com.zoku.runit

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


    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        PermissionHelper(this, PERMISSIONS, ::finish).launchPermission()
        super.onCreate(savedInstanceState)
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

    private fun sendBpm(bpm: Int) {
        lifecycleScope.launch {
            try {
                val nodes = capabilityClient
                    .getCapability(PHONE_CAPABILITY, CapabilityClient.FILTER_REACHABLE)
                    .await()
                    .nodes

                val bpmData = bpm.toString().toByteArray(Charsets.UTF_8)

                Timber.tag("sendPhone").d("노드 확인 $nodes , $SEND_BPM")
                nodes.map { node ->
                    async {
                        Timber.tag("sendPhone").d("메세지 전송 $nodes , $bpmData")
                        messageClient.sendMessage(node.id, SEND_BPM, bpmData)
                            .await()
                    }
                }.awaitAll()

            } catch (cancellationException: CancellationException) {
                Timber.tag("sendPhone").d("핸드폰에 데이터 보내기 취소!")
                throw cancellationException
            } catch (exception: Exception) {
                Timber.tag("sendPhone").d("핸드폰에 데이터 보내기 오류 ${exception}")
            }
        }
    }
    

    override fun onDestroy() {
        super.onDestroy()
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