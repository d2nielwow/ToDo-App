package com.daniel.todoapp.domain.repository

import com.daniel.todoapp.domain.model.TodoItem
import com.daniel.todoapp.domain.model.TodoItemResponse
import com.daniel.todoapp.domain.model.TodoListResponse

interface TodoRepository {
    suspend fun getAllItems(): TodoListResponse
    suspend fun addItem(item: TodoItem, revision: Int): TodoItemResponse
    suspend fun removeItem(item: TodoItem, revision: Int): TodoListResponse
    suspend fun updateItem(item: TodoItem, revision: Int): TodoListResponse
    suspend fun patchTodoList(list: List<TodoItem>, revision: Int): TodoListResponse
}