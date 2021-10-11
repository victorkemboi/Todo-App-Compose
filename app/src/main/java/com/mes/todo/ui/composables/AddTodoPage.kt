package com.mes.todo.ui.composables

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.mes.todo.ui.theme.BluePinkGradient
import com.mes.todo.ui.theme.MutedPurple
import com.mes.todo.utils.formatAsDate
import com.mes.todo.utils.safeLaunch
import com.mes.todo.utils.toast
import com.mes.todo.viewmodels.AddTodoViewModel
import org.koin.androidx.compose.getViewModel
import java.util.*

@Composable
fun AddTodoPage(
    navController: NavHostController,
    addTodoViewModel: AddTodoViewModel = getViewModel()
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colors.background
            )
            .padding(16.dp)
    ) {
        var title: String by remember {
            mutableStateOf("")
        }
        var datePicked: Long? by remember {
            mutableStateOf(null)
        }
        val updatedDate = { date: Long? ->
            datePicked = date
        }

        val (saveBtn) = createRefs()

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {

            Text(
                text = "Add Task",
                fontSize = 24.sp,
            )

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") }
            )

            DatePickerView(datePicked?.formatAsDate(), updatedDate)
        }

        Button(
            colors = ButtonDefaults.textButtonColors(
                backgroundColor = MutedPurple
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp, end = 32.dp)
                .constrainAs(saveBtn) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .background(
                    brush = Brush.horizontalGradient(
                        colors = BluePinkGradient()
                    ),
                    shape = RoundedCornerShape(8.dp)
                ),
            onClick = {
                coroutineScope.safeLaunch {
                    if (title.isEmpty()) {
                        context.toast("Title cannot be empty!")
                        return@safeLaunch
                    }
                    if (datePicked == null) {
                        context.toast("Pick the due date!")
                        return@safeLaunch
                    }
                    addTodoViewModel.saveTodo(
                        title = title,
                        dueDate = Date(datePicked!!)
                    )
                    navController.navigate("todosPage") {
                        launchSingleTop = true
                    }
                }
            }
        ) {
            Text(text = "Save", color = Color.White)
        }
    }
}

@Composable
fun DatePickerView(
    datePicked: String?,
    updatedDate: (date: Long?) -> Unit,
) {
    val activity = LocalContext.current as AppCompatActivity

    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.TopStart)
            .padding(top = 10.dp)
            .border(0.5.dp, MaterialTheme.colors.onSurface.copy(alpha = 0.5f))
            .clickable {
                showDatePicker(activity, updatedDate)
            }
    ) {

        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            val (label, iconView) = createRefs()

            Text(
                text = datePicked ?: "Date Picker",
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(label) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(iconView.start)
                        width = Dimension.fillToConstraints
                    }
            )

            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = null,
                modifier = Modifier
                    .size(20.dp, 20.dp)
                    .constrainAs(iconView) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    },
                tint = MaterialTheme.colors.onSurface
            )

        }

    }
}

private fun showDatePicker(
    activity: AppCompatActivity,
    updatedDate: (Long?) -> Unit
) {
    val datePicker = MaterialDatePicker.Builder.datePicker().build()
    datePicker.show(activity.supportFragmentManager, datePicker.toString())
    datePicker.addOnPositiveButtonClickListener { datePickedLong ->
        val datePicked = Date(datePickedLong)
        MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .build()
            .apply {
                addOnPositiveButtonClickListener {
                    updatedDate(
                        Date(
                            datePicked.year, datePicked.month, datePicked.date,
                            this.hour, this.minute
                        ).time
                    )
                }
            }.show(activity.supportFragmentManager, MaterialTimePicker::class.java.canonicalName)
    }
//    AndroidView(
//        { CalendarView(it) },
//        modifier = Modifier.wrapContentWidth(),
//        update = { views ->
//            views.setOnDateChangeListener { calendarView, i, i2, i3 ->
//            }
//        }
//    )

}
