package com.daniel.todoapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.daniel.todoapp.data.usecase.CreateTodoItemUseCase
import com.daniel.todoapp.data.usecase.GetTodoItemsUseCase
import com.daniel.todoapp.data.usecase.RemoveTodoItemUseCase
import com.daniel.todoapp.data.usecase.UpdateTodoItemUseCase

class ViewModelFactory(
    private val getTodoItemsUseCase: GetTodoItemsUseCase,
    private val createTodoItemUseCase: CreateTodoItemUseCase,
    private val removeTodoItemUseCase: RemoveTodoItemUseCase,
    private val updateTodoItemUseCase: UpdateTodoItemUseCase,

) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodoViewModel::class.java)) {
            return TodoViewModel(
                getTodoItemsUseCase,
                createTodoItemUseCase,
                removeTodoItemUseCase,
                updateTodoItemUseCase,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}