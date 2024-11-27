package com.daniel.todoapp.domain.model

import com.google.gson.annotations.SerializedName

data class TodoListResponse(
    @SerializedName("status")  val status: String,
    @SerializedName("list")  val list: List<TodoItem>,
    @SerializedName("revision") val revision: Int
)
