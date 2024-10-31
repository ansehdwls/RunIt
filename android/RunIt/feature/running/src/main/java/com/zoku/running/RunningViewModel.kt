package com.zoku.running

import android.app.Application
import android.location.Location
import android.os.Looper
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.zoku.running.model.RunningUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class RunningViewModel @Inject constructor(
    application: Application
) : ViewModel() {

    // UI variable
    private val _uiState = MutableStateFlow(
        RunningUIState(
            time = 0,
            distance = 0,
            face = 0,
            bpm = 0
        )
    )
    val uiState: StateFlow<RunningUIState> = _uiState

    // Measure variable
    private var distanceJob: Job? = null
    private var isMeasure = false
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)
    private var lastLocation: Location? = null


    private fun updateUIState(
        newTime: Int? = null,
        newDistance: Int? = null,
        newFace: Int? = null,
        newBPM: Int? = null,
    ) {
        _uiState.update { currentState ->
            currentState.copy(
                time = newTime ?: currentState.time,
                distance = newDistance ?: currentState.distance,
                bpm = newBPM ?: currentState.bpm,
                face = newFace ?: currentState.face
            )
        }
    }

    fun startMeasuringDistance() {
        if (isMeasure) return
        isMeasure = true
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000)
            .build()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val newLocation = locationResult.lastLocation
                if (lastLocation != null && newLocation != null) {
                    val distance = lastLocation!!.distanceTo(newLocation)
                    updateUIState(
                        newDistance = uiState.value.distance + distance.toInt(),
                        newTime = uiState.value.time + 1
                    )
                }
                lastLocation = newLocation
            }
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )

        distanceJob?.invokeOnCompletion {
            fusedLocationClient.removeLocationUpdates(locationCallback)
            isMeasure = false
        }
    }

    fun pauseMeasuringDistance() {
        distanceJob?.cancel()
        isMeasure = false
    }
}