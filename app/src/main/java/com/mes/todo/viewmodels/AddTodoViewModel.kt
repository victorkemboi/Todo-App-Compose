package com.mes.todo.viewmodels

import androidx.lifecycle.ViewModel
import com.mes.todo.data.entities.Todo
import com.mes.todo.domain.usecases.TodoUseCase
import java.util.*

class AddTodoViewModel(
    private val todoUseCase: TodoUseCase
) : ViewModel() {

    suspend fun saveTodo(
        title: String,
        dueDate: Date
    ) = todoUseCase.saveTodo(
        Todo(title = title, dueDate = dueDate)
    )
}