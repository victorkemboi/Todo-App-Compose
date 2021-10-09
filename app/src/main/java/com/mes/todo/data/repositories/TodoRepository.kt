package com.mes.todo.data.repositories

import com.mes.todo.data.DataBase
import com.mes.todo.data.entities.Todo

class TodoRepository(
    private val dataBase: DataBase = DataBase()
) {
    suspend fun save(item: Todo) {
        dataBase.insertTodo(item)
    }

    fun fetchToDos() = dataBase.toDosFlow
}