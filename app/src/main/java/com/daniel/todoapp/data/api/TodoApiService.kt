package com.daniel.todoapp.data.api

import com.daniel.todoapp.domain.model.TodoItem
import com.daniel.todoapp.domain.model.TodoListResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TodoApiService {

    @GET("list")
    suspend fun getTodoItems(): Response<TodoListResponse>

    @POST("list")
    suspend fun addTodoItem(
        @Header("X-Last-Known-Revision") revision: Int,
        @Body item: TodoItem
    ): Response<TodoItem>

    @PUT("list/{id}")
    suspend fun updateTodoItem(
        @Path("id") id: String,
        @Body item: TodoItem,
        @Header("X-Last-Known-Revision") revision: Int
    ): Response<TodoListResponse>

    @DELETE("list/{id}")
    suspend fun removeTodoItem(
        @Path("id") id: String,
        @Header("X-Last-Known-Revision") revision: Int
    ): Response<TodoListResponse>

    @PATCH("list")
    suspend fun patchTodoList(
        @Body list: List<TodoItem>,
        @Header("X-Last-Known-Revision") revision: Int
    ): Response<TodoListResponse>
}