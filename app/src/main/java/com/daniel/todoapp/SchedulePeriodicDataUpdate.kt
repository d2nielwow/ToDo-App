package com.daniel.todoapp

import android.content.Context
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

fun schedulePeriodicDataUpdate(context: Context) {
    val workRequest = PeriodicWorkRequestBuilder<UpdateDataWorker>(8, TimeUnit.HOURS)
        .build()

    val workManager = WorkManager.getInstance(context)
    workManager.enqueue(workRequest)
}