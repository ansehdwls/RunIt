package com.zoku.runit

import android.app.Application
import android.util.Log
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import com.kakao.vectormap.KakaoMapSdk
import dagger.hilt.android.HiltAndroidApp

private const val TAG = "ApplicationClass"
@HiltAndroidApp
class ApplicationClass: Application(){

    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this,BuildConfig.KAKAO_API_KEY)
        KakaoMapSdk.init(this,BuildConfig.KAKAO_API_KEY)

        Log.d("KeyHash", "${Utility.getKeyHash(this)}")
    }
}