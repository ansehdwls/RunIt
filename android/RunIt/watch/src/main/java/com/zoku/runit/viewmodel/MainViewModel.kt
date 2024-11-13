package com.zoku.runit.viewmodel

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.wearable.Wearable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    private val _isPhoneActive = MutableStateFlow<Boolean>(false)
    val isPhoneActive: StateFlow<Boolean> get() = _isPhoneActive

    private val messageClient = Wearable.getMessageClient(application)

    init {
        listenForHeartbeats()
    }



    private fun listenForHeartbeats() {
        viewModelScope.launch {
            messageClient.addListener { messageEvent ->
                Timber.tag("MainViewModel").d("확인 처리 $messageEvent")
                if (messageEvent.path == "/start-activity") {
                    _isPhoneActive.update {
                        true
                    }
                }else if(messageEvent.path == "/heartbeat-false"){
                    _isPhoneActive.update {
                        false
                    }
                }
            }
        }
    }

}