package com.zoku.runit.data

import android.content.Context
import com.zoku.runit.di.bindService
import com.zoku.runit.service.ExerciseService
import com.zoku.runit.service.ExerciseServiceState
import dagger.hilt.android.ActivityRetainedLifecycle
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@ActivityRetainedScoped
class HealthServicesRepository @Inject constructor(
    @ApplicationContext private val applicationContext: Context,
    val exerciseClientManager: ExerciseClientManager,
    val coroutineScope: CoroutineScope,
    val lifecycle: ActivityRetainedLifecycle
) {

    private val binderConnection =
        lifecycle.bindService<ExerciseService.LocalBinder, ExerciseService>(applicationContext)

    private val exerciseServiceStateUpdates: Flow<ExerciseServiceState> =
        //ExerciseService 상태 업데이트 수신
        binderConnection.flowWhenConnected(ExerciseService.LocalBinder::exerciseServiceState)

    private var errorState: MutableStateFlow<String?> = MutableStateFlow(null)

    val serviceState: StateFlow<ServiceState> =
        exerciseServiceStateUpdates.combine(errorState) { exerciseServiceState, errorString ->
            ServiceState.Connected(exerciseServiceState.copy(error = errorString))
        }.stateIn(
            coroutineScope,
            started = SharingStarted.Eagerly,
            initialValue = ServiceState.Disconnected
        )

    //운동 기능이 가능한지
    suspend fun hasExerciseCapability(): Boolean = exerciseClientManager.getExerciseCapabilities() != null

    //현재 운동이 진행 중인지
    suspend fun isExerciseInProgress(): Boolean =
        exerciseClientManager.isExerciseInProgress()

    //다른 앱에서 운동을 추적 중인지
    suspend fun isTrackingExerciseInAnotherApp(): Boolean =
        exerciseClientManager.isTrackingExerciseInAnotherApp()

    fun prepareExercise() = serviceCall { prepareExercise() }

    private fun serviceCall(function: suspend ExerciseService.() -> Unit) = coroutineScope.launch {
        Timber.tag("healthServiceRepo start").d("serviceCall first")
        binderConnection.runWhenConnected {
            Timber.tag("healthServiceRepo start").d("serviceCall function")
            function(it.getService())
        }
    }

    fun startExercise() = serviceCall {
        Timber.tag("healthServiceRepo start").d("Top start")
        try {
            errorState.value = null
            startExercise()
            Timber.tag("healthServiceRepo start").d("start")
        } catch (e: Exception) {
            Timber.tag("healthServiceRepo start").d(e)
            errorState.value = e.message
        }
    }

    fun pauseExercise() = serviceCall { pauseExercise() }
    fun stopExercise() = serviceCall { endExercise() }
    fun resumeExercise() = serviceCall { resumeExercise() }



}


sealed class ServiceState {
    data object Disconnected : ServiceState()

    data class Connected(val exerciseServiceState: ExerciseServiceState) : ServiceState()
}