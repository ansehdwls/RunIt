package com.zoku.runit.manager


import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.Wearable
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.tasks.await
import timber.log.Timber


@HiltWorker
class SendHeartbeatWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val messageClient: MessageClient
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val data = inputData.getBoolean("type", true)

        runCatching {
            val nodeClient = Wearable.getNodeClient(context)
            nodeClient.connectedNodes.await()
        }.onSuccess {
            if (it.isEmpty()) {
                return Result.retry()
            }

            it.forEach { node ->
                messageClient.sendMessage(node.id, "/heartbeat-${data}", "heartbeat".toByteArray())
                    .await()
            }
            return Result.success()
        }.onFailure {
            Timber.tag("SendHeartbeatWorker").d("에러 발생 ${it.message}")
            return Result.retry()
        }
        return Result.retry()
    }
}