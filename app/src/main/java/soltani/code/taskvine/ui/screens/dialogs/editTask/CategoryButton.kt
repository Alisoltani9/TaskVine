package soltani.code.taskvine.ui.screens.dialogs.editTask

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import soltani.code.taskvine.helpers.LocalCustomColors

@Composable
fun CategoryButton(
    taskCategory: String,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit
) {
    val colors = LocalCustomColors.current

    Button(
        onClick = { onExpandedChange(!expanded) },
        shape = RoundedCornerShape(5.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = colors.primaryText),
        border = BorderStroke(1.dp, colors.primaryText),
        modifier = Modifier.size(width = 120.dp, height = 40.dp) // Adjust size as needed
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.List, // Leading icon
                contentDescription = "Category Icon",
                tint = colors.primaryText,
                modifier = Modifier
                    .size(20.dp) // Adjust icon size
                    .offset(x = (-15).dp)
            )
            Text(
                text = taskCategory.ifEmpty { "Category" },
                color = colors.primaryText,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .requiredSize(120.dp, 20.dp)
            )
        }
    }
}

