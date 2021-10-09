package com.mes.todo.data.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.mes.todo.utils.generateUuid
import java.util.*

@Entity(indices = [Index(value = ["id"], unique = true)])
data class Todo(
    var title: String,
    var dueDate: Date,
    var isDone: Boolean = false,
    var createdAt: Date = Calendar.getInstance().time,
    @PrimaryKey
    var id: String = generateUuid(),
)
