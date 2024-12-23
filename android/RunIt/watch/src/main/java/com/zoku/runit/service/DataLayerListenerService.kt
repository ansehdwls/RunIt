package com.zoku.runit.service

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable
import com.google.android.gms.wearable.WearableListenerService
import com.zoku.runit.MainActivity
import com.zoku.runit.viewmodel.MainViewModel
import com.zoku.ui.model.PhoneWatchConnection
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class DataLayerListenerService : WearableListenerService() {


    private val messageClient by lazy { Wearable.getMessageClient(this) }

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)


    private var isBound = false

    private var exerciseService: ExerciseService? = null
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            Timber.tag("DataLayerListenerService").d("Service Connected")
            exerciseService = (binder as ExerciseService.LocalBinder).getService()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            exerciseService = null
        }
    }

    override fun onCreate() {
        super.onCreate()
        // ExerciseService에 바인드
        if (!isBound) {
            bindService(
                Intent(this, ExerciseService::class.java),
                serviceConnection,
                BIND_AUTO_CREATE
            )
            isBound = true
        }

    }


    override fun onMessageReceived(messageEvent: MessageEvent) {
        super.onMessageReceived(messageEvent)
        Timber.tag("DataLayerListenerService")
            .d("메세지 수신 $messageEvent ${MainActivity.isActivityActive}")
        when (messageEvent.path) {
            PhoneWatchConnection.START_ACTIVITY.route -> {
                exerciseService?.setPhoneActive(true)
                if (!MainActivity.isActivityActive) {
                    startActivity(
                        Intent(this, MainActivity::class.java)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    )
                }
            }

            PhoneWatchConnection.START_RUNNING.route -> {
                scope.launch {
                    exerciseService?.startExercise()
                }
            }

            PhoneWatchConnection.PAUSE_RUNNING.route -> {
                scope.launch {
                    exerciseService?.pauseExercise()
                }
            }

            PhoneWatchConnection.STOP_RUNNING.route -> {
                scope.launch {
                    exerciseService?.endExercise()

                }
            }

            PhoneWatchConnection.RESUME_RUNNING.route -> {
                scope.launch {
                    exerciseService?.resumeExercise()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
        unbindService(serviceConnection)
    }


}
