package com.daniel.todoapp.data.repository

import com.daniel.todoapp.data.source.TodoApiSource
import com.daniel.todoapp.domain.model.TodoItem
import com.daniel.todoapp.domain.model.TodoItemResponse
import com.daniel.todoapp.domain.model.TodoListResponse
import com.daniel.todoapp.domain.repository.TodoRepository

class TodoRepositoryImpl(private val apiSource: TodoApiSource) : TodoRepository {

    override suspend fun getAllItems(): TodoListResponse {
        return apiSource.getTodoItems()
    }

    override suspend fun addItem(item: TodoItem, revision: Int): TodoItemResponse {
       return apiSource.addTodoItem(item, revision)
    }

    override suspend fun removeItem(item: TodoItem, revision: Int): TodoListResponse {
        return apiSource.removeTodoItem(item.id, revision)
    }

    override suspend fun updateItem(item: TodoItem, revision: Int): TodoListResponse {
        return apiSource.updateTodoItem(item.id, item, revision)
    }

    override suspend fun patchTodoList(list: List<TodoItem>, revision: Int): TodoListResponse {
        return apiSource.patchTodoList(list, revision)
    }
}
