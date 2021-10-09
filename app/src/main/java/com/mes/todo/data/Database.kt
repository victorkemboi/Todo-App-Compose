package com.mes.todo.data

import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mes.todo.data.converters.DateConverter
import com.mes.todo.data.daos.TodoDao
import com.mes.todo.data.entities.Todo
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.HashMap
@androidx.room.Database(
    entities = [
        Todo::class,
    ],
    version = 1,
    exportSchema = false
)

@TypeConverters(
    DateConverter::class,
)
abstract class Database : RoomDatabase() {
    abstract fun TodoDao(): TodoDao
}

