package com.daniel.todoapp.data.repository

import com.daniel.todoapp.domain.repository.TodoRepository
import com.daniel.todoapp.domain.model.TodoItem

class TodoItemRepository: TodoRepository {

    private val todoItems = mutableListOf<TodoItem>()

    override fun getAllItems(): List<TodoItem> = todoItems

    override fun addItem(item: TodoItem) {
        todoItems.add(item)
    }

    override fun removeItem(item: TodoItem) {
        todoItems.remove(item)
    }

    override fun updateItem(item: TodoItem) {
        todoItems.indexOfFirst { it.id == item.id }.takeIf { it != -1 }?.let {
            todoItems[it] = item
        }
    }
}
