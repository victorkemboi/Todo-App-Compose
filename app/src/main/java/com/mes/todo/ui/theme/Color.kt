package com.mes.todo.ui.theme

import androidx.compose.ui.graphics.Color

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)
val LightBlue = Color(0xFF4FC3F7)
val LightGreen = Color(0xFFA5D6A7)
val MutedBlue = Color(0xFF26A69A)
val MutedGreen = Color(0xFF66BB6A)
val MutedPurple = Color(0xFFAB47BC)
val MutedPink = Color(0xFFEC407A)
val MutedOrange = Color(0xFFFF7043)
val LightOrange = Color(0xFFFFAB91)


// Gradients
fun OrangeGradient(inverse: Boolean = false) = when (inverse) {
    true -> listOf(
        LightOrange,
        MutedOrange
    )
    false -> listOf(
        MutedOrange,
        LightOrange
    )
}

fun BluePinkGradient(inverse: Boolean = false) = when (inverse) {
    true -> listOf(
        MutedBlue,
        MutedPink
    )
    false -> listOf(
        MutedPink,
        MutedBlue
    )
}