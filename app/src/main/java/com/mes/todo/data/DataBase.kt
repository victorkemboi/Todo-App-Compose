package com.mes.todo.data

import com.mes.todo.data.entities.Todo
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.HashMap

class DataBase() {
    val todos: HashMap<String, Todo> = HashMap()

    val toDosFlow: MutableStateFlow<Collection<Todo>> = MutableStateFlow(todos.values)


    init {
        MainScope().launch {
            for (i in 0..10) {
                insertTodo(
                    Todo(
                        title = "ToDo ${todos.size + 1}",
                        dueDate = Date(),
                        isDone = false
                    )
                )
                delay(2000)
            }
        }
    }

    suspend fun insertTodo(todo: Todo) {
        todos[todo.id] = todo
        toDosFlow.emit(todos.values)
    }

    val observeTodos: StateFlow<Collection<Todo>>
        get() = toDosFlow
}

