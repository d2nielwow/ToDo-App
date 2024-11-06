package com.daniel.todoapp.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.daniel.todoapp.data.usecase.CreateTodoItemUseCase
import com.daniel.todoapp.data.usecase.GetTodoItemsUseCase
import com.daniel.todoapp.data.usecase.RemoveTodoItemUseCase
import com.daniel.todoapp.data.usecase.UpdateTodoItemUseCase
import com.daniel.todoapp.domain.model.Importance
import com.daniel.todoapp.domain.model.TodoItem

class TodoViewModel(
    private val getTodoItemsUseCase: GetTodoItemsUseCase,
    private val createTodoItemUseCase: CreateTodoItemUseCase,
    private val removeTodoItemUseCase: RemoveTodoItemUseCase,
    private val updateTodoItemUseCase: UpdateTodoItemUseCase,
) : ViewModel() {

    private val _todoItems = mutableStateOf<List<TodoItem>>(emptyList())
    val todoItems: State<List<TodoItem>> get() = _todoItems

    private val _showCompletedTasks = mutableStateOf(true)
    val showCompletedTasks: State<Boolean> get() = _showCompletedTasks

    var completedCount by mutableIntStateOf(0)
        private set

    var deadLine by mutableStateOf("")
        private set

    init {
        loadItems()
    }

    fun updateDeadLine(newText: String) {
        deadLine = newText
    }

    fun loadItems() {
        val items = getTodoItemsUseCase.execute()
        _todoItems.value = items
        updateCompletedCount()
    }

    fun toggleCompletedTasksVisibility() {
        _showCompletedTasks.value = !_showCompletedTasks.value
    }

    fun addItem(text: String, importance: Importance, deadLine: String?) {
        val newTodoItem = TodoItem(
            id = java.util.UUID.randomUUID().toString(),
            text = text,
            importance = importance,
            isCompleted = false,
            createdAt = java.util.Date(),
            modifiedAt = null,
            deadLine = deadLine
        )
        createTodoItemUseCase.execute(newTodoItem)
        loadItems()
    }

    fun removeItem(item: TodoItem) {
        removeTodoItemUseCase.execute(item)
        loadItems()
    }

    fun updateTaskCompletion(item: TodoItem) {
        updateTodoItemUseCase.execute(item)
        loadItems()
    }

    private fun updateCompletedCount() {
        completedCount = _todoItems.value.count { it.isCompleted }
    }
}