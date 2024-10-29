package com.zoku.watch.data

import androidx.health.services.client.ExerciseClient
import androidx.health.services.client.ExerciseUpdateCallback
import androidx.health.services.client.HealthServicesClient
import androidx.health.services.client.data.Availability
import androidx.health.services.client.data.DataType
import androidx.health.services.client.data.ExerciseConfig
import androidx.health.services.client.data.ExerciseLapSummary
import androidx.health.services.client.data.ExerciseTrackedStatus.Companion.NO_EXERCISE_IN_PROGRESS
import androidx.health.services.client.data.ExerciseTrackedStatus.Companion.OTHER_APP_IN_PROGRESS
import androidx.health.services.client.data.ExerciseTrackedStatus.Companion.OWNED_EXERCISE_IN_PROGRESS
import androidx.health.services.client.data.ExerciseType
import androidx.health.services.client.data.ExerciseTypeCapabilities
import androidx.health.services.client.data.ExerciseUpdate
import androidx.health.services.client.data.LocationAvailability
import androidx.health.services.client.getCapabilities
import androidx.health.services.client.getCurrentExerciseInfo
import androidx.health.services.client.startExercise
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExerciseClientManager @Inject constructor(healthServicesClient: HealthServicesClient) {
    private val exerciseClient: ExerciseClient = healthServicesClient.exerciseClient

    suspend fun getExerciseCapabilities(): ExerciseTypeCapabilities? {
        val capabilities = exerciseClient.getCapabilities()

        return if (ExerciseType.RUNNING in capabilities.supportedExerciseTypes) {
            capabilities.getExerciseTypeCapabilities(ExerciseType.RUNNING)
        } else {
            null
        }
    }


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


                val supportsAutoPauseAndResume = capabilities.supportsAutoPauseAndResume

                val config = ExerciseConfig(
                    exerciseType = ExerciseType.RUNNING,
                    dataTypes = dataTypes,
                    isAutoPauseAndResumeEnabled = supportsAutoPauseAndResume,
                    isGpsEnabled = true,
                )

                exerciseClient.startExercise(config)

            }


        }


    }

    val exerciseUpdateFlow = callbackFlow {
        val callback = object : ExerciseUpdateCallback {
            override fun onExerciseUpdateReceived(update: ExerciseUpdate) {
                trySendBlocking(ExerciseMessage.ExerciseUpdateMessage(update))
            }

            override fun onLapSummaryReceived(lapSummary: ExerciseLapSummary) {
                trySendBlocking(ExerciseMessage.LapSummaryMessage(lapSummary))
            }

            override fun onRegistered() {}

            override fun onRegistrationFailed(throwable: Throwable) {}

            override fun onAvailabilityChanged(
                dataType: DataType<*, *>, availability: Availability
            ) {
                if (availability is LocationAvailability) {
                    trySendBlocking(ExerciseMessage.LocationAvailabilityMessage(availability))
                }
            }
        }

        exerciseClient.setUpdateCallback(callback)
        awaitClose {
            exerciseClient.clearUpdateCallbackAsync(callback)
        }

    }

}