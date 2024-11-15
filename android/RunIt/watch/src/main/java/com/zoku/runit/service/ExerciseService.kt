package com.zoku.runit.service

import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import androidx.core.app.ServiceCompat
import androidx.health.services.client.data.ExerciseState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.zoku.runit.data.ExerciseClientManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.Duration
import javax.inject.Inject

@AndroidEntryPoint
class ExerciseService : LifecycleService() {

    @Inject
    lateinit var exerciseClientManager: ExerciseClientManager

    @Inject
    lateinit var exerciseNotificationManager: ExerciseNotificationManager

    @Inject
    lateinit var exerciseServiceMonitor: ExerciseServiceMonitor

    private var isBound = false //서비스 바인딩 여부
    private var isStarted = false //서비스 시작 여부
    private val localBinder = LocalBinder()
    private var phoneActive = false


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        Timber.tag("ExerciseService").d("onStartCommand 호출 $intent  ${intent?.action}")
        if (!isStarted) {
            isStarted = true


            if (!isBound) {
                stopSelfIfNotRunning()
            }

            lifecycleScope.launch(Dispatchers.Default) {// 운동 데이터 모니터링
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    exerciseServiceMonitor.monitor()
                }
            }
        }

        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)

        handleBind()

        return localBinder
    }

    override fun onRebind(intent: Intent?) {
        super.onRebind(intent)

        handleBind()
    }

    override fun onUnbind(intent: Intent?): Boolean {
        isBound = false
        lifecycleScope.launch {
            delay(UNBIND_DELAY)
            if (!isBound) {
                stopSelfIfNotRunning()
            }
        }
        // Allow clients to re-bind. We will be informed of this in onRebind().
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleScope.launch { endExercise() }
    }

    //서비스가 바인딩되었음을 처리.
    private fun handleBind() {
        if (!isBound) {
            isBound = true
            startService(Intent(this, this::class.java))
        }
    }


    //핸드폰 앱 로그인 확인
    fun setPhoneActive(check: Boolean) {
        Timber.tag("ExerciseService").d("setPhoneActive $check")
        phoneActive = check
    }

    fun checkPhoneActive(): Boolean = phoneActive

    //운동 상태 확인
    private suspend fun isExerciseInProgress() = exerciseClientManager.isExerciseInProgress()

    //운동 준비 함수
    suspend fun prepareExercise() {
        exerciseClientManager.prepareExercise()
    }

    //운동 시작 함수
    suspend fun startExercise() {
        postOngoingActivityNotification()
        exerciseClientManager.startExercise()
    }

    //운동 일시정지 함수
    suspend fun pauseExercise() {
        exerciseClientManager.pauseExercise()
    }

    //운동 재개 함수
    suspend fun resumeExercise() {
        exerciseClientManager.resumeExercise()
    }

    //운동 종료 함수
    suspend fun endExercise() {
        exerciseClientManager.endExercise()
        removeOngoingActivityNotification()
    }

    //랩 표시 함수
    fun markLap() {
        lifecycleScope.launch {
            exerciseClientManager.markLap()
        }
    }


    private fun stopSelfIfNotRunning() { //서비스가 실제 수행중인지 확인하고, 그렇지 않으면 서비스 중지
        lifecycleScope.launch {
            if (!isExerciseInProgress()) {
                if (exerciseServiceMonitor.exerciseServiceState.value.exerciseState == ExerciseState.PREPARING) {
                    lifecycleScope.launch {
                        endExercise()
                    }
                }

                stopSelf()
            }
        }
    }

    fun removeOngoingActivityNotification() { // 서비스가 포그라운드에서 실행 중인 경우, 진행 중인 활동 알림 제거
        stopForeground(STOP_FOREGROUND_REMOVE)
    }

    private fun postOngoingActivityNotification() {

        exerciseNotificationManager.createNotificationChannel()
        val serviceState = exerciseServiceMonitor.exerciseServiceState.value
        ServiceCompat.startForeground(
            this,
            ExerciseNotificationManager.NOTIFICATION_ID,
            exerciseNotificationManager.buildNotification(
                serviceState.activeDurationCheckpoint?.activeDuration ?: Duration.ZERO
            ),

            // foregroundServiceTypes
            ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION or if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
                ServiceInfo.FOREGROUND_SERVICE_TYPE_HEALTH else 0
        )
    }

    inner class LocalBinder : Binder() {
        fun getService() = this@ExerciseService

        val exerciseServiceState: Flow<ExerciseServiceState>
            get() = this@ExerciseService.exerciseServiceMonitor.exerciseServiceState
    }

    companion object {
        private val UNBIND_DELAY = 3000L
        const val RUNNING_ACTION = "com.zoku.runit.action.running"

    }
}