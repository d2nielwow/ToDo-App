package com.daniel.todoapp.data.repository

import com.daniel.todoapp.data.api.TodoApiService
import com.daniel.todoapp.domain.model.TodoItem
import com.daniel.todoapp.domain.model.TodoListResponse
import com.daniel.todoapp.domain.repository.TodoRepository
import retrofit2.HttpException
import java.io.IOException

class TodoRepositoryImpl(private val apiService: TodoApiService) : TodoRepository {

    private var currentRevision: Int = 0

    private suspend fun <T> handleApiResponse(
        apiCall: suspend () -> T,
        onSuccess: (T) -> TodoListResponse
    ): TodoListResponse {
        return try {
            val response = apiCall()
            onSuccess(response)
        } catch (e: IOException) {
            throw e
        } catch (e: HttpException) {
            throw e
        }
    }

    override suspend fun getAllItems(): TodoListResponse {
        return handleApiResponse(
            apiCall = { apiService.getTodoItems() },
            onSuccess = { response ->
                response.body()?.let {
                    TodoListResponse(
                        status = "ok",
                        list = it.list,
                        revision = it.revision
                    )
                } ?: TodoListResponse(
                    status = "error",
                    list = emptyList(),
                    revision = currentRevision
                )
            }
        )
    }

    override suspend fun addItem(item: TodoItem, revision: Int): TodoListResponse {
        return handleApiResponse(
            apiCall = { apiService.addTodoItem(revision, item) },
            onSuccess = { response ->
                val addedItem = response.body()
                addedItem?.let {
                    TodoListResponse(
                        status = "ok",
                        list = listOf(it),
                        revision = currentRevision
                    )
                } ?: TodoListResponse(status = "error", list = emptyList(), revision = currentRevision)
            }
        )
    }

    override suspend fun removeItem(item: TodoItem, revision: Int): TodoListResponse {
        return handleApiResponse(
            apiCall = { apiService.removeTodoItem(item.id, revision) },
            onSuccess = { response ->
                TodoListResponse(
                    status = "ok",
                    list = emptyList(),
                    revision = response.body()?.revision ?: currentRevision
                )
            }
        )
    }

    override suspend fun updateItem(item: TodoItem, revision: Int): TodoListResponse {
        return handleApiResponse(
            apiCall = { apiService.updateTodoItem(item.id, item, revision) },
            onSuccess = { response ->
                TodoListResponse(
                    status = "ok",
                    list = emptyList(),
                    revision = response.body()?.revision ?: currentRevision
                )
            }
        )
    }

    override suspend fun patchTodoList(list: List<TodoItem>, revision: Int): TodoListResponse {
        return handleApiResponse(
            apiCall = { apiService.patchTodoList(list, revision) },
            onSuccess = { response ->
                TodoListResponse(
                    status = "ok",
                    list = response.body()?.list ?: emptyList(),
                    revision = response.body()?.revision ?: 0
                )
            }
        )
    }
}
