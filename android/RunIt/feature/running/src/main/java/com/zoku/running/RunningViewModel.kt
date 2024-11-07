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
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.zoku.data.NetworkResult
import com.zoku.data.repository.RunningRepository
import com.zoku.network.model.request.Pace
import com.zoku.network.model.request.PostRunningRecordRequest
import com.zoku.network.model.request.TestSumRequest
import com.zoku.network.model.request.Track
import com.zoku.running.model.RunningUIState
import com.zoku.running.service.LocationService
import com.zoku.running.util.getIso8601TimeString
import com.zoku.ui.model.LocationData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@HiltViewModel
class RunningViewModel @Inject constructor(
    application: Application,
    private val runningRepository: RunningRepository
) : AndroidViewModel(application) {

    // UI variable
    private val _uiState = MutableStateFlow(
        RunningUIState(
            time = 0,
            distance = 0.0,
            face = 0,
            bpm = 0
        )
    )
    val uiState: StateFlow<RunningUIState> = _uiState
    private val _totalRunningList = MutableStateFlow<List<LocationData>>(emptyList())
    val totalRunningList: StateFlow<List<LocationData>> = _totalRunningList

    // GPS
    private var lastLocation: Location? = null
    private val locationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val isPause = intent.getBooleanExtra("isPause", false)
            if (isPause) {
                val locationList =
                    intent.getParcelableArrayListExtra("locationList", LocationData::class.java)
                locationList?.let {
                    _totalRunningList.value += it
                }
            } else {
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
                        newDistance = uiState.value.distance + distance.toInt()
                    )
                }
                lastLocation = newLocation
            }
        }
    }

    //Timer
    private var timerJob: Job? = null

    init {
        val filter = IntentFilter("com.zoku.running.LOCATION_UPDATE")
        application.registerReceiver(locationReceiver, filter, Context.RECEIVER_EXPORTED)
    }


    private fun updateUIState(
        newTime: Int? = null,
        newDistance: Double? = null,
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

    fun startTimer() {
        timerJob = viewModelScope.launch {
            while (true) {
                val startTime = System.currentTimeMillis()
                delay(1000)
                val currentTime = System.currentTimeMillis()

                val newTimeInSeconds = ((currentTime - startTime) / 1000).toInt()
                updateUIState(newTime = uiState.value.time + newTimeInSeconds)
            }
        }
    }


    fun stopTimer() {
        timerJob?.cancel()
    }

    fun postRunningRecord(captureFile: File, onSuccess: () -> Unit, onFail: (String) -> Unit) {
        viewModelScope.launch {

            val filePart = MultipartBody.Part.createFormData(
                name = "images",
                filename = captureFile.name,
                body = captureFile.asRequestBody("image/png".toMediaTypeOrNull())
            )

            val userJson = Gson().toJson(
                PostRunningRecordRequest(
                    track = Track(
                        path = totalRunningList.value.toString()
                    ),
                    record = com.zoku.network.model.request.Record(
                        distance = uiState.value.distance,
                        startTime = getIso8601TimeString(System.currentTimeMillis()),
                        endTime = getIso8601TimeString(System.currentTimeMillis()),
                        bpm = 100
                    ),
                    paceList = listOf(Pace(pace = 10, bpm = 10), Pace(pace = 20, bpm = 20))
                )
            )

            val userRequestBody = userJson.toRequestBody("application/json".toMediaTypeOrNull())

            val requestBody = MultipartBody.Part.createFormData("dto", null,userRequestBody)



            when (val result = runningRepository.postRunningRecord(requestBody,filePart)) {
                is NetworkResult.Success -> {
                    onSuccess()
                }

                is NetworkResult.Error -> {
                    onFail("${result.errorMsg}")
                }

                is NetworkResult.Exception -> {
                    onFail("서버가 연결이 되지 않았습니다.")
                }
            }

        }
    }


    fun submitTestSum(testSumRequest: TestSumRequest) {
        viewModelScope.launch {
            when (val result = runningRepository.postTestSum(testSumRequest)) {
                is NetworkResult.Success -> {
                    Log.d("확인", " 성공 ${result}")
                }

                is NetworkResult.Error -> {
                    Log.d("확인", "실패, 에러 ${result}")
                }

                is NetworkResult.Exception -> {
                    Log.d("확인", "서버 연결 에러")
                }
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        stopTimer()
        getApplication<Application>().unregisterReceiver(locationReceiver)
    }

}