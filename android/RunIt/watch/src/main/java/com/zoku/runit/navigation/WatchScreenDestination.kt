package com.zoku.runit.navigation

import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.zoku.runit.model.ExerciseResult


sealed class WatchScreenDestination(
    open val route: String
) {
    data object home : WatchScreenDestination(route = "home")
    data object running : WatchScreenDestination(route = "running")
    data object runningPause : WatchScreenDestination(route = "running/pause"){
        override val route: String
            get() = "running/pause/{result}"
        val arguments = listOf(navArgument("result"){
            type = ExerciseResultType()
        })

        fun createRoute(result : ExerciseResult) : String {
            val jsonString = Uri.encode(Gson().toJson(result))

            return "running/pause/${jsonString}"
        }
    }
}


class ExerciseResultType : NavType<ExerciseResult>(isNullableAllowed = false){
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun get(
        bundle: Bundle,
        key: String
    ): ExerciseResult? = bundle.getParcelable(key, ExerciseResult::class.java)

    override fun parseValue(value: String): ExerciseResult {
        return Gson().fromJson(value, ExerciseResult::class.java)
    }

    override fun put(
        bundle: Bundle,
        key: String,
        value: ExerciseResult
    ) {
        bundle.putParcelable(key, value)
    }
}