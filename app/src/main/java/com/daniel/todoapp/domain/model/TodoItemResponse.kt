package com.daniel.todoapp.domain.model

import com.google.gson.annotations.SerializedName

data class TodoItemResponse(
    @SerializedName("status")  val status: String,
    @SerializedName("element")  val element: TodoItem,
    @SerializedName("revision") val revision: Int
)
