package com.zoku.running

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zoku.running.model.RunningUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RunningViewModel @Inject constructor(

) : ViewModel() {

    private val _uiState = MutableStateFlow(
        RunningUIState(
            time = 0,
            distance = 0,
            face = 0,
            bpm = 0
        )
    )
    val uiState: StateFlow<RunningUIState> = _uiState
    private var distanceJob: Job? = null
    private var isMeasure = false

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
        distanceJob = viewModelScope.launch {
            while (true) {
                updateUIState(
                    newDistance = uiState.value.distance + 100,
                    newTime = uiState.value.time + 100
                )
                delay(1000)
            }
        }
    }

    fun pauseMeasuringDistance() {
        distanceJob?.cancel()
        isMeasure = false
    }
}