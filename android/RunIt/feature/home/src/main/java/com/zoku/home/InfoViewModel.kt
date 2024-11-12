package com.zoku.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zoku.data.NetworkResult
import com.zoku.data.repository.RunningRepository
import com.zoku.network.model.response.AllTotal
import com.zoku.network.model.response.RunToday
import com.zoku.network.model.response.RunWeekList
import com.zoku.network.model.response.WeekTotal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InfoViewModel @Inject constructor(
    val runningRepository: RunningRepository
) : ViewModel()
{

    private val _todayRecord = MutableStateFlow<RunToday>(RunToday(0.0,0.0,0.0))
    val todayRecord : StateFlow<RunToday> = _todayRecord

    private val _totalAllRecord = MutableStateFlow<AllTotal>(AllTotal(0.0,0.0))
    val totalAllRecord : StateFlow<AllTotal> = _totalAllRecord

    private val _totalWeekRecord = MutableStateFlow<WeekTotal>(WeekTotal(0.0,0.0))
    val totalWeekRecord : StateFlow<WeekTotal> = _totalWeekRecord

    private val _totalWeekList = MutableStateFlow<RunWeekList>(
        RunWeekList(emptyList(), emptyList(), emptyList())
    )
    val totalWeekList : StateFlow<RunWeekList> = _totalWeekList

    fun getRunToday(){
        viewModelScope.launch {
            when(val result = runningRepository.getRunToday()){
                is NetworkResult.Success -> {
                    Log.d("확인", " 성공 ${result}")
                    _todayRecord.value = result.data.data
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

    fun getTotalRecord()
    {
        viewModelScope.launch {
            when(val result = runningRepository.getRunTotal()){
                is NetworkResult.Success -> {
                    Log.d("확인", " 성공 ${result}")
                    _totalAllRecord.value =
                        AllTotal(
                            totalDistance =  result.data.data.totalDistance,
                            totalTime = result.data.data.totalTime
                        )
                    _totalWeekRecord.value =
                        WeekTotal(
                            weekDistance = result.data.data.weekDistance,
                            weekTime = result.data.data.weekTime
                        )

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
    fun getWeekList(){
        viewModelScope.launch {
            when(val result = runningRepository.getRunWeek()){
                is NetworkResult.Success -> {
                    Log.d("확인", " 성공 ${result}")
                    _totalWeekList.value = result.data.data

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