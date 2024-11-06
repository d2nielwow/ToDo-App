package com.daniel.todoapp.data.usecase

import com.daniel.todoapp.domain.repository.TodoRepository
import com.daniel.todoapp.domain.model.TodoItem

class GetTodoItemsUseCase(private val repository: TodoRepository) {
    fun execute(): List<TodoItem> = repository.getAllItems()
}