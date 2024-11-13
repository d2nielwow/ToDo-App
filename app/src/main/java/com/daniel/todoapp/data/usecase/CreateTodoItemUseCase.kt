package com.daniel.todoapp.data.usecase

import com.daniel.todoapp.data.repository.TodoItemRepository
import com.daniel.todoapp.domain.model.TodoItem
import com.daniel.todoapp.domain.model.TodoListResponse

class CreateTodoItemUseCase(private val repository: TodoItemRepository) {
   suspend fun execute(item: TodoItem, revision: Int): TodoListResponse {
        return repository.addItem(item, revision)
    }
}