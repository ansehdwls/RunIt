package com.zoku.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zoku.data.NetworkResult
import com.zoku.data.repository.ExpRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.exp

@HiltViewModel
class RankViewModel @Inject constructor(
    private val expRepository : ExpRepository
) : ViewModel()
{
    fun getAllExpHistory(){
        viewModelScope.launch {
            when(val result = expRepository.getAllExpHistory()){
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
}