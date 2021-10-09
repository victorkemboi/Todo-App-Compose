package com.mes.todo.utils

import java.text.SimpleDateFormat
import java.util.*

fun Date.format(): String =
    SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(this).toString()

fun generateUuid(): String = UUID.randomUUID().toString()