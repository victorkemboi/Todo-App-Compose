package com.mes.todo.utils

import java.text.SimpleDateFormat
import java.util.*

fun Date.format(): String =
    SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(this).toString()

fun generateUuid(): String = UUID.randomUUID().toString()

fun Random.nextInt(range: IntRange): Int = range.first + nextInt(range.last - range.first)