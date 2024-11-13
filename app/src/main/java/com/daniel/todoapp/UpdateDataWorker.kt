package com.daniel.todoapp

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.daniel.todoapp.data.api.RetrofitClient
import com.daniel.todoapp.data.repository.TodoItemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

 class UpdateDataWorker(appContext: Context, workerParameters: WorkerParameters): CoroutineWorker(appContext, workerParameters) {
    override suspend fun doWork(): Result {
        return try {
            val repository = TodoItemRepository(RetrofitClient.api)
            val todoItems = withContext(Dispatchers.IO) {
                repository.getAllItems()
            }
            Result.success()
        } catch (e: HttpException) {
            Result.failure()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}