package com.daniel.todoapp

import com.daniel.todoapp.di.AppModule
import com.daniel.todoapp.presentation.MainActivity
import com.daniel.todoapp.presentation.viewmodel.TodoViewModel
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(todoViewModel: TodoViewModel)
    fun inject(activity: MainActivity)
}