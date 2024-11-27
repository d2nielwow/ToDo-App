package com.daniel.todoapp.domain.usecase

import com.daniel.todoapp.data.repository.TodoRepositoryImpl
import com.daniel.todoapp.domain.model.TodoItem
import com.daniel.todoapp.domain.model.TodoItemResponse
import com.daniel.todoapp.domain.model.TodoListResponse

class CreateTodoItemUseCase(private val repository: TodoRepositoryImpl) {
   suspend fun execute(item: TodoItem, revision: Int): TodoItemResponse {
        return repository.addItem(item, revision)
    }
}