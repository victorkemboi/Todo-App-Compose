package com.mes.todo.data.entities

import com.mes.todo.utils.generateUuid
import java.util.*

data class Todo(
    var title: String,
    var dueDate: Date,
    var isDone: Boolean = false,
    val id: String = generateUuid()
)
