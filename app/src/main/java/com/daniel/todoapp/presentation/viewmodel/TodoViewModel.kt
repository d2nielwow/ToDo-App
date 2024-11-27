package com.daniel.todoapp.presentation.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.daniel.todoapp.AppComponent
import com.daniel.todoapp.BackgroundTaskManager
import com.daniel.todoapp.data.network.UpdateDataWorker
import com.daniel.todoapp.domain.usecase.CreateTodoItemUseCase
import com.daniel.todoapp.domain.usecase.GetTodoItemsUseCase
import com.daniel.todoapp.domain.usecase.RemoveTodoItemUseCase
import com.daniel.todoapp.domain.usecase.UpdateTodoItemUseCase
import com.daniel.todoapp.domain.model.TodoItem
import com.daniel.todoapp.domain.model.TodoItemResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class TodoViewModel @Inject constructor(
    application: Application,
    private val getTodoItemsUseCase: GetTodoItemsUseCase,
    private val createTodoItemUseCase: CreateTodoItemUseCase,
    private val removeTodoItemUseCase: RemoveTodoItemUseCase,
    private val updateTodoItemUseCase: UpdateTodoItemUseCase,
    backgroundTaskManager: BackgroundTaskManager,
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

    private var currentRevision: Int = 0

    init {
       backgroundTaskManager.startPeriodicUpdate()
        loadTodoItems()
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

    fun addTodoItem(item: TodoItem, revision: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = createTodoItemUseCase.execute(item, revision)
                if (response.status == "ok") {
                    _todoItems.value = _todoItems.value.orEmpty() + response.element
                    _error.value = null
                } else {
                    _error.value = "Failed to add task"
                }
            } catch (e: Exception) {
                _error.value = "Something went wrong"
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
            _isLoading.value = true
            try {
                val response = updateTodoItemUseCase.execute(item, currentRevision)
                if (response.status == "ok") {
                    currentRevision = response.revision
                    _todoItems.value = _todoItems.value.map {
                        if (it.id == item.id) item else it
                    }
                    _error.value = null
                } else {
                    _error.value = "Failed to update task"
                }
            } catch (e: Exception) {
                _error.value = "Something went wrong"
            } finally {
                _isLoading.value = false
            }
        }
    }
}