package com.daniel.todoapp.domain.repository

import com.daniel.todoapp.domain.model.TodoItem

interface TodoRepository {
    fun getAllItems(): List<TodoItem>
    fun addItem(item: TodoItem)
    fun removeItem(item: TodoItem)
    fun updateItem(item: TodoItem)
}