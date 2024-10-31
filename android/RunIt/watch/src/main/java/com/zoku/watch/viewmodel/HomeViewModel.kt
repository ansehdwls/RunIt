package com.zoku.watch.viewmodel

import androidx.lifecycle.ViewModel
import com.zoku.watch.data.HealthServicesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val healthServicesRepository: HealthServicesRepository
) : ViewModel() {

    fun startRunning() {
        Timber.tag("homeViewModel").d("startExercise")
        healthServicesRepository.startExercise()
    }
}