package com.mes.todo.domain

import com.mes.todo.data.entities.Todo
import com.mes.todo.data.repositories.TodoRepository
import kotlinx.coroutines.flow.map

class TodoUseCase(
    private val todoRepository: TodoRepository = TodoRepository()
) {
    suspend fun saveTodo(item: Todo) {
        todoRepository.save(item)
    }

    fun fetchTodos() = todoRepository.fetchToDos().map {
        it.toList().sortedBy {todo -> todo.dueDate }
    }
}