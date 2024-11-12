package com.zoku.running.navigation

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.zoku.network.model.response.RunRecordDetail
import com.zoku.running.RunningScreen
import com.zoku.ui.model.PhoneWatchConnection
import com.zoku.util.ScreenDestinations

fun NavController.navigateToRunning(recordDetail: RunRecordDetail) =
    navigate(route = ScreenDestinations.running.createRoute(recordDetail))

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun NavGraphBuilder.runningScreen(
    modifier: Modifier = Modifier,
    onPauseWearableActivityClick: (String) -> Unit,
    onResumeWearableActivityClick: (String) -> Unit,
    onStopWearableActivityClick: (String) -> Unit,
    moveToHome : ()->Unit,
    phoneWatchData: PhoneWatchConnection
) {
    composable(route = ScreenDestinations.running.route,
        arguments = listOf(navArgument("recordDto") { type = NavType.StringType })
    ) {
            backStackEntry ->
        val recordDtoJson = backStackEntry.arguments?.getString("recordDto") ?: ""
        val recordDto = Gson().fromJson(recordDtoJson, RunRecordDetail::class.java)

        Log.d("확인", "runningScreen: $recordDto 잘왔다")
        RunningScreen(
            modifier,
            onPauseWearableActivityClick = onPauseWearableActivityClick,
            onResumeWearableActivityClick = onResumeWearableActivityClick,
            onStopWearableActivityClick = onStopWearableActivityClick,
            moveToHome = moveToHome,
            phoneWatchData = phoneWatchData,
            runRecordDetail = recordDto
        )
    }
}
