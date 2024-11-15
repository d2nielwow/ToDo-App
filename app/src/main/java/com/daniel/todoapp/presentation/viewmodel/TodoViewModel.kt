package com.daniel.todoapp.presentation.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.daniel.todoapp.data.network.UpdateDataWorker
import com.daniel.todoapp.domain.usecase.CreateTodoItemUseCase
import com.daniel.todoapp.domain.usecase.GetTodoItemsUseCase
import com.daniel.todoapp.domain.usecase.RemoveTodoItemUseCase
import com.daniel.todoapp.domain.usecase.UpdateTodoItemUseCase
import com.daniel.todoapp.domain.model.TodoItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


class TodoViewModel(
    application: Application,
    private val getTodoItemsUseCase: GetTodoItemsUseCase,
    private val createTodoItemUseCase: CreateTodoItemUseCase,
    private val removeTodoItemUseCase: RemoveTodoItemUseCase,
    private val updateTodoItemUseCase: UpdateTodoItemUseCase
) : AndroidViewModel(application) {

    private val _todoItems = MutableStateFlow<List<TodoItem>>(emptyList())
    val todoItems: StateFlow<List<TodoItem>> = _todoItems

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading


    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _showCompletedTasks = MutableStateFlow(false)
    val showCompletedTasks: StateFlow<Boolean> = _showCompletedTasks

    private val _deadLine = MutableStateFlow<Long?>(null)
    val deadLine: StateFlow<Long?> = _deadLine

    var lastTodoItem: TodoItem? = null


    private var currentRevision: Int = 0

    init {
        startPeriodUpdate()
        loadTodoItems()
    }

    private fun startPeriodUpdate() {

        val inputData = workDataOf(
            "item_id" to "some_id",
            "revision" to 0
        )

        val updateWorkRequest = PeriodicWorkRequestBuilder<UpdateDataWorker>(8, TimeUnit.HOURS)
            .setInitialDelay(10, TimeUnit.MINUTES)
            .setInputData(inputData)
            .build()

        WorkManager.getInstance(getApplication()).enqueueUniquePeriodicWork(
            "updateTodoItems",
            ExistingPeriodicWorkPolicy.KEEP,
            updateWorkRequest
        )
    }

    fun updateDeadLine(date: Long?) {
        _deadLine.value = date
    }

     fun loadTodoItems() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = getTodoItemsUseCase.execute()
                Log.d("TodoViewModel", "Fetched tasks: $response")
                currentRevision = response.revision
                _todoItems.value = response.list
            } catch (e: Exception) {
                _error.value = "Something went wrong"
                Log.e("TodoViewModel", "Error fetching tasks", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun toggleCompletedTasksVisibility() {
        _showCompletedTasks.value = !_showCompletedTasks.value
    }

//    fun addTodoItemWithRetry(item: TodoItem) {
//        viewModelScope.launch {
//            try {
//                repository.addItemWithRetry(item)
//                val response = repository.getAllItems()
//                _todoItems.value = response.list
//            } catch (e: Exception) {
//                _error.value = "Не удалось добавить задачу. Попробуйте еще раз."
//                Log.e("TodoViewModel", "Error adding task", e)
//            }
//        }
//    }

    fun retryLastAction() {
        lastTodoItem?.let { addTodoItem(it) }
    }

    fun addTodoItem(item: TodoItem) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = createTodoItemUseCase.execute(item, currentRevision)

                if (response.status == "ok") {
                    currentRevision = response.revision
                    _todoItems.value = _todoItems.value + response.list
                    _error.value = null
                } else {
                    _error.value = "Faile to add task"
                }
                Log.d("TodoViewModel", "Added Todo item: ${item.text}")
                lastTodoItem = null
                _error.value = ""
            } catch (e: Exception) {
                _error.value = "Something went wrong"
                lastTodoItem = item
                Log.e("TodoViewModel", "Error adding task", e)
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun removeTodoItem(item: TodoItem) {
        viewModelScope.launch {
            try {
                val response = removeTodoItemUseCase.execute(item, currentRevision)
                currentRevision = response.revision
                _todoItems.value = response.list
            } catch (e: Exception) {
                _error.value = "Something went wrong"
            }
        }
    }

    fun updateTodoItem(item: TodoItem) {
        viewModelScope.launch {
            try {
                val response = updateTodoItemUseCase.execute(item, currentRevision)
                Log.d("TodoViewModel", "Updated Todo item: ${item.text}")
                currentRevision = response.revision
                _todoItems.value = response.list
            } catch (e: Exception) {
                _error.value = "Something went wrong"
                Log.e("TodoViewModel", "Error updating task", e)
            }
        }
    }
}