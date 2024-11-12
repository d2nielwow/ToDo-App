package com.daniel.todoapp.domain.model

import java.util.Date

data class TodoItem(
    val id: String,
    val text: String,
    val importance: Importance,
    val deadLine: String? = null,
    val isCompleted: Boolean,
    val createdAt: Date,
    val modifiedAt: Date? = null,
)

enum class Importance {
    LOW, NORMAL, HIGH
}
