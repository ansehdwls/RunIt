package com.zoku.running

import android.annotation.SuppressLint
import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import android.os.Build
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.zoku.data.NetworkResult
import com.zoku.data.repository.RunningRepository
import com.zoku.network.model.request.Pace
import com.zoku.network.model.request.PostRunningRecordRequest
import com.zoku.network.model.request.Track
import com.zoku.network.model.response.ExpWithAttend
import com.zoku.network.model.response.RunRecordDetail
import com.zoku.running.model.RunningEventState
import com.zoku.running.model.RunningUIState
import com.zoku.running.service.LocationService
import com.zoku.running.util.getIso8601TimeString
import com.zoku.ui.model.LocationData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber
import java.io.File
import java.util.Locale
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@HiltViewModel
class RunningViewModel @Inject constructor(
    application: Application,
    private val runningRepository: RunningRepository
) : AndroidViewModel(application), TextToSpeech.OnInitListener {

    // Running Record
    private val _practiceRecord = MutableStateFlow<RunRecordDetail?>(null)
    val practiceRecord: StateFlow<RunRecordDetail?> = _practiceRecord

    private val _runningEvent = MutableSharedFlow<RunningEventState>()
    val runningEvent: SharedFlow<RunningEventState> get() = _runningEvent

    fun updateRunningEvent(value: RunningEventState) {
        viewModelScope.launch {
            _runningEvent.emit(value)
        }
    }

    private var isPractice = false

    private var recordId: Int = 0
    private var startTime: Long = 0
    private var endTime: Long = 0

    private var last100Value: Int = 0
    private var last100Second: Int = 0
    private var lastIndex = -1

    private val bpmList = mutableListOf<Int>()

    fun getPracticeRecord(runDto: RunRecordDetail) {
        _practiceRecord.value = runDto
        if(runDto.id > 0)isPractice = true
    }

    val totalPaceList = mutableListOf<Pace>()

    // TTS
    private var tts: TextToSpeech = TextToSpeech(application, this)

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
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)
    private var initialLocation: LocationData? = null
    private var lastLocation: Location? = null
    private val locationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val isPause = intent.getBooleanExtra("isPause", false)
            if (isPause) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    val locationList =
                        intent.getParcelableArrayListExtra("locationList", LocationData::class.java)
                    locationList?.let {
                        _totalRunningList.value += it
                    }
                } else {
                    val locationList =
                        intent.getParcelableArrayListExtra<LocationData>("locationList")
                    locationList?.let {
                        _totalRunningList.value += it
                    }
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
                    Timber.tag("RunningViewModel").d("$distance")
                    updateUIState(
                        newDistance = uiState.value.distance + distance.toInt()
                    )

                    // 100m 이동시
                    val current100Value = ((uiState.value.distance % 1000) / 100).toInt()
                    if (current100Value != last100Value) {
                        val timeDifference =
                            (uiState.value.time - last100Second)

                        // pace와 bpm 추가 로직
                        updateUIState(newFace = (timeDifference * 10))
                        if (lastIndex + 1 < bpmList.size) {
                            val recentBpmList = bpmList.subList(lastIndex + 1, bpmList.size)
                            updateUIState(newBPM = recentBpmList.average().toInt())
                            totalPaceList.add(
                                Pace(
                                    bpm = recentBpmList.average().toInt(),
                                    pace = timeDifference * 10
                                )
                            )
                        } else {
                            totalPaceList.add(Pace(bpm = 0, pace = timeDifference * 10))
                        }

                        //1km 별 페이스
                        if(isPractice){
                            if(totalPaceList.size > 0 && (totalPaceList.size % 10 == 0)){
                                val recentPaces = totalPaceList.takeLast(10)
                                val practicePaces = practiceRecord.value?.paceList?.takeLast(10)

                                practicePaces?.let{
                                    val recentAveragePace = recentPaces.map { it.pace }.average()
                                    val practiceAveragePace = practicePaces.mapNotNull { it.durationList }.average()
                                    if (recentAveragePace > practiceAveragePace) {
                                        tts.speak(
                                            "현재 1키로미터 페이스가 연습보다 ${practiceAveragePace.toInt() - recentAveragePace.toInt()}초 느립니다. ",
                                            TextToSpeech.QUEUE_FLUSH,
                                            null,
                                            null
                                        )
                                    } else {
                                        tts.speak(
                                            "현재 1키로미터 페이스가 연습보다 ${practiceAveragePace.toInt() - recentAveragePace.toInt()}초 빠릅니다. ",
                                            TextToSpeech.QUEUE_FLUSH,
                                            null,
                                            null
                                        )
                                    }
                                }
                            }
                        }
                        

//                        if (bpmList.size >= timeDifference) {
//                            val recentBpmList = bpmList.takeLast(timeDifference)
//                            updateUIState(newBPM = recentBpmList.average().toInt())
//                            totalPaceList.add(
//                                Pace(
//                                    bpm = recentBpmList.average().toInt(),
//                                    pace = timeDifference * 10
//                                )
//                            )
//                        } else {
//                            totalPaceList.add(Pace(bpm = 0, pace = timeDifference * 10))
//                        }

                        lastIndex = bpmList.size - 1
                        last100Value = current100Value
                        last100Second = uiState.value.time
                    }


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
        startTime = System.currentTimeMillis()
        getInitialLocation()
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

    @SuppressLint("MissingPermission")
    private fun getInitialLocation() {
        if (_totalRunningList.value.isEmpty()) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    initialLocation = LocationData(
                        latitude = it.latitude,
                        longitude = it.longitude
                    )
                }
            }.addOnFailureListener { exception ->
                Log.e("RunningViewModel", "초기 위치를 가져오는 데 실패했습니다: $exception")
            }
        }
    }

    fun stopTimer() {
        timerJob?.cancel()
    }

    fun postRunningRecord(
        captureFile: File,
        onSuccess: (ExpWithAttend) -> Unit,
        onFail: (String) -> Unit
    ) {
        viewModelScope.launch {
            val filePart = MultipartBody.Part.createFormData(
                name = "images",
                filename = captureFile.name,
                body = captureFile.asRequestBody("image/png".toMediaTypeOrNull())
            )

            val filteredPathList =
                totalRunningList.value.filterIndexed { index, _ -> index % 2 != 0 }

            endTime = System.currentTimeMillis()

            val userJson = Gson().toJson(
                PostRunningRecordRequest(
                    track = Track(
                        path = filteredPathList.toString()
                    ),
                    record = com.zoku.network.model.request.Record(
                        distance = uiState.value.distance / 1000,
                        startTime = getIso8601TimeString(startTime),
                        endTime = getIso8601TimeString(endTime),
                        bpm = if (bpmList.average().isNaN()) 0 else bpmList.average().toInt(),
                        duration = uiState.value.time
                    ),
                    paceList = totalPaceList
                )
            )

            Timber.tag("RunningViewModel").d(
                "리퀘스트확인 레코드 : ${
                    com.zoku.network.model.request.Record(
                        distance = uiState.value.distance / 1000,
                        startTime = getIso8601TimeString(startTime),
                        endTime = getIso8601TimeString(endTime),
                        bpm = if (bpmList.average().isNaN()) 0 else bpmList.average().toInt(),
                        duration = uiState.value.time
                    )
                }"
            )

            val userRequestBody = userJson.toRequestBody("application/json".toMediaTypeOrNull())

            val requestBody = MultipartBody.Part.createFormData("dto", null, userRequestBody)



            when (val result = runningRepository.postRunningRecord(requestBody, filePart)) {
                is NetworkResult.Success -> {
                    Timber.tag("RunningViewModel").d("달리기 저장 ${result.data.data}")
                    recordId = result.data.data.id
                    onSuccess(result.data.data)
                }

                is NetworkResult.Error -> {
                    Timber.tag("RunningViewModel").d("달리기 에러 ${result.errorMsg}")
                    onFail("${result.errorMsg}")
                }

                is NetworkResult.Exception -> {
                    Timber.tag("RunningViewModel").d("달리기 네트워크 에러 ${result.e}")
                    onFail("서버가 연결이 되지 않았습니다.")
                }
            }

        }
    }

    fun getIsPractice() = isPractice

    fun getInitialLocationData(): LocationData? = initialLocation

    fun addBpm(bpm: Int) {
        bpmList.add(bpm)
    }

    fun updatePracticeRecord() {
        viewModelScope.launch {
            if(isPractice){
                practiceRecord.value?.id?.let {
                    when (val result = runningRepository.updateRunPracticeMode(it)) {
                        is NetworkResult.Success -> {
                            Timber.tag("RecordModeViewModel").d("연습 리스트 빼는거 성공 ${result.data}")
                        }

                        is NetworkResult.Error -> {
                            Timber.tag("RecordModeViewModel").d("연습 리스트 뺴는거 실패 ${result.errorMsg}")
                        }

                        is NetworkResult.Exception -> {
                            Timber.tag("RecordModeViewModel").d("연습 리스트 빼는거 네트워크 ${result.e}")
                        }
                    }
                }
            }
            when (val result = runningRepository.updateRunPracticeMode(recordId)) {
                is NetworkResult.Success -> {
                    updateRunningEvent(RunningEventState.RunningRecordModeSuccess)
                    Timber.tag("RecordModeViewModel").d("연습 리스트 새거 넣기 성공 ${result.data}")
                }

                is NetworkResult.Error -> {
                    Timber.tag("RecordModeViewModel").d("연습 리스트 새거 넣기 실패 ${result.errorMsg}")
                }

                is NetworkResult.Exception -> {
                    Timber.tag("RecordModeViewModel").d("연습 리스트 새거 넣기 네트워크 ${result.e}")
                }
            }
        }
    }

    fun getStartTime() = startTime
    fun getEndTime() = endTime


    override fun onCleared() {
        super.onCleared()
        stopTimer()
        getApplication<Application>().unregisterReceiver(locationReceiver)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale.KOREAN
        }
    }

}