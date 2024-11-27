package com.daniel.todoapp.domain.usecase

import com.daniel.todoapp.domain.repository.TodoRepository
import com.daniel.todoapp.domain.model.TodoListResponse

class GetTodoItemsUseCase(private val repository: TodoRepository) {
    suspend fun execute() : TodoListResponse {
        return repository.getAllItems()
    }
}