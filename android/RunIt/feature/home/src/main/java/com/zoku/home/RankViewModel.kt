package com.zoku.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zoku.data.NetworkResult
import com.zoku.data.model.LoginData
import com.zoku.data.repository.ExpRepository
import com.zoku.network.model.response.ExpDataHistory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.exp

@HiltViewModel
class RankViewModel @Inject constructor(
    private val expRepository : ExpRepository
) : ViewModel()
{

    private val _currentExp = MutableStateFlow(0)

    val currentExp: StateFlow<Int> = _currentExp

    private val _allExpHistoryDataList = MutableStateFlow<List<ExpDataHistory>>(emptyList())

    val allExpHistoryDataList : StateFlow<List<ExpDataHistory>> = _allExpHistoryDataList


    fun getAllExpHistory(){
        viewModelScope.launch {
            when(val result = expRepository.getAllExpHistory()){
                is NetworkResult.Success -> {
                    Log.d("확인", " 성공 ${result}")
                    _allExpHistoryDataList.value = result.data.data
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

    fun getWeekExp() {
        viewModelScope.launch {
            when(val result = expRepository.getWeekExpHistory()){
                is NetworkResult.Success -> {
                    Log.d("확인", " 성공 ${result}")
                    _currentExp.value = result.data.data.toInt()
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