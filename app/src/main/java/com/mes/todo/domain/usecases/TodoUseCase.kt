package com.mes.todo.domain.usecases

import com.mes.todo.data.entities.Todo
import com.mes.todo.data.repositories.TodoRepository
import kotlinx.coroutines.flow.map

class TodoUseCase(
    private val todoRepository: TodoRepository
) {
    suspend fun saveTodo(item: Todo) = todoRepository.save(item)

    fun fetchDoneTodos() = todoRepository.fetchDoneTodos()
    fun fetchPendingTodos() = todoRepository.fetchPendingTodos()

    suspend fun updateTodo(todo: Todo) = todoRepository.update(todo)

    suspend fun deleteTodo(todo: Todo) = todoRepository.delete(todo)

    suspend fun clearTodos() = todoRepository.clearTodos()
}