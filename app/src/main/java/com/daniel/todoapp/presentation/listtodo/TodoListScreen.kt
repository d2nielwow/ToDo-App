package com.daniel.todoapp.presentation.listtodo


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.daniel.todoapp.R
import com.daniel.todoapp.domain.model.Importance
import com.daniel.todoapp.domain.model.TodoItem
import com.daniel.todoapp.presentation.viewmodel.TodoViewModel
import com.daniel.todoapp.ui.theme.Typography


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun TodoListScreen(
    viewModel: TodoViewModel,
    navController: NavHostController,
) {

    val todoItems by viewModel.todoItems
    val completedCount = viewModel.completedCount
    val showCompletedTasks by viewModel.showCompletedTasks
    val listState = rememberLazyListState()


    Scaffold(
        modifier = Modifier
            .background(colorResource(id = R.color.primary)),
        topBar = {
            if (listState.firstVisibleItemIndex > 0) {
                TopAppBar(
                    title = {
                        AppBarTitle(viewModel, showCompletedTasks)
                    },
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = colorResource(id = R.color.primary)
                    )
                )
            }
        },

        content = { paddingValues ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(id = R.color.primary))
                    .padding(paddingValues)
            ) {

                if (listState.firstVisibleItemIndex == 0) {
                    Text(
                        text = stringResource(id = R.string.my_task),
                        color = Color.Black,
                        style = Typography.displaySmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 70.dp, top = 60.dp)
                    )
                }

                if (listState.firstVisibleItemIndex == 0) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 70.dp, end = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = "Выполнено — $completedCount",
                            fontSize = 18.sp,
                            color = colorResource(id = R.color.gray)
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        IconButton(onClick = { viewModel.toggleCompletedTasksVisibility() }) {
                            Icon(
                                painter = painterResource(id = if (showCompletedTasks) R.drawable.ic_visibility else R.drawable.ic_visibility_off),
                                contentDescription = null,
                                tint = colorResource(id = R.color.blue)
                            )
                        }
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp, horizontal = 8.dp)
                ) {

                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .background(Color.White)
                    ) {

                        items(todoItems.size) { index ->
                            val item = todoItems[index]
                            if (item.isCompleted && showCompletedTasks || !item.isCompleted) {
                                TaskItem(
                                    item = todoItems[index],
                                    onCheckedChange = { isChecked ->
                                        val updateItem =
                                            todoItems[index].copy(isCompleted = isChecked)
                                        viewModel.updateTaskCompletion(updateItem)
                                    }
                                )
                            }
                        }
                    }

                    Text(
                        text = stringResource(id = R.string.new_task),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .clickable {
                                navController.navigate("createTodo")
                            }
                            .padding(horizontal = 63.dp, vertical = 16.dp),
                        fontSize = 20.sp,
                        color = colorResource(id = R.color.light_gray)
                    )
                }
            }
        },

        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("createTodo") },
                modifier = Modifier
                    .padding(end = 16.dp, bottom = 56.dp)
                    .size(72.dp),
                containerColor = colorResource(id = R.color.blue),
                shape = RoundedCornerShape(36.dp)
            ) {
                Icon(
                    painterResource(id = R.drawable.ic_add), contentDescription = null,
                    tint = Color.White
                )
            }
        }
    )
}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun TaskItem(
    item: TodoItem,
    onCheckedChange: (Boolean) -> Unit,
) {

    val isChecked = remember {
        mutableStateOf(item.isCompleted)
    }

    val checkboxColor = when {
        item.importance == Importance.HIGH && !isChecked.value -> Color.Red
        else -> colorResource(id = R.color.light_gray)
    }
    val importanceColor = when (item.importance) {
        Importance.HIGH -> Color.Red
        else -> Color.LightGray
    }

    val showDialog = remember { mutableStateOf(false) }

    if (showDialog.value) {
        TaskDetailsDialog(
            item = item,
            onDismiss = { showDialog.value = false }
        )
    }

    val textColor = if (isChecked.value) colorResource(id = R.color.light_gray)
    else Color.Black

    val shouldShowImportance = !(isChecked.value && item.importance == Importance.HIGH)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Checkbox(
            checked = isChecked.value, onCheckedChange = {
                isChecked.value = it
                onCheckedChange(it)
            },
            colors = CheckboxDefaults.colors(
                checkedColor = colorResource(id = R.color.green),
                checkmarkColor = Color.White,
                uncheckedColor = checkboxColor
            )
        )

        Spacer(modifier = Modifier.width(8.dp))

        if (shouldShowImportance && item.importance != Importance.NORMAL) {
            val importanceIcon = when (item.importance) {
                Importance.LOW -> R.drawable.ic_importance_low
                Importance.HIGH -> R.drawable.ic_importance_high
                else -> R.drawable.ic_importance_no
            }

            Icon(
                painter = painterResource(id = importanceIcon),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = importanceColor
            )

            Spacer(modifier = Modifier.width(8.dp))
        }

        Column(
            modifier = Modifier
                .weight(1f),
        ) {
            Text(
                text = item.text,
                maxLines = 3,
                color = textColor,
                overflow = TextOverflow.Ellipsis,
                fontSize = 20.sp,
                style = TextStyle(
                    textDecoration = if (isChecked.value) TextDecoration.LineThrough else TextDecoration.None
                )
            )
        }

        item.deadLine?.let { deadline ->
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = deadline,
                color = colorResource(id = R.color.blue),
                style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal),
            )
        }

        IconButton(onClick = {
            showDialog.value = true
        }) {
            Icon(
                painterResource(id = R.drawable.ic_info), contentDescription = null,
                tint = colorResource(id = R.color.light_gray)
            )
        }
    }
}

@Composable
fun TaskDetailsDialog(
    item: TodoItem,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Подробности задачи") },
        text = {
            Column {
                Text("Текст задачи: ${item.text}")
                item.deadLine?.let { Text("Срок: $it") }
                Text("Важность: ${item.importance}")
                Text("Статус: ${if (item.isCompleted) "Выполнена" else "Не выполнена"}")
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.blue),
                    contentColor = Color.White
                )
            ) {
                Text("Закрыть")
            }
        },
        containerColor = Color.White,
        textContentColor = colorResource(id = R.color.black)
    )
}

@Composable
fun AppBarTitle(viewModel: TodoViewModel, showCompletedTasks: Boolean) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = stringResource(id = R.string.my_task),
            color = Color.Black,
            style = Typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 30.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        IconButton(
            onClick = { viewModel.toggleCompletedTasksVisibility() },
            modifier = Modifier.padding(top = 30.dp)
        ) {
            Icon(
                painter = painterResource(id = if (showCompletedTasks) R.drawable.ic_visibility else R.drawable.ic_visibility_off),
                contentDescription = null,
                tint = colorResource(id = R.color.blue)
            )
        }
    }
}