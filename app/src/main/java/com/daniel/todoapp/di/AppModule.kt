package com.daniel.todoapp.di

import com.daniel.todoapp.data.api.RetrofitClient
import com.daniel.todoapp.data.api.TodoApiService
import com.daniel.todoapp.data.repository.TodoRepositoryImpl
import com.daniel.todoapp.data.source.TodoApiSource
import com.daniel.todoapp.domain.repository.TodoRepository
import com.daniel.todoapp.domain.usecase.CreateTodoItemUseCase
import com.daniel.todoapp.domain.usecase.GetTodoItemsUseCase
import com.daniel.todoapp.domain.usecase.RemoveTodoItemUseCase
import com.daniel.todoapp.domain.usecase.UpdateTodoItemUseCase
import dagger.Provides
import javax.inject.Singleton

object AppModule {

    @Provides
    @Singleton
    fun provideRetrofitClient(): TodoApiService {
        return RetrofitClient.api
    }

    @Provides
    @Singleton
    fun provideTodoRepository(apiSource: TodoApiSource): TodoRepository {
        return TodoRepositoryImpl(apiSource)
    }

    @Provides
    @Singleton
    fun provideApiSource(apiService: TodoApiService): TodoApiSource {
        return TodoApiSource(apiService)
    }

    @Provides
    @Singleton
    fun provideCreateTodoItemUseCase(todoRepository: TodoRepositoryImpl): CreateTodoItemUseCase {
        return CreateTodoItemUseCase(todoRepository)
    }

    @Provides
    @Singleton
    fun provideGetTodoItemsUseCase(todoRepository: TodoRepositoryImpl): GetTodoItemsUseCase {
        return GetTodoItemsUseCase(todoRepository)
    }

    @Provides
    @Singleton
    fun provideRemoveTodoItemUseCase(todoRepository: TodoRepositoryImpl): RemoveTodoItemUseCase {
        return RemoveTodoItemUseCase(todoRepository)
    }

    @Provides
    @Singleton
    fun provideUpdateTodoItemUseCase(todoRepository: TodoRepositoryImpl): UpdateTodoItemUseCase {
        return UpdateTodoItemUseCase(todoRepository)
    }
}