package com.zoku.runit.util

import android.app.Activity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun Activity.appExit(coroutineScope: CoroutineScope) {
    coroutineScope.launch {
        finish()
        System.exit(0)
    }
}