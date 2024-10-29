package com.zoku.watch.service

import androidx.lifecycle.LifecycleService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ExerciseService : LifecycleService() {

    @Inject
    lateinit var healthServiceManager : H


}