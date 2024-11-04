package com.zoku.watch

import android.Manifest
import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

val PERMISSIONS = listOf(
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION,
    Manifest.permission.ACTIVITY_RECOGNITION,
    Manifest.permission.BODY_SENSORS,
    Manifest.permission.BODY_SENSORS_BACKGROUND,
    Manifest.permission.FOREGROUND_SERVICE,
    Manifest.permission.POST_NOTIFICATIONS
)


@HiltAndroidApp
class RunItWatchApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}