package com.zoku.runit

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.Wearable
import com.google.firebase.messaging.FirebaseMessaging
import com.zoku.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import kotlin.coroutines.cancellation.CancellationException

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val dataClient by lazy { Wearable.getDataClient(this) }
    private val messageClient by lazy { Wearable.getMessageClient(this) }
    private val capabilityClient by lazy { Wearable.getCapabilityClient(this) }

    private val clientDataViewModel by viewModels<ClientDataViewModel>()


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
//        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                val token = task.result
//                Log.d("FCM Token", token)
//            } else {
//                Log.w("FCM Token", "Fetching FCM registration token failed", task.exception)
//            }
//        }
        setContent {
            com.zoku.ui.RunItTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(Modifier.padding(innerPadding),
                        ::startWearableActivity)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        dataClient.addListener(clientDataViewModel)
        messageClient.addListener(clientDataViewModel)
        capabilityClient.addListener(
            clientDataViewModel,
            Uri.parse("wear://"),
            CapabilityClient.FILTER_REACHABLE
        )
    }

    private fun startWearableActivity() {
        lifecycleScope.launch {
            try {
                val nodes = capabilityClient
                    .getCapability(WEAR_CAPABILITY, CapabilityClient.FILTER_REACHABLE)
                    .await()
                    .nodes

                Timber.tag("MainWearable").d("가능 노드 $nodes")
                nodes.map { node ->
                    async {
                        messageClient.sendMessage(node.id, START_ACTIVITY_PATH, byteArrayOf())
                            .await()
                    }
                }.awaitAll()
                Timber.tag("MainWearable").d("웨어러블 실행하도록 요청하는 것 이가능!")
            } catch (cancellationException: CancellationException) {
                throw cancellationException
            } catch (exception: Exception) {
                Timber.tag("MainWearable").d("웨어러블 실행 오류 ${exception}")
            }
        }
    }

    companion object {
        private const val TAG = "MainActivity"

        private const val START_ACTIVITY_PATH = "/start-activity"
        private const val WEAR_CAPABILITY = "wear"

    }

}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    com.zoku.ui.RunItTheme {
//        MainScreen()
    }
}

