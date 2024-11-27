package com.daniel.todoapp.data.source

import com.daniel.todoapp.data.api.TodoApiService
import com.daniel.todoapp.domain.model.TodoItem
import com.daniel.todoapp.domain.model.TodoItemResponse
import com.daniel.todoapp.domain.model.TodoListResponse
import javax.inject.Inject

class TodoApiSource @Inject constructor(private val apiService: TodoApiService) {

    suspend fun getTodoItems(): TodoListResponse {
        val response = apiService.getTodoItems()
        return if (response.isSuccessful) {
            response.body() ?: TodoListResponse(status = "error", list = emptyList(), revision = 0)
        } else {
            TodoListResponse(status = "error", list = emptyList(), revision = 0)
        }
    }

    suspend fun addTodoItem(item: TodoItem, revision: Int): TodoItemResponse {
        val response = apiService.addTodoItem(revision, item)
        return if (response.isSuccessful) {
            response.body() ?: TodoItemResponse(status = "error", element = item, revision = revision)
        } else {
            throw Exception("Failed to add Todo item")
        }
    }

    suspend fun updateTodoItem(id: String, item: TodoItem, revision: Int): TodoListResponse {
        val response = apiService.updateTodoItem(id, item, revision)
        return if (response.isSuccessful) {
            response.body() ?: TodoListResponse(status = "error", list = emptyList(), revision = revision)
        } else {
            TodoListResponse(status = "error", list = emptyList(), revision = revision)
        }
    }

    suspend fun removeTodoItem(id: String, revision: Int): TodoListResponse {
        val response = apiService.removeTodoItem(id, revision)
        return if (response.isSuccessful) {
            response.body() ?: TodoListResponse(status = "error", list = emptyList(), revision = revision)
        } else {
            TodoListResponse(status = "error", list = emptyList(), revision = revision)
        }
    }

    suspend fun patchTodoList(list: List<TodoItem>, revision: Int): TodoListResponse {
        val response = apiService.patchTodoList(list, revision)
        return if (response.isSuccessful) {
            response.body() ?: TodoListResponse(status = "error", list = emptyList(), revision = revision)
        } else {
            TodoListResponse(status = "error", list = emptyList(), revision = revision)
        }
    }
}