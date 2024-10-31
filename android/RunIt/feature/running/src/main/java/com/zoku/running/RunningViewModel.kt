package com.zoku.running

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RunningViewModel @Inject constructor(

) : ViewModel() {

    fun startViewModel() {
        Log.d("확인", "러닝 뷰모델 시작 !")
    }
}