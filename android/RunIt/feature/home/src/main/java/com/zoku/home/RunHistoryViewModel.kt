package com.zoku.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zoku.data.NetworkResult
import com.zoku.data.repository.RunningRepository
import com.zoku.network.model.response.HistoryWeekResponse
import com.zoku.network.model.response.WeekList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RunHistoryViewModel @Inject constructor(
    val runningRepository: RunningRepository
) : ViewModel()
{
    private val _historyWeekList = MutableStateFlow<List<List<WeekList>>>(emptyList())
    val historyWeekList : StateFlow<List<List<WeekList>>> = _historyWeekList

    private var _isFirst = MutableStateFlow<Boolean>(false)
    val isFirst : StateFlow<Boolean> = _isFirst
    fun getWeekList( today : String ){

        viewModelScope.launch {
            when(val result = runningRepository.getWeekList(today)){
                is NetworkResult.Success -> {
                    Log.d("확인", " 성공 ${result}")
                    _historyWeekList.value = result.data.data
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