package com.zoku.watch.service

import android.annotation.SuppressLint
import com.zoku.watch.data.ExerciseClientManager
import javax.inject.Inject
import android.app.Service
import androidx.health.services.client.data.ExerciseUpdate
import com.zoku.watch.data.ExerciseMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

//운동 서비스로부터의 업데이트를 모니터링
class ExerciseServiceMonitor @Inject constructor(
    val exerciseClientManager : ExerciseClientManager,
    val service : Service
){

    val exerciseService = service as ExerciseService

    val exerciseServiceState = MutableStateFlow(
        ExerciseServiceState(
            exerciseState = null,
            exerciseMetrics = ExerciseMetrics()
        )
    )

    suspend fun monitor() { //운동 업데이트를 지속적으로 수집하고 처리
        exerciseClientManager.exerciseUpdateFlow.collect {
            when (it) {
                is ExerciseMessage.ExerciseUpdateMessage ->
                    processExerciseUpdate(it.exerciseUpdate)

                is ExerciseMessage.LapSummaryMessage ->
                    exerciseServiceState.update { oldState ->
                        oldState.copy(
                            exerciseLaps = it.lapSummary.lapCount
                        )
                    }

                is ExerciseMessage.LocationAvailabilityMessage ->
                    exerciseServiceState.update { oldState ->
                        oldState.copy(
                            locationAvailability = it.locationAvailability
                        )
                    }
            }
        }
    }

    @SuppressLint("RestrictedApi") //제한된 API 사용에 대한 린트 경고 무시
    private fun processExerciseUpdate(exerciseUpdate: ExerciseUpdate) { //운동 업데이트 메시지 처리 함수
        // Dismiss any ongoing activity notification.
        if (exerciseUpdate.exerciseStateInfo.state.isEnded) {
//            exerciseService.removeOngoingActivityNotification()
        }

        exerciseServiceState.update { old ->
            old.copy(
                exerciseState = exerciseUpdate.exerciseStateInfo.state,
                exerciseMetrics = old.exerciseMetrics.update(exerciseUpdate.latestMetrics),
                activeDurationCheckpoint = exerciseUpdate.activeDurationCheckpoint
                    ?: old.activeDurationCheckpoint,
                exerciseGoal = exerciseUpdate.latestAchievedGoals
            )
        }
    }


}