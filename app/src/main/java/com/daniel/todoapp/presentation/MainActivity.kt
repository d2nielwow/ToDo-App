package com.daniel.todoapp.presentation

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.daniel.todoapp.BackgroundTaskManager
import com.daniel.todoapp.TodoApp
import com.daniel.todoapp.data.api.RetrofitClient
import com.daniel.todoapp.data.repository.TodoRepositoryImpl
import com.daniel.todoapp.di.AppModule
import com.daniel.todoapp.domain.usecase.CreateTodoItemUseCase
import com.daniel.todoapp.domain.usecase.GetTodoItemsUseCase
import com.daniel.todoapp.domain.usecase.RemoveTodoItemUseCase
import com.daniel.todoapp.domain.usecase.UpdateTodoItemUseCase
import com.daniel.todoapp.presentation.viewmodel.TodoViewModel
import com.daniel.todoapp.presentation.viewmodel.ViewModelFactory
import com.daniel.todoapp.ui.theme.ToDoAppTheme

class MainActivity : ComponentActivity() {

    private val backgroundTaskManager = BackgroundTaskManager(applicationContext)

    private val viewModel: TodoViewModel by viewModels {
        ViewModelFactory(
            application,
            AppModule.getTodoItemsUseCase,
            AppModule.createTodoItemUseCase,
            AppModule.removeTodoItemUseCase,
            AppModule.updateTodoItemUseCase,
            backgroundTaskManager
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
