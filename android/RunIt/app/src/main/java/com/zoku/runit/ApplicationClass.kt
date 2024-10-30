package com.zoku.runit

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ApplicationClass: Application(){
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this,BuildConfig.KAKAO_API_KEY)
    }
}