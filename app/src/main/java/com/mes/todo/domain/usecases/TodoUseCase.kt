package com.mes.todo.domain.usecases

import com.mes.todo.data.entities.Todo
import com.mes.todo.data.repositories.TodoRepository
import kotlinx.coroutines.flow.map

class TodoUseCase(
    private val todoRepository: TodoRepository
) {
    suspend fun saveTodo(item: Todo) = todoRepository.save(item)

    fun fetchTodos() = todoRepository.fetchAll().map {
        it.toList().sortedBy {todo -> todo.dueDate }
    }

    fun clearTodos() = todoRepository.clearTodos()
}