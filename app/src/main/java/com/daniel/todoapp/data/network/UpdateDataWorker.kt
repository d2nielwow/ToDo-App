package com.daniel.todoapp.data.network

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.daniel.todoapp.data.api.RetrofitClient
import com.daniel.todoapp.data.repository.TodoRepositoryImpl
import com.daniel.todoapp.domain.usecase.UpdateTodoItemUseCase
import com.daniel.todoapp.domain.model.TodoItem
import com.daniel.todoapp.domain.repository.TodoRepository

 class UpdateDataWorker(
     appContext: Context,
     workerParameters: WorkerParameters
 ): CoroutineWorker(appContext, workerParameters) {

     private val apiService = RetrofitClient.api
     private val todoRepository: TodoRepository = TodoRepositoryImpl(apiService)
     private val updateTodoItemUseCase = UpdateTodoItemUseCase(todoRepository)

     override suspend fun doWork(): Result {

         val itemId = inputData.getString("item_id") ?: return Result.failure()
         val revision = inputData.getInt("revision", 0)

         val text = inputData.getString("text") ?: return Result.failure()
         val importance = inputData.getString("importance") ?: return Result.failure()
         val isCompleted = inputData.getBoolean("isCompleted", false)
         val createdAt = inputData.getLong("createdAt", 0L)

         try {
             val item = TodoItem(
                 id = itemId,
                 text = text,
                 importance = importance,
                 isCompleted = isCompleted,
                 createdAt = createdAt
             )

             val response = updateTodoItemUseCase.execute(item, revision)

             if (response.status == "ok") {
                 return Result.success()
             } else {
                 return Result.failure()
             }

         } catch (e: Exception) {
             return Result.retry()
         }
     }
}