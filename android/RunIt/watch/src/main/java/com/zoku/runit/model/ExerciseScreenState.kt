package com.zoku.runit.model

import androidx.health.services.client.data.ExerciseState
import com.zoku.runit.data.ServiceState
import com.zoku.runit.service.ExerciseServiceState
import java.time.Duration


data class ExerciseScreenState(
    val hasExerciseCapabilities: Boolean,
    val isTrackingAnotherExercise: Boolean,
    val serviceState: ServiceState,
    val exerciseState: ExerciseServiceState?,
) {
    val isEnding: Boolean
        get() = exerciseState?.exerciseState?.isEnding == true

    val isEnded: Boolean
        get() = exerciseState?.exerciseState?.isEnded == true

    val isPausing: Boolean
        get() = exerciseState?.exerciseState == ExerciseState.USER_PAUSING || exerciseState?.exerciseState == ExerciseState.AUTO_PAUSING || exerciseState?.exerciseState?.isResuming == true

    val isPaused: Boolean
        get() = exerciseState?.exerciseState?.isPaused == true

    val error: String?
        get() = when (serviceState) {
            is ServiceState.Connected -> serviceState.exerciseServiceState.error
            else -> null
        }
}

fun ExerciseScreenState.toExerciseResult(duration: Duration?): ExerciseResult {
    return ExerciseResult(
        distance = this.exerciseState?.exerciseMetrics?.distance ?: 0.0,
        pace = this.exerciseState?.exerciseMetrics?.pace ?: 0.0,
        time = duration?.toMillis(),
        bpm = this.exerciseState?.exerciseMetrics?.heartRate ?: 0.0
    )
}
