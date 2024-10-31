package com.zoku.running

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import com.zoku.running.model.RunningUIState
import com.zoku.running.service.LocationService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@HiltViewModel
class RunningViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

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

    private var lastLocation: Location? = null
    private val locationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val latitude = intent.getDoubleExtra("latitude", 0.0)
            val longitude = intent.getDoubleExtra("longitude", 0.0)

            val newLocation = Location("provider").apply {
                this.latitude = latitude
                this.longitude = longitude
            }

            if (lastLocation != null) {
                val distance = lastLocation!!.distanceTo(newLocation)
                Log.d("확인", "거리를 재고 있어요 $distance")
                updateUIState(
                    newDistance = uiState.value.distance + distance.toInt(),
                    newTime = uiState.value.time + 1
                )
            }

            lastLocation = newLocation
        }
    }

    init {
        val filter = IntentFilter("com.zoku.running.LOCATION_UPDATE")
        application.registerReceiver(locationReceiver, filter, Context.RECEIVER_EXPORTED)
    }


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

    fun startLocationService() {
        val context = getApplication<Application>()
        val intent = Intent(context, LocationService::class.java)
        context.startForegroundService(intent)
    }

    fun stopLocationService() {
        val context = getApplication<Application>()
        val intent = Intent(context, LocationService::class.java)
        context.stopService(intent)
    }

    override fun onCleared() {
        super.onCleared()
        getApplication<Application>().unregisterReceiver(locationReceiver)
    }

}