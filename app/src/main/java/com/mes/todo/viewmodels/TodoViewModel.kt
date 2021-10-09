package com.mes.todo.viewmodels

import androidx.lifecycle.ViewModel
import com.mes.todo.data.entities.Todo
import com.mes.todo.domain.usecases.TodoUseCase
import java.util.*
import kotlin.random.Random.Default.nextInt

class TodoViewModel(
    private val todoUseCase: TodoUseCase
) : ViewModel() {
    private suspend fun saveTodo(item: Todo) = todoUseCase.saveTodo(item)

    fun fetchTodos() = todoUseCase.fetchTodos()

    suspend fun addToDoItem() {
        saveTodo(
            Todo(
                title = "ToDo ${nextInt()}",
                dueDate = Date(),
                isDone = false
            )
        )
    }

    suspend fun updateTodo(todo: Todo) = todoUseCase.updateTodo(todo = todo)
    suspend fun deleteTodo(todo: Todo) = todoUseCase.deleteTodo(todo = todo)

    private suspend fun clearTodos() = todoUseCase.clearTodos()
}