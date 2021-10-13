package com.mes.todo.utils

import android.content.Context
import android.widget.Toast
import androidx.paging.compose.LazyPagingItems
import java.text.SimpleDateFormat
import java.util.*

fun Date.format(): String =
    SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(this).toString()

fun Long.formatAsDate(): String =
    Date(this).format()

fun generateUuid(): String = UUID.randomUUID().toString()

fun Random.nextInt(range: IntRange): Int = range.first + nextInt(range.last - range.first)

fun Context.toast(text: String, length: Int = Toast.LENGTH_LONG) = Toast.makeText(
    this, text, length
).show()

fun  <T: Any> LazyPagingItems<T>.isEmpty(): Boolean = when(itemCount) {
    0 -> true
    else -> false
}
