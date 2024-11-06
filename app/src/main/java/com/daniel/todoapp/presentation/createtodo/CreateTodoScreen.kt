package com.daniel.todoapp.presentation.createtodo

import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.daniel.todoapp.R
import com.daniel.todoapp.domain.model.Importance
import com.daniel.todoapp.presentation.viewmodel.TodoViewModel
import com.daniel.todoapp.ui.theme.customTypography
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTodoScreen(
    viewModel: TodoViewModel,
    navController: NavController,
) {

    val context = LocalContext.current
    val textState = rememberSaveable { mutableStateOf("") }
    val importanceState = rememberSaveable { mutableStateOf(Importance.NORMAL) }
    val isSwitchChecked = rememberSaveable { mutableStateOf(false) }

    fun clearForm() {
        textState.value = ""
        importanceState.value = Importance.NORMAL
        viewModel.updateDeadLine("")
        isSwitchChecked.value = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.primary))
            .verticalScroll(rememberScrollState())
    ) {
        TopAppBar(
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_close),
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            navController.popBackStack()
                            clearForm()
                        }
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = stringResource(id = R.string.save).uppercase(),
                        color = colorResource(id = R.color.blue),
                        style = customTypography.titleMedium,
                        modifier = Modifier
                            .clickable(enabled = textState.value.isNotBlank()) {
                                if (textState.value.isNotBlank()) {
                                    viewModel.addItem(textState.value, importanceState.value, viewModel.deadLine)
                                    navController.popBackStack()
                                } else {
                                    Toast
                                        .makeText(
                                            context,
                                            "Пожалуйста, заполните описание задачи",
                                            Toast.LENGTH_SHORT
                                        )
                                        .show()
                                }
                                clearForm()
                            }
                            .alpha(if (textState.value.isNotBlank()) 1f else 0.5F)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                }
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = colorResource(id = R.color.primary)
            ),
        )
        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .background(Color.White),
        ) {

            TextField(
                value = textState.value,
                onValueChange = { newText ->
                    textState.value = newText
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 124.dp),
                textStyle = TextStyle(
                    fontSize = 18.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Normal
                ),
                placeholder = {
                    Text(
                        "Что надо сделать...",
                        color = colorResource(id = R.color.light_gray)
                    )
                },
                singleLine = false,
                maxLines = 15,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
        }

        ImportanceSelector(importanceState.value) { newImportance ->
            importanceState.value = newImportance
        }

        Divider(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 16.dp),
            color = Color.LightGray
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = stringResource(id = R.string.selectDate),
                style = customTypography.bodyLarge,
                modifier = Modifier
                    .clickable {
                        showDatePicker(context) { selectedDate ->
                            if (selectedDate.isNotEmpty()) {
                                viewModel.updateDeadLine(selectedDate)
                                isSwitchChecked.value = true
                            } else {
                                isSwitchChecked.value = false
                                viewModel.updateDeadLine("")
                            }
                        }
                    }
            )

            Spacer(modifier = Modifier.weight(1f))

            Switch(
                checked = isSwitchChecked.value,
                onCheckedChange = { isChecked ->
                    if (isChecked) {
                        showDatePicker(context) { selectDate ->
                            if (selectDate.isNotEmpty()) {
                                viewModel.updateDeadLine(selectDate)
                                isSwitchChecked.value = true
                            } else {
                                isSwitchChecked.value = false
                                viewModel.updateDeadLine("")
                            }
                        }
                    } else {
                        isSwitchChecked.value = false
                        viewModel.updateDeadLine("")
                    }
                },
                colors = androidx.compose.material3.SwitchDefaults.colors(
                    uncheckedThumbColor = colorResource(id = R.color.white),
                    uncheckedTrackColor = colorResource(id = R.color.light_gray),
                    checkedThumbColor = colorResource(id = R.color.white),
                    checkedTrackColor = colorResource(id = R.color.light_blue)
                )
            )
        }

        if (viewModel.deadLine.isNotBlank()) {
            Text(
                modifier = Modifier.padding(start = 16.dp),
                color = colorResource(id = R.color.blue),
                text = viewModel.deadLine,
            )
        }

        Divider(
            modifier = Modifier
                .padding(vertical = 30.dp),
            color = Color.LightGray
        )

        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .wrapContentWidth()
                .clickable(enabled = textState.value.isNotBlank()) { clearForm() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_delete),
                contentDescription = null,
                modifier = Modifier.alpha(if (textState.value.isNotBlank()) 1f else 0.5f)
            )
            Spacer(
                modifier = Modifier.width(8.dp)
            )
            Text(
                text = stringResource(id = R.string.delete),
                color = Color.Red,
                style = customTypography.bodyLarge,
                modifier = Modifier.alpha(if (textState.value.isNotBlank()) 1f else 0.5f)
            )
        }
    }
}

private fun showDatePicker(
    context: Context,
    onDateSelected: (String) -> Unit,
) {

    val calendar = Calendar.getInstance()

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val selectedDate = "$dayOfMonth ${getMonthName(month)} $year"
            onDateSelected(selectedDate)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    datePickerDialog.setOnShowListener {

        val positiveButton = datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE)
        positiveButton.setTextColor(ContextCompat.getColor(context, R.color.blue))

        val negativeButton = datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE)
        negativeButton.setTextColor(ContextCompat.getColor(context, R.color.blue))
    }
    datePickerDialog.show()
}

private fun getMonthName(month: Int): String {
    val months = arrayOf(
        "Янв", "Фев", "Мар", "Апр", "Май", "Июн",
        "Июл", "Авг", "Сен", "Окт", "Ноя", "Дек"
    )
    return months[month]
}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun ImportanceSelector(
    currentImportance: Importance,
    onImportanceChange: (Importance) -> Unit,
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = stringResource(id = R.string.importance),
            style = customTypography.bodyLarge,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 9.dp)
        )

        Row(
            modifier = Modifier
                .padding(end = 8.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.Gray.copy(alpha = 0.1f))
                .clip(RoundedCornerShape(12.dp))
                .padding(vertical = 5.dp, horizontal = 10.dp)
        ) {

            IconButton(
                onClick = { onImportanceChange(Importance.NORMAL) },
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        if (currentImportance == Importance.NORMAL) Color.White else Color.Transparent
                    )
                    .padding(8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_importance_no),
                    contentDescription = null,
                    tint = Color.LightGray
                )
            }

            IconButton(
                onClick = { onImportanceChange(Importance.LOW) },
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .size(40.dp)
                    .background(
                        if (currentImportance == Importance.LOW) Color.White else Color.Transparent
                    )
                    .padding(8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_importance_low),
                    contentDescription = null,
                    tint = Color.LightGray
                )
            }

            IconButton(
                onClick = { onImportanceChange(Importance.HIGH) },
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        if (currentImportance == Importance.HIGH) Color.White else Color.Transparent
                    )
                    .padding(8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_importance_high),
                    contentDescription = null,
                    tint = Color.Red
                )
            }
        }
    }
}