package com.zoku.runit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zoku.runit.data.HealthServicesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val healthServicesRepository: HealthServicesRepository
) : ViewModel() {


    private val _checkPhoneActive = MutableStateFlow(false)
    val checkPhoneActive: StateFlow<Boolean> get() = _checkPhoneActive


    fun prepareRunning() {
        healthServicesRepository.prepareExercise()
    }

    fun startRunning() {
        Timber.tag("homeViewModel").d("startExercise")
        healthServicesRepository.startExercise()
    }

    fun checkPhoneActive() {
        viewModelScope.launch {
            runCatching { healthServicesRepository.checkPhoneActive() }
                .onSuccess {
                    Timber.tag("HomeViewModel").d("checkPhoneActive $it")
                    _checkPhoneActive.value = it
                }
                .onFailure {
                    Timber.tag("HomeViewModel").d("checkPhoneActive fail $it")
                    _checkPhoneActive.value = false
                }
        }
    }

    init {
        checkPhoneActive()
    }
}