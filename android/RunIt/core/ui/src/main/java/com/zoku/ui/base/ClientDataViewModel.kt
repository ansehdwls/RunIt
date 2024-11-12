package com.zoku.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.DataClient.OnDataChangedListener
import com.google.android.gms.wearable.MessageClient
import com.zoku.ui.model.PhoneWatchConnection
import com.zoku.ui.model.RunningConnectionState
import com.zoku.ui.model.WatchToPhoneData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.collections.plus
import kotlin.collections.toString
import kotlin.text.toIntOrNull


@HiltViewModel
class ClientDataViewModel @Inject constructor(

) : ViewModel(), OnDataChangedListener,
    MessageClient.OnMessageReceivedListener,
    CapabilityClient.OnCapabilityChangedListener {

    private val _phoneWatchData = MutableStateFlow<PhoneWatchConnection>(PhoneWatchConnection.EMPTY)
    val phoneWatchData: StateFlow<PhoneWatchConnection> get() = _phoneWatchData


    private val _runningConnectionState =
        MutableStateFlow<RunningConnectionState>(RunningConnectionState.ConnectionDefault)
    val runningConnectionState: StateFlow<RunningConnectionState> get() = _runningConnectionState

    fun updateConnection(state: RunningConnectionState) {
        val originState = runningConnectionState.value
        _runningConnectionState.value = when {
            originState is RunningConnectionState.ConnectionSuccess && state is RunningConnectionState.ConnectionSuccess -> {
                originState.copy(
                    data = originState.data.copy(
                        bpm = state.data.bpm,
                        time = state.data.time
                    )
                )
            }
            else -> {
                state
            }
        }
    }

    val _bpm = MutableStateFlow<List<Int>>(emptyList())
    val bpm: StateFlow<List<Int>> get() = _bpm

    fun addBpmData(bpm: Int) {
        _bpm.update { data ->
            data + bpm
        }
    }

    fun updateMessageType(connection: PhoneWatchConnection) {
        _phoneWatchData.value = connection
    }

    override fun onDataChanged(dataEventBuffer: com.google.android.gms.wearable.DataEventBuffer) {

    }

    override fun onMessageReceived(messageEvent: com.google.android.gms.wearable.MessageEvent) {
        if (messageEvent.path == PhoneWatchConnection.SEND_BPM.route) {
            // ByteArray를 String으로 변환한 후 Int로 변환
            val bpmString = messageEvent.data.toString(Charsets.UTF_8).split(":")
            val bpm = bpmString[0].toIntOrNull()
            val time = bpmString[1].toIntOrNull()
            if (bpm != null && time != null) {
                updateConnection(
                    RunningConnectionState.ConnectionSuccess(
                        data = WatchToPhoneData(
                            bpm = bpm,
                            time = time
                        )
                    )
                )
                addBpmData(bpm)
                Timber.tag("ClientDataViewModel").d("BPM 받기 성공 ${bpm} 시간 : ${time}")
            } else {
                timber.log.Timber.Forest.tag("ClientDataViewModel").e("BPM 받기 실패")
            }
        }
        updateMessageType(
            PhoneWatchConnection.getType(messageEvent.path) ?: PhoneWatchConnection.EMPTY
        )
        Timber.tag("ClientDataViewModel").d("message받기 $messageEvent")
    }

    override fun onCapabilityChanged(capabilityInfo: com.google.android.gms.wearable.CapabilityInfo) {
        Timber.tag("ClientDataViewModel").d("$capabilityInfo")
    }
}