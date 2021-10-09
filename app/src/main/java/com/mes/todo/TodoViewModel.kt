package com.mes.todo

import com.mes.todo.data.entities.Todo
import com.mes.todo.domain.TodoUseCase
import kotlinx.coroutines.flow.first
import java.util.*

class TodoViewModel(
    private val todoUseCase: TodoUseCase = TodoUseCase()
) {
    suspend fun saveTodo(item: Todo) = todoUseCase.saveTodo(item)

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
}