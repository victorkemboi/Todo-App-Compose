package com.mes.todo.data.entities

import androidx.room.Entity
import androidx.room.Index
import com.mes.todo.utils.generateUuid
import java.util.*

@Entity(indices = [Index(value = ["id"], unique = true)])
data class Todo(
    var title: String,
    var dueDate: Date,
    var isDone: Boolean = false,
    val id: String = generateUuid()
)
