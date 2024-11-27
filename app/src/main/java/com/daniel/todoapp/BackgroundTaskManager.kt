package com.daniel.todoapp

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.daniel.todoapp.data.network.UpdateDataWorker
import java.util.concurrent.TimeUnit

class BackgroundTaskManager(
    private val context: Context
) {

    fun startPeriodicUpdate() {
        val inputData = workDataOf("item_id" to "some_id", "revision" to 0)

        val updateWorkRequest = PeriodicWorkRequestBuilder<UpdateDataWorker>(8, TimeUnit.HOURS)
            .setInitialDelay(10, TimeUnit.MINUTES)
            .setInputData(inputData)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "updateTodoItems",
            ExistingPeriodicWorkPolicy.KEEP,
            updateWorkRequest
        )
    }
}