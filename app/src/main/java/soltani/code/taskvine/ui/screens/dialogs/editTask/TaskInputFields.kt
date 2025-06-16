package soltani.code.taskvine.ui.screens.dialogs.editTask

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import soltani.code.taskvine.helpers.LocalCustomColors

@Composable
fun TaskInputFields(
    taskTitle: String,
    taskDescription: String,
    onTaskTitleChange: (String) -> Unit,
    onTaskDescriptionChange: (String) -> Unit
) {
    val colors = LocalCustomColors.current

    TextField(
        value = taskTitle,
        onValueChange = onTaskTitleChange,
        placeholder = {
            Text(
                "Choose a Title For Your Task!",
                color = colors.primaryText,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
            )
        },
        textStyle = TextStyle(
            color = colors.primaryText,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        ),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent, // Match dialog surface
            unfocusedContainerColor = Color.Transparent, // Match dialog surface
            focusedIndicatorColor = colors.primaryText, // Dynamic color
            unfocusedIndicatorColor = Color.Transparent
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 0.dp)

    )
    TextField(
        value = taskDescription,
        onValueChange = onTaskDescriptionChange,

        placeholder = {
            Text(
                "Description",
                color = colors.primaryText,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.titleMedium,
            )
        },
        textStyle = TextStyle(
            color = colors.primaryText,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp
        ),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent, // Match dialog surface
            unfocusedContainerColor = Color.Transparent, // Match dialog surface
            focusedIndicatorColor = colors.primaryText, // Dynamic color
            unfocusedIndicatorColor = Color.Transparent
        ),
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = (-16).dp) // Negative offset to overlap
    )
}
