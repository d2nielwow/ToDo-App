package com.daniel.todoapp


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.daniel.todoapp.presentation.viewmodel.TodoViewModel
import com.daniel.todoapp.presentation.createtodo.CreateTodoScreen
import com.daniel.todoapp.presentation.listtodo.TodoListScreen

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun TodoApp(viewModel: TodoViewModel) {

    val navController = rememberNavController()
    NavHost(navController, startDestination = "todoList") {
        composable("todoList") { TodoListScreen(viewModel, navController) }
        composable("createTodo") { CreateTodoScreen(viewModel, navController) }
    }
}