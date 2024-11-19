package com.zoku.home.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zoku.data.NetworkResult
import com.zoku.data.repository.RouteRepository
import com.zoku.data.repository.RunningRepository
import com.zoku.network.model.response.RouteInfo
import com.zoku.network.model.response.RunPractice
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.text.removeSurrounding
import kotlin.text.split
import kotlin.text.substringAfter
import kotlin.text.substringBefore
import kotlin.text.toDoubleOrNull

@HiltViewModel
class RecordModeViewModel @Inject constructor(
    private val runningRepository: RunningRepository,
    private val routeRepository: RouteRepository
) : ViewModel() {
    private val _practiceList = MutableStateFlow<List<RunPractice>>(emptyList())
    val practiceList: StateFlow<List<RunPractice>> = _practiceList

    private val _routeList = MutableStateFlow<List<RouteInfo>>(emptyList())
    val routeList: StateFlow<List<RouteInfo>> = _routeList


    fun getPracticeList() {
        viewModelScope.launch {
            when (val result = runningRepository.getRunPracticeList()) {
                is NetworkResult.Success -> {
                    Timber.tag("RecordModeViewModel").d("연습 리스트 불러오기 성공 ${result.data}")
                    _practiceList.value = result.data.data
                }

                is NetworkResult.Error -> {
                    Timber.tag("RecordModeViewModel").d("연습 리스트 불러오기 에러 ${result.errorMsg}")
                }

                is NetworkResult.Exception -> {
                    Timber.tag("RecordModeViewModel").d("연습 리스트 불러오기 네트워크 에러 ${result.e}")
                }
            }
        }
    }

    fun updatePracticeRecord(recordId: Int) {
        viewModelScope.launch {
            when (val result = runningRepository.updateRunPracticeMode(recordId)) {
                is NetworkResult.Success -> {
                    Timber.tag("RecordModeViewModel").d("연습 리스트 갱신 성공 ${result.data}")
                }

                is NetworkResult.Error -> {
                    Timber.tag("RecordModeViewModel").d("연습 리스트 갱신 실패 ${result.errorMsg}")
                }

                is NetworkResult.Exception -> {
                    Timber.tag("RecordModeViewModel").d("연습 리스트 갱신 네트워크 ${result.e}")
                }
            }
        }
    }


    fun getRouteList(recordId: Int) {
        viewModelScope.launch {
            when (val result = routeRepository.getRouteList(recordId)) {
                is NetworkResult.Success -> {
                    Log.d("확인", " 성공111 ${result}")
                    _routeList.value = parseRouteInfoList(result.data.data.path)
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

    fun parseRouteInfoList(str: String): List<RouteInfo> {
        val routeList = mutableListOf<RouteInfo>()

        // 문자열에서 각 RouteInfo 객체 부분 추출
        val routeEntries = str.removeSurrounding("[", "]").split("), ")
        for (entry in routeEntries) {
            val latitude = entry.substringAfter("latitude=").substringBefore(",").toDoubleOrNull()
            val longitude = entry.substringAfter("longitude=").substringBefore(")").toDoubleOrNull()

            // 유효한 값일 때만 RouteInfo 객체로 추가
            if (latitude != null && longitude != null) {
                routeList.add(RouteInfo(latitude, longitude))
            }
        }
        return routeList
    }

}

