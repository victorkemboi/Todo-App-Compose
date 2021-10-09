package com.mes.todo.data.converters

import androidx.room.TypeConverter
import java.util.*

object DateConverter {

    @TypeConverter
    @JvmStatic
    fun Long.toDate(): Date = Date(this)

    @TypeConverter
    @JvmStatic
    fun Date.toTimestamp(): Long = this.time
}