package com.daniel.todoapp.domain.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.Date


data class TodoItem(
    @SerializedName("id") val id: String,
    @SerializedName("text") val text: String,
    @SerializedName("importance") val importance: String,
    @SerializedName("deadline") val deadLine: Long? = null,
    @SerializedName("done") val isCompleted: Boolean,
    @SerializedName("color") val color: String? = null,
    @SerializedName("created_at") val createdAt: Long,
    @SerializedName("changed_at") val modifiedAt: Long? = null,
    @SerializedName("last_updated_by") val lastUpdatedBy: String? = null
)


data class TodoListResponse(
  @SerializedName("status")  val status: String,
  @SerializedName("list")  val list: List<TodoItem>,
  @SerializedName("revision") val revision: Int
)

enum class Importance {
    LOW, NORMAL, HIGH
}
