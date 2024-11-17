package com.zoku.home.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zoku.data.NetworkResult
import com.zoku.data.repository.RunningRepository
import com.zoku.network.model.response.RouteInfo
import com.zoku.network.model.response.RunRecordDetail
import com.zoku.network.model.response.WeekList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RunHistoryViewModel @Inject constructor(
    val runningRepository: RunningRepository
) : ViewModel() {
    private val _historyWeekList = MutableStateFlow<List<List<WeekList>>>(emptyList())
    val historyWeekList: StateFlow<List<List<WeekList>>> = _historyWeekList

    private val _historyRunRecord = MutableStateFlow<RunRecordDetail?>(null)
    val historyRunRecord: StateFlow<RunRecordDetail?> = _historyRunRecord

    private var _isFirst = MutableStateFlow<Boolean>(false)
    val isFirst: StateFlow<Boolean> = _isFirst

    private val _routeList = MutableStateFlow<List<RouteInfo>>(emptyList())
    val routeList: StateFlow<List<RouteInfo>> = _routeList

    fun getWeekList(today: String) {

        viewModelScope.launch {
            when (val result = runningRepository.getWeekList(today)) {
                is NetworkResult.Success -> {
                    Timber.tag("RunHistoryViewModel").d("히스토리 날짜 불러오기 성공 ${result.data.data}")
                    _historyWeekList.value = result.data.data
                    _isFirst.value = true
                }

                is NetworkResult.Error -> {
                    Timber.tag("RunHistoryViewModel").d("히스토리 날짜 불러오기 에러 ${result.errorMsg}")
                }

                is NetworkResult.Exception -> {
                    Timber.tag("RunHistoryViewModel").d("히스토리 날짜 불러오기 네트워크 에러 ${result.e}")
                }
            }
        }
    }

    fun getRunRecordDetail(recordId: Int) {
        viewModelScope.launch {
            when (val result = runningRepository.getRunRecordDetail(recordId)) {
                is NetworkResult.Success -> {
                    Log.d("확인", " 성공 ${result}")
                    _historyRunRecord.value = result.data.data
                    _isFirst.value = true
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

}