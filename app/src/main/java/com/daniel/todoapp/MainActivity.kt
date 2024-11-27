package com.daniel.todoapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.daniel.todoapp.data.api.RetrofitClient
import com.daniel.todoapp.data.repository.TodoRepositoryImpl
import com.daniel.todoapp.domain.usecase.CreateTodoItemUseCase
import com.daniel.todoapp.domain.usecase.GetTodoItemsUseCase
import com.daniel.todoapp.domain.usecase.RemoveTodoItemUseCase
import com.daniel.todoapp.domain.usecase.UpdateTodoItemUseCase
import com.daniel.todoapp.presentation.viewmodel.TodoViewModel
import com.daniel.todoapp.presentation.viewmodel.ViewModelFactory
import com.daniel.todoapp.ui.theme.ToDoAppTheme

class MainActivity : ComponentActivity() {

    private val apiService = RetrofitClient.api
    private val repository = TodoRepositoryImpl(apiService)
    private val getTodoItemsUseCase = GetTodoItemsUseCase(repository)
    private val createTodoItemUseCase = CreateTodoItemUseCase(repository)
    private val removeTodoItemUseCase = RemoveTodoItemUseCase(repository)
    private val updateTodoItemUseCase = UpdateTodoItemUseCase(repository)

    private val viewModel: TodoViewModel by viewModels {
        ViewModelFactory(
            application,
            getTodoItemsUseCase,
            createTodoItemUseCase,
            removeTodoItemUseCase,
            updateTodoItemUseCase,
        )
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoAppTheme {
                TodoApp(viewModel)
            }
        }
    }
}
