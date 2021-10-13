package com.mes.todo

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.mes.todo.ui.composables.AddTodoPage
import com.mes.todo.ui.composables.TodosPage
import com.mes.todo.ui.theme.ToDoTheme

class MainActivity : AppCompatActivity() {
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberAnimatedNavController()
            ToDoTheme {
                AnimatedNavHost (navController = navController, startDestination = "todosPage") {
                    composable("todosPage") {
                        TodosPage(navController = navController)
                    }
                    composable("addTodoPage") {
                        AddTodoPage(navController = navController)
                    }
                }
            }
        }
    }
}


