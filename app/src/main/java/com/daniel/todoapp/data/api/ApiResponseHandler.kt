package com.daniel.todoapp.data.api

import android.util.Log
import com.daniel.todoapp.domain.model.TodoListResponse
import retrofit2.HttpException
import java.io.IOException

class ApiResponseHandler {
    suspend fun <T> handleApiResponse(
        apiCall: suspend () -> T,
        onSuccess: (T) -> TodoListResponse
    ): TodoListResponse {
        return try {
            val response = apiCall()
            onSuccess(response)
        } catch (e: IOException) {
            Log.e("API_ERROR", "Network error: ${e.message}")
            throw e
        } catch (e: HttpException) {
            Log.e("API_ERROR", "HTTP error: ${e.message()}")
            throw e
        }
    }
}