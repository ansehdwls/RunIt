package com.zoku.runit

import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
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
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.Wearable
import com.zoku.runit.manager.SendHeartbeatWorker
import com.zoku.ui.base.ClientDataViewModel
import com.zoku.ui.model.PhoneWatchConnection
import com.zoku.ui.theme.RunItTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var messageClient: MessageClient


    private val dataClient by lazy { Wearable.getDataClient(this) }

    private val capabilityClient by lazy { Wearable.getCapabilityClient(this) }


    private val clientDataViewModel by viewModels<ClientDataViewModel>()


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        enableEdgeToEdge()
        setContent {
            RunItTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(
                        Modifier.padding(innerPadding),
                        ::sendWearable,
                        ::sendWearable,
                        ::sendWearable,
                        ::sendWearable,
                        ::sendWearable,
                        viewModel = clientDataViewModel,
                        sendHeartBeat = ::sendHeartbeatToWatch
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        sendHeartbeatToWatch(true)
        dataClient.addListener(clientDataViewModel)
        messageClient.addListener(clientDataViewModel)
        capabilityClient.addListener(
            clientDataViewModel,
            Uri.parse("phone://"),
            CapabilityClient.FILTER_REACHABLE
        )
    }

    private fun sendHeartbeatToWatch(type: Boolean = true) { // true -> 시작, false -> 종료
        Timber.tag("MainActivity").d("보내기")
        val data = Data.Builder()
            .putBoolean("type", type)
            .build()

        val sendHeartbeatRequest = OneTimeWorkRequestBuilder<SendHeartbeatWorker>()
            .setInputData(data)
            .build()
        val workManager = WorkManager.getInstance(this)
        workManager.enqueue(sendHeartbeatRequest)
    }

    private fun sendWearable(path: String = PhoneWatchConnection.START_ACTIVITY.route) {
        lifecycleScope.launch {
            try {
                val nodes = capabilityClient
                    .getCapability(WEAR_CAPABILITY, CapabilityClient.FILTER_REACHABLE)
                    .await()
                    .nodes
                Timber.tag("MainWearable").d("메세지 전송 $nodes , $path")
                nodes.map { node ->
                    async {
                        Timber.tag("MainWearable").d("메세지 전송 $nodes , $path")
                        messageClient.sendMessage(node.id, path, byteArrayOf())
                            .await()
                    }
                }.awaitAll()
            } catch (cancellationException: CancellationException) {
                Timber.tag("MainWearable").d("웨어러블 실행하도록 요청하는 것 !")
                throw cancellationException
            } catch (exception: Exception) {
                Timber.tag("MainWearable").d("웨어러블 실행 오류 ${exception}")
            }
        }
    }

    override fun onStop() {
        super.onStop()
        Timber.tag("PhoneMainActivity").d("onStop")

    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.tag("PhoneMainActivity").d("onDestroy")
        sendHeartbeatToWatch(false)
    }

    companion object {
        private const val TAG = "MainActivity"
        private const val WEAR_CAPABILITY = "wear"

    }

}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RunItTheme {
//        MainScreen()
    }
}

