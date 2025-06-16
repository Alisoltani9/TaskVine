package soltani.code.taskvine.ui.component.bottomSheetContent

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import soltani.code.taskvine.helpers.LocalCustomColors

@Composable
fun TaskInputFieldsBs(
    taskTitle: String,
    taskDescription: String,
    onTaskTitleChange: (String) -> Unit,
    onTaskDescriptionChange: (String) -> Unit,
    focusRequester: FocusRequester
) {
    val colors = LocalCustomColors.current

    TextField(
        value = taskTitle,
        onValueChange = onTaskTitleChange,
        textStyle = TextStyle(
            color = colors.primaryText,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        ),
        placeholder = {
            Text(
                "Choose a Title For Your Task!",
                color = colors.primaryText,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
            )
        }, colors = textFieldColors(),
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
    )

    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    TextField(
        value = taskDescription,
        onValueChange = onTaskDescriptionChange,
        textStyle = TextStyle(
            color = colors.highlight,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
        ),
        placeholder = {
            Text(
                "Description",
                color = colors.highlight,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.titleMedium,
            )
        },
        colors = textFieldColors(),
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = (-16).dp)
    )
}

@Composable
private fun textFieldColors(): TextFieldColors {
    val colors = LocalCustomColors.current
    return TextFieldDefaults.colors(

        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        focusedLabelColor = colors.primaryText,
        unfocusedLabelColor = colors.primaryText.copy(alpha = 0.6f)
    )
}
