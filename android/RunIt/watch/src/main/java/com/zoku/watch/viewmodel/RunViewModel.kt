package com.zoku.watch.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zoku.watch.data.HealthServicesRepository
import com.zoku.watch.data.ServiceState
import com.zoku.watch.model.ExerciseScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber
import java.time.Duration
import javax.inject.Inject


@HiltViewModel
class RunViewModel @Inject constructor(
    private val healthServicesRepository: HealthServicesRepository
) : ViewModel() {



    val uiState: StateFlow<ExerciseScreenState> = healthServicesRepository.serviceState.map {
        Timber.tag("runViewModel").d("$it")
        ExerciseScreenState(
            hasExerciseCapabilities = healthServicesRepository.hasExerciseCapability(),
            isTrackingAnotherExercise = healthServicesRepository.isTrackingExerciseInAnotherApp(),
            serviceState = it,
            exerciseState = (it as? ServiceState.Connected)?.exerciseServiceState,
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(3_000),
        healthServicesRepository.serviceState.value.let {
            ExerciseScreenState(
                hasExerciseCapabilities = true,
                isTrackingAnotherExercise = false,
                serviceState = it,
                exerciseState = (it as? ServiceState.Connected)?.exerciseServiceState,
            )
        }
    )




    fun pauseRunning() {
        Timber.tag("homeViewModel").d("startExercise")
        healthServicesRepository.pauseExercise()
    }
}