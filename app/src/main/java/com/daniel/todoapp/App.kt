package com.daniel.todoapp

import android.app.Application

class App: Application() {

    val appComponent: AppComponent by lazy {

    }

    override fun onCreate() {
        super.onCreate()
    }
}