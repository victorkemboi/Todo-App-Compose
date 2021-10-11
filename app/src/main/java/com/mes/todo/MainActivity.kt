package com.mes.todo

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mes.todo.ui.composables.AddTodoPage
import com.mes.todo.ui.composables.TodosPage
import com.mes.todo.ui.theme.ToDoTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            ToDoTheme {
                NavHost(navController = navController, startDestination = "todosPage") {
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


