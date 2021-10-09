package com.mes.todo.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mes.todo.data.entities.Todo
import com.mes.todo.domain.usecases.TodoUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.*

class TodoViewModel(
    private val todoUseCase: TodoUseCase
): ViewModel() {
    init {
        viewModelScope.launch {
            for (i in 1..10) {
                addToDoItem()
                delay(1500)
            }
        }
    }
    private suspend fun saveTodo(item: Todo) = todoUseCase.saveTodo(item)

    fun fetchTodos() = todoUseCase.fetchTodos()

    suspend fun addToDoItem() {
        saveTodo(
            Todo(
                title = "ToDo ${fetchTodos().first().size + 1}",
                dueDate = Date(),
                isDone = false
            )
        )
    }

    private fun clearTodos() = todoUseCase.clearTodos()
}