package com.daniel.todoapp.di

import com.daniel.todoapp.data.api.RetrofitClient
import com.daniel.todoapp.data.repository.TodoRepositoryImpl
import com.daniel.todoapp.data.source.TodoApiSource
import com.daniel.todoapp.domain.usecase.CreateTodoItemUseCase
import com.daniel.todoapp.domain.usecase.GetTodoItemsUseCase
import com.daniel.todoapp.domain.usecase.RemoveTodoItemUseCase
import com.daniel.todoapp.domain.usecase.UpdateTodoItemUseCase

object AppModule {

   private val apiService = RetrofitClient.api
   private val apiSource = TodoApiSource(apiService)
   private val repository = TodoRepositoryImpl(apiSource)

    val getTodoItemsUseCase = GetTodoItemsUseCase(repository)
    val createTodoItemUseCase = CreateTodoItemUseCase(repository)
    val removeTodoItemUseCase = RemoveTodoItemUseCase(repository)
    val updateTodoItemUseCase = UpdateTodoItemUseCase(repository)
}