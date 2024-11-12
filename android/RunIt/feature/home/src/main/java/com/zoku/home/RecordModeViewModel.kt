package com.zoku.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zoku.data.NetworkResult
import com.zoku.data.repository.RunningRepository
import com.zoku.network.model.response.RunPractice
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecordModeViewModel @Inject constructor(
    private val runningRepository: RunningRepository
) : ViewModel()
{
    private val _practiceList = MutableStateFlow<List<RunPractice>>(emptyList())
    val practiceList : StateFlow<List<RunPractice>> = _practiceList

    fun getPracticeList(){
        viewModelScope.launch {
            when(val result = runningRepository.getRunPracticeList()){
                is NetworkResult.Success -> {
                    Log.d("확인", " 성공 ${result}")
                    _practiceList.value = result.data.data
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