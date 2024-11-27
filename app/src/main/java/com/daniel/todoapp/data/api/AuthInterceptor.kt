package com.daniel.todoapp.data.api

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val request = originalRequest.newBuilder()
            .addHeader("Authorization", "Bearer Legolin")
            .build()
        return chain.proceed(request)
    }
}