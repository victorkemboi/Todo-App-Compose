package com.mes.todo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import com.mes.todo.data.DataBase
import com.mes.todo.data.entities.Todo
import com.mes.todo.ui.theme.ToDoTheme
import com.mes.todo.utils.format
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : ComponentActivity() {
    val todoViewModel: TodoViewModel = TodoViewModel()
    val database: DataBase = DataBase()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoTheme {
                // A surface container using the 'background' color from the theme
                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            MaterialTheme.colors.background
                        )
                ) {
                    val (addFab) = createRefs()
                    val todosState by database.toDosFlow.map {
                        it.toList()
                    }.collectAsState(
                        initial = listOf()
                    )
                    TodoItems(items = todosState, modifier = Modifier.fillMaxSize())

                    FloatingActionButton(
                        onClick = {
                            lifecycleScope.launch {
                                database.insertTodo(
                                    Todo(
                                        title = "ToDo ${database.todos.size + 1}",
                                        dueDate = Date(),
                                        isDone = false
                                    )
                                )
                            }
                        },
                        modifier = Modifier
                            .constrainAs(addFab) {
                                bottom.linkTo(parent.bottom)
                                end.linkTo(parent.end)
                            }
                            .offset(x = (-16).dp, y = (-16).dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_add),
                            contentDescription = "Add icon"
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun TodoItems(
    items: List<Todo>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.padding(16.dp)
    ) {
        items(items = items) { item ->
            TodoItem(
                painter = painterResource(
                    id = if (
                        item.isDone
                    ) {
                        R.drawable.ic_done
                    } else {
                        R.drawable.ic_pending_action
                    }
                ),
                title = item.title,
                dueDate = item.dueDate.format(),
                contentDescription = item.title
            )
        }
    }
}

@Composable
fun TodoItem(
    painter: Painter,
    title: String,
    dueDate: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
    iconTint: Color = Color.Green,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = 5.dp
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            val (icon, toDoDetailsColumn) = createRefs()

            Image(
                modifier = Modifier
                    .height(48.dp)
                    .width(48.dp)

                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                iconTint,
                                Color.DarkGray
                            )
                        ),
                        RoundedCornerShape(12.dp)
                    )
                    .padding(12.dp)
                    .constrainAs(icon) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    },
                painter = painter,
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
                colorFilter = ColorFilter.tint(Color.White)
            )

            Column(
                modifier = Modifier
                    .constrainAs(toDoDetailsColumn) {
                        start.linkTo(icon.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
                    .padding(start = 16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = title, fontSize = 20.sp)
                Text(text = dueDate, fontSize = 14.sp)
            }

        }
    }
}