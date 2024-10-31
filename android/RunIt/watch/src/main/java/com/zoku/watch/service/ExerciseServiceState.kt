package com.zoku.watch.service

import androidx.health.services.client.data.DataPointContainer
import androidx.health.services.client.data.DataType
import androidx.health.services.client.data.ExerciseGoal
import androidx.health.services.client.data.ExerciseState
import androidx.health.services.client.data.ExerciseUpdate.ActiveDurationCheckpoint
import androidx.health.services.client.data.LocationAvailability

data class ExerciseMetrics(
    val heartRate: Double? = null,
    val distance: Double? = null,
    val calories: Double? = null,
    val heartRateAverage: Double? = null,
    val pace: Double? = null
) { //메트릭 데이터를 저장하고 update함
    //DataPointContaner -> 운동 데이터 포인트를 저장하는 컨테이너
    fun update(latestMetrics: DataPointContainer): ExerciseMetrics {
        return copy(
            heartRate = latestMetrics.getData(DataType.HEART_RATE_BPM).lastOrNull()?.value
                ?: heartRate,
            distance = latestMetrics.getData(DataType.DISTANCE_TOTAL)?.total ?: distance,
            calories = latestMetrics.getData(DataType.CALORIES_TOTAL)?.total ?: calories,
            heartRateAverage = latestMetrics.getData(DataType.HEART_RATE_BPM_STATS)?.average
                ?: heartRateAverage,
            pace = latestMetrics.getData(DataType.PACE).lastOrNull()?.value ?: pace
        )
    }
}

//Capturing most of the values associated with our exercise in a data class
data class ExerciseServiceState(
    val exerciseState: ExerciseState? = null,
    val exerciseMetrics: ExerciseMetrics = ExerciseMetrics(),
    val exerciseLaps: Int = 0,
    val activeDurationCheckpoint: ActiveDurationCheckpoint? = null,
    val locationAvailability: LocationAvailability = LocationAvailability.UNKNOWN,
    val error: String? = null,
    val exerciseGoal: Set<ExerciseGoal<out Number>> = emptySet()
)