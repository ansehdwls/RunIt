package com.zoku.watch.data

import android.annotation.SuppressLint
import androidx.health.services.client.ExerciseClient
import androidx.health.services.client.ExerciseUpdateCallback
import androidx.health.services.client.HealthServicesClient
import androidx.health.services.client.clearUpdateCallback
import androidx.health.services.client.data.Availability
import androidx.health.services.client.data.DataType
import androidx.health.services.client.data.ExerciseConfig
import androidx.health.services.client.data.ExerciseLapSummary
import androidx.health.services.client.data.ExerciseTrackedStatus
import androidx.health.services.client.data.ExerciseTrackedStatus.Companion.NO_EXERCISE_IN_PROGRESS
import androidx.health.services.client.data.ExerciseTrackedStatus.Companion.OTHER_APP_IN_PROGRESS
import androidx.health.services.client.data.ExerciseTrackedStatus.Companion.OWNED_EXERCISE_IN_PROGRESS
import androidx.health.services.client.data.ExerciseType
import androidx.health.services.client.data.ExerciseTypeCapabilities
import androidx.health.services.client.data.ExerciseUpdate
import androidx.health.services.client.data.LocationAvailability
import androidx.health.services.client.endExercise
import androidx.health.services.client.getCapabilities
import androidx.health.services.client.getCurrentExerciseInfo
import androidx.health.services.client.markLap
import androidx.health.services.client.pauseExercise
import androidx.health.services.client.resumeExercise
import androidx.health.services.client.startExercise
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@SuppressLint("RestrictedApi")
@Singleton
class ExerciseClientManager @Inject constructor(healthServicesClient: HealthServicesClient) {

    //HealthServicesClient에서 운동 관련 기능을 제공하는 ExerciseClient를 가져옴.
    private val exerciseClient: ExerciseClient = healthServicesClient.exerciseClient

    //러닝이 지원되는 운동 타입 목록에 있는지 확인하고, 지원될 경우 해당 운동 타입의 기능을 리턴
    suspend fun getExerciseCapabilities(): ExerciseTypeCapabilities? {
        val capabilities = exerciseClient.getCapabilities()

        return if (ExerciseType.RUNNING in capabilities.supportedExerciseTypes) {
            capabilities.getExerciseTypeCapabilities(ExerciseType.RUNNING)
        } else {
            null
        }
    }

    //현재 사용자가 운동 중인지 확인함.
    suspend fun isExerciseInProgress(): Boolean {
        //현재 운동 정보를 가져옴.
        val exerciseInfo = exerciseClient.getCurrentExerciseInfo()
        return exerciseInfo.exerciseTrackedStatus == ExerciseTrackedStatus.OWNED_EXERCISE_IN_PROGRESS
    }

    //다른 앱에서 운동을 추적하고 있는지 확인
    suspend fun isTrackingExerciseInAnotherApp(): Boolean {
        val exerciseInfo = exerciseClient.getCurrentExerciseInfo()
        return exerciseInfo.exerciseTrackedStatus == ExerciseTrackedStatus.OTHER_APP_IN_PROGRESS
    }

    //운동 세션을 시작함.
    suspend fun startExercise() {
        val capabilities = getExerciseCapabilities() ?: return

        val exerciseInfo = exerciseClient.getCurrentExerciseInfo()
        when (exerciseInfo.exerciseTrackedStatus) {
            OTHER_APP_IN_PROGRESS -> {}
            OWNED_EXERCISE_IN_PROGRESS -> {}
            NO_EXERCISE_IN_PROGRESS -> {
                val dataTypes = setOf(
                    DataType.HEART_RATE_BPM,
                    DataType.HEART_RATE_BPM_STATS,
                    DataType.CALORIES_TOTAL,
                    DataType.DISTANCE_TOTAL,
                    DataType.SPEED,
                    DataType.SPEED_STATS,
                    DataType.PACE,
                    DataType.PACE_STATS,
                    DataType.RUNNING_STEPS_TOTAL,
                    DataType.STEPS_PER_MINUTE,
                    DataType.STEPS_PER_MINUTE_STATS,
                    DataType.VO2_MAX,
                    DataType.VO2_MAX_STATS,
                    DataType.LOCATION,
                ).intersect(capabilities.supportedDataTypes)

                //자동 일시정지 및 재개 기능 지원되는지 확인
                val supportsAutoPauseAndResume = capabilities.supportsAutoPauseAndResume

                //운동 타입, 데이터 타입, 자동 일시정지/재개, GPS 사용 여부, 운동 목표 설정
                val config = ExerciseConfig(
                    exerciseType = ExerciseType.RUNNING,
                    dataTypes = dataTypes,
                    isAutoPauseAndResumeEnabled = supportsAutoPauseAndResume,
                    isGpsEnabled = true,
                )

                //운동 세션 시작
                exerciseClient.startExercise(config)

            }


        }
    }

    //운동 세션 종료
    suspend fun endExercise() {

        exerciseClient.endExercise()
    }

    //운동 세션 일시정지
    suspend fun pauseExercise() {

        exerciseClient.pauseExercise()
    }

    //운동 세션 재개
    suspend fun resumeExercise() {

        exerciseClient.resumeExercise()
    }


    //운동 세션 중 랩을 기록함.
    suspend fun markLap() {
        if (isExerciseInProgress()) {
            exerciseClient.markLap()
        }
    }


    //운동 업데이트를 비동기적으로 수집하고, 이를 Flow로 변환해 ExerciseService에서 관찰할 수 있게 함.
    val exerciseUpdateFlow = callbackFlow {
        val callback = object : ExerciseUpdateCallback {
            override fun onExerciseUpdateReceived(update: ExerciseUpdate) { //운동 업데이트
                trySendBlocking(ExerciseMessage.ExerciseUpdateMessage(update))
            }

            override fun onLapSummaryReceived(lapSummary: ExerciseLapSummary) { // 랩 요약
                trySendBlocking(ExerciseMessage.LapSummaryMessage(lapSummary))
            }

            override fun onRegistered() { //등록 상태 처리

            }

            override fun onRegistrationFailed(throwable: Throwable) {}

            override fun onAvailabilityChanged(
                dataType: DataType<*, *>, availability: Availability
            ) { //위치 가용성 변경
                if (availability is LocationAvailability) {
                    trySendBlocking(ExerciseMessage.LocationAvailabilityMessage(availability))
                }
            }
        }

        exerciseClient.setUpdateCallback(callback) //콜백을 설정해 운동 업데이트를 수신
        awaitClose { // Flow 종료시 콜백 해제
            runBlocking {
                exerciseClient.clearUpdateCallback(callback)
            }
        }

    }

}

sealed class ExerciseMessage {
    class ExerciseUpdateMessage(val exerciseUpdate: ExerciseUpdate) : ExerciseMessage()
    class LapSummaryMessage(val lapSummary: ExerciseLapSummary) : ExerciseMessage()
    class LocationAvailabilityMessage(val locationAvailability: LocationAvailability) :
        ExerciseMessage()
}
