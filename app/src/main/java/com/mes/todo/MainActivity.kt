package com.mes.todo

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.mes.todo.data.entities.Todo
import com.mes.todo.ui.theme.ToDoTheme
import com.mes.todo.utils.format
import com.mes.todo.viewmodels.TodoViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MainActivity : ComponentActivity() {
    private val todoViewModel: TodoViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoTheme {
                Timber.d("Theming.")
                // A surface container using the 'background' color from the theme
                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            MaterialTheme.colors.background
                        )
                ) {
                    val (addFab, taskTitle, todos) = createRefs()
                    Text(
                        text = "Tasks",
                        fontSize = 24.sp,
                        modifier = Modifier
                            .padding(16.dp)
                            .constrainAs(taskTitle) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                            }
                    )

                    TodoItems(
                        todoItems = todoViewModel.fetchTodos(),
                        onMarkToDoDone = todoViewModel::updateTodo,
                        modifier = Modifier
                            .fillMaxSize()
                            .constrainAs(todos) {
                                top.linkTo(taskTitle.bottom)
                                bottom.linkTo(parent.bottom)
                            }
                            .padding(
                                top = 18.dp,
                                bottom = 18.dp,
                                start = 8.dp,
                                end = 8.dp,
                            )
                    )

                    FloatingActionButton(
                        onClick = {
                            lifecycleScope.launch {
                                todoViewModel.addToDoItem()
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
    todoItems: Flow<PagingData<Todo>>,
    onMarkToDoDone: suspend (todo: Todo) -> Unit,
    modifier: Modifier = Modifier
) {
    val lazyItems = todoItems.collectAsLazyPagingItems()
    SwipeRefresh(
        state = rememberSwipeRefreshState(
            lazyItems.loadState.append == LoadState.Loading
        ),
        onRefresh = { lazyItems.refresh() },
    ) {
        LazyColumn(
            modifier = modifier
        ) {
            items(lazyItems) { item ->
                item ?: return@items
                Box(
                    modifier = Modifier.padding(8.dp)
                ) {
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
                        todo = item,
                        onMarkToDoDone = onMarkToDoDone,
                        iconTint = if (item.isDone) {
                            Color.Green
                        } else {
                            Color.Blue
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TodoItem(
    painter: Painter,
    todo: Todo,
    onMarkToDoDone: suspend (todo: Todo) -> Unit,
    modifier: Modifier = Modifier,
    iconTint: Color = Color.Green,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = 5.dp
    ) {
        Timber.d("Todo item")
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            val (icon, toDoDetailsColumn) = createRefs()
            val context = LocalContext.current
            val coroutineScope = rememberCoroutineScope()
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
                    }
                    .clickable {
                        coroutineScope.launch {
                            Toast
                                .makeText(
                                    context,
                                    "Done for: ${todo.title}",
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                            onMarkToDoDone(todo.apply {
                                isDone = true
                            })
                        }
                    },
                painter = painter,
                contentDescription = todo.title,
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
                Text(text = todo.title, fontSize = 20.sp)
                Text(text = todo.dueDate.format(), fontSize = 14.sp)
            }

        }
    }
}