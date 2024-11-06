package com.daniel.todoapp.data.usecase

import com.daniel.todoapp.domain.repository.TodoRepository
import com.daniel.todoapp.domain.model.TodoItem

class UpdateTodoItemUseCase(private val repository: TodoRepository) {
    fun execute(item: TodoItem) = repository.updateItem(item)
}