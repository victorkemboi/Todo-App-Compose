package com.mes.todo.data.entities

import com.mes.todo.utils.generateUuid
import io.realm.RealmObject
import java.util.*

open class Todo(
    var title: String = "",
    var dueDate: Date = Date(),
    var isDone: Boolean = false,
    var id: String = generateUuid()
) : RealmObject()
