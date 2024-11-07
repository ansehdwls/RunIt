package com.zoku.runit

import androidx.lifecycle.ViewModel
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.CapabilityInfo
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class ClientDataViewModel @Inject constructor(

) : ViewModel(), DataClient.OnDataChangedListener,
    MessageClient.OnMessageReceivedListener,
    CapabilityClient.OnCapabilityChangedListener {

    override fun onDataChanged(dataEventBuffer: DataEventBuffer) {

    }

    override fun onMessageReceived(messageEvent: MessageEvent) {
        Timber.tag("ClientDataViewModel").d("message받기 $messageEvent")
    }

    override fun onCapabilityChanged(capabilityInfo: CapabilityInfo) {
        Timber.tag("ClientDataViewModel").d("$capabilityInfo")
    }
}