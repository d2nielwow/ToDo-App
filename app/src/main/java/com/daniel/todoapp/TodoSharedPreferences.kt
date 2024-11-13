package com.daniel.todoapp

import android.content.Context
import android.content.SharedPreferences

object TodoSharedPreferences {

    private const val PREFS_NAME = "todo_prefs"
    private const val KEY_REVISION = "current_revision"

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun getRevision(context: Context): Int {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getInt(KEY_REVISION, 0)
    }

    fun setRevision(context: Context, revision: Int) {
        val sharedPreferences = getSharedPreferences(context)
        sharedPreferences.edit().putInt(KEY_REVISION, revision).apply()
    }
}