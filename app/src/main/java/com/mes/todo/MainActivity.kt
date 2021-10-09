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
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.mes.todo.data.entities.Todo
import com.mes.todo.ui.theme.ToDoTheme
import com.mes.todo.utils.TodoStateConstants.DONE
import com.mes.todo.utils.TodoStateConstants.PENDING
import com.mes.todo.utils.format
import com.mes.todo.utils.safeLaunch
import com.mes.todo.viewmodels.TodoViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
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
                    val coroutineScope = rememberCoroutineScope()
                    val (addFab, content) = createRefs()
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 16.dp, bottom = 16.dp)
                    ) {

                        Text(
                            text = "Tasks",
                            fontSize = 24.sp,
                            modifier = Modifier
                                .padding(16.dp)
                        )

                        val languageOptions: List<String> = listOf(PENDING, DONE)

                        val selectedLanguage = radioGroup(
                            radioOptions = languageOptions,
                            title = "State:",
                            cardBackgroundColor = Color(0xFFFFFAF0),
                        )
                        TodoItems(
                            todoItems = when (selectedLanguage) {
                                PENDING -> todoViewModel.fetchPendingTodos()
                                else -> todoViewModel.fetchDoneTodos()
                            },
                            onMarkToDoDone = todoViewModel::updateTodo,
                            onDeleteTodo = todoViewModel::deleteTodo,
                            coroutineScope = coroutineScope,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(
                                    top = 18.dp,
                                    bottom = 18.dp,
                                    start = 8.dp,
                                    end = 8.dp,
                                )
                        )
                    }

                    FloatingActionButton(
                        onClick = {
                            coroutineScope.safeLaunch {
                                todoViewModel.addToDoItem()
                            }
                        },
                        shape = RoundedCornerShape(14.dp),
                        backgroundColor = Color.Blue,
                        modifier = Modifier
                            .constrainAs(addFab) {
                                bottom.linkTo(parent.bottom)
                                end.linkTo(parent.end)
                            }
                            .offset(x = (-16).dp, y = (-24).dp)

                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_add),
                            contentDescription = "Add icon",
                            tint = Color.Green
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
    onDeleteTodo: suspend (todo: Todo) -> Unit,
    coroutineScope: CoroutineScope,
    modifier: Modifier = Modifier
) {
    val lazyItems = todoItems.collectAsLazyPagingItems()
    SwipeRefresh(
        state = rememberSwipeRefreshState(
            lazyItems.loadState.append == LoadState.Loading
        ),
        onRefresh = { lazyItems.refresh() },
        modifier = modifier
    ) {
        LazyColumn(
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
                        onDeleteTodo = onDeleteTodo,
                        coroutineScope = coroutineScope,
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
    onDeleteTodo: suspend (todo: Todo) -> Unit,
    coroutineScope: CoroutineScope,
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
            val (icon, toDoDetailsColumn, deleteBtn) = createRefs()
            val context = LocalContext.current
            Image(
                modifier = Modifier
                    .clickable {
                        if (!todo.isDone) {
                            coroutineScope.safeLaunch {
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
                        }
                    }
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

            Image(
                modifier = Modifier
                    .clickable {
                        coroutineScope.safeLaunch {
                            Toast
                                .makeText(
                                    context,
                                    "Deleted: ${todo.title}",
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                            onDeleteTodo(todo)
                        }
                    }
                    .height(48.dp)
                    .width(48.dp)
                    .padding(12.dp)
                    .constrainAs(deleteBtn) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    },
                painter = painterResource(id = R.drawable.ic_delete),
                contentDescription = "Delete",
                contentScale = ContentScale.Crop,
                colorFilter = ColorFilter.tint(Color.Red)
            )

        }
    }
}


@Composable
fun radioGroup(
    radioOptions: List<String> = listOf(),
    title: String = "",
    cardBackgroundColor: Color = Color(0xFFFEFEFA),
    modifier: Modifier = Modifier
): String {
    if (radioOptions.isNotEmpty()) {
        val (selectedOption, onOptionSelected) = remember {
            mutableStateOf(radioOptions[0])
        }

        Card(
            backgroundColor = cardBackgroundColor,
            modifier = modifier
                .padding(10.dp)
                .fillMaxWidth(),
            elevation = 8.dp,
            shape = RoundedCornerShape(12.dp),
        ) {
            Column(
                Modifier.padding(10.dp)
            ) {
                Text(
                    text = title,
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(5.dp),
                )

                Row(
                    Modifier.padding(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    radioOptions.forEach { item ->
                        Row(
                            Modifier.padding(5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (item == selectedOption),
                                onClick = { onOptionSelected(item) }
                            )

                            val annotatedString = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(fontWeight = FontWeight.Bold)
                                ) { append("  $item  ") }
                            }

                            ClickableText(
                                text = annotatedString,
                                onClick = {
                                    onOptionSelected(item)
                                }
                            )
                        }
                    }
                }
            }
        }
        return selectedOption
    } else {
        return ""
    }
}