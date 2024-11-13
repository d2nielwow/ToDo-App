package com.daniel.todoapp.data.repository

import com.daniel.todoapp.data.api.TodoApiService
import com.daniel.todoapp.domain.model.TodoItem
import com.daniel.todoapp.domain.model.TodoListResponse
import com.daniel.todoapp.domain.repository.TodoRepository
import kotlinx.coroutines.delay
import retrofit2.HttpException
import java.io.IOException

class TodoItemRepository(private val apiService: TodoApiService) : TodoRepository {

    private var currentRevision: Int = 0

    override suspend fun getAllItems(): TodoListResponse {
        return try {
            val response = apiService.getTodoItems()
            if (response.isSuccessful) {
                response.body()?.revision ?: 0
                return response.body() ?: TodoListResponse(
                    status = "error",
                    list = emptyList(),
                    currentRevision
                )
            } else {
                throw HttpException(response)
            }
        } catch (e: IOException) {
            throw e
        } catch (e: HttpException) {
            throw e
        }
    }

    suspend fun addItemWithRetry(item: TodoItem, maxRetries: Int = 3): TodoItem {
        var currentRetry = 0
        while (currentRetry < maxRetries) {
            try {
                val response = apiService.addTodoItem(item)
                if (response.isSuccessful) {
                    return response.body()!!
                } else {
                    throw HttpException(response)
                }
            } catch (e: Exception) {
                if (currentRetry == maxRetries - 1) {
                    throw e
                }
                currentRetry++
                delay(2000)
            }
        }
        throw Exception()
    }

    override suspend fun addItem(item: TodoItem, revision: Int): TodoListResponse {
        return try {
            val response = apiService.addTodoItem(item)
            val addedItem = response.body()
            addedItem?.let {
                currentRevision = it.createdAt.toInt() ?: revision
                return TodoListResponse(
                    status = "ok",
                    list = listOf(it),
                    revision = currentRevision
                )
            }
            TodoListResponse(status = "error", list = emptyList(), revision = currentRevision)

        } catch (e: IOException) {
            throw e
        }
    }

    override suspend fun removeItem(item: TodoItem, revision: Int): TodoListResponse {
        return try {
            val response = apiService.removeTodoItem(item.id, revision)
            if (response.isSuccessful) {
                response.body()?.revision ?: 0
                return response.body() ?: TodoListResponse(
                    status = "error",
                    list = emptyList(),
                    currentRevision
                )
            } else {
                throw HttpException(response)
            }
        } catch (e: IOException) {
            throw e
        } catch (e: HttpException) {
            throw e
        }
    }

    override suspend fun updateItem(item: TodoItem, revision: Int): TodoListResponse {
        return try {
            val response = apiService.updateTodoItem(item.id, item, revision)
            if (response.isSuccessful) {
                response.body()?.revision ?: 0
                return response.body() ?: TodoListResponse(
                    status = "error",
                    list = emptyList(),
                    currentRevision
                )
            } else {
                throw HttpException(response)
            }
        } catch (e: IOException) {
            throw e
        } catch (e: HttpException) {
            throw e
        }
    }

    override suspend fun patchTodoList(list: List<TodoItem>, revision: Int): TodoListResponse {
        return try {
            val response = apiService.patchTodoList(list, revision)
            if (response.isSuccessful) {
                response.body() ?: TodoListResponse(
                    status = "error",
                    list = emptyList(),
                    revision = 0
                )
            } else {
                throw HttpException(response)
            }
        } catch (e: IOException) {
            throw e
        } catch (e: HttpException) {
            throw e
        }
    }
}
