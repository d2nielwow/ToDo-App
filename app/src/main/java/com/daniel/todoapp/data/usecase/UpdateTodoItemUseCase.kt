package com.daniel.todoapp.data.usecase

import com.daniel.todoapp.domain.repository.TodoRepository
import com.daniel.todoapp.domain.model.TodoItem
import com.daniel.todoapp.domain.model.TodoListResponse

class UpdateTodoItemUseCase(private val repository: TodoRepository) {
  suspend  fun execute(item: TodoItem, revision: Int): TodoListResponse {
      return  repository.updateItem(item, revision)
    }
}