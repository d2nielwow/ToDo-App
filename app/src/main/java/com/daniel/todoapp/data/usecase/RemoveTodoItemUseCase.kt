package com.daniel.todoapp.data.usecase

import com.daniel.todoapp.domain.repository.TodoRepository
import com.daniel.todoapp.domain.model.TodoItem

class RemoveTodoItemUseCase(private val repository: TodoRepository) {
    fun execute(item: TodoItem) = repository.removeItem(item)
}