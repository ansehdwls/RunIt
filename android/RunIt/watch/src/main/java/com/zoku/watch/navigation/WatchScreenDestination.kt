package com.zoku.watch.navigation

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.navigation.NavType
import com.zoku.watch.model.ExerciseScreenState


sealed class WatchScreenDestination(
    open val route: String
) {
    data object home : WatchScreenDestination(route = "home")
    data object running : WatchScreenDestination(route = "running")
    data object runningPause : WatchScreenDestination(route = "running/pause")
}


class ExerciseScreenStateNavType : NavType<ExerciseScreenState>(isNullableAllowed = false){
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun get(
        bundle: Bundle,
        key: String
    ): ExerciseScreenState? = bundle.getParcelable(key, ExerciseScreenState::class.java)

    override fun parseValue(value: String): ExerciseScreenState {
        val listType = object : TypeToken<List<User>>() {}.type
        return Gson().fromJson(value, listType)
    }

    override fun put(
        bundle: Bundle,
        key: String,
        value: ExerciseScreenState
    ) {
        TODO("Not yet implemented")
    }
}