package soltani.code.taskvine.ui.component.bottomSheetContent

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import soltani.code.taskvine.helpers.LocalCustomColors
import soltani.code.taskvine.ui.component.CustomDropdownMenuItem

@Composable
fun CategoryDropdownBs(
    expanded: Boolean,
    taskCategory: String,
    categoryList: List<String>,
    onTaskCategoryChange: (String) -> Unit,
    onDialogOpen: () -> Unit,
    onDropdownExpand: (Boolean) -> Unit,
    onLongPressCategory: (String) -> Unit
) {
    val colors = LocalCustomColors.current

    Column {
        Button(
            onClick = { onDropdownExpand(!expanded) },
            shape = RoundedCornerShape(5.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = colors.primaryText),
            border = BorderStroke(1.dp, colors.primaryText),
            modifier = Modifier.size(width = 120.dp, height = 40.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center, // Center content horizontally
                modifier = Modifier.fillMaxSize() // Fill button's available size
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.List, // Leading icon
                    contentDescription = "Category Icon",
                    tint = colors.primaryText,
                    modifier = Modifier
                        .size(20.dp) // Adjust icon size
                        .offset((-15).dp)

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

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onDropdownExpand(false) },
            modifier = Modifier
                .background(colors.SecondaryBackground) // Use surface color for the dropdown background
                .width(150.dp)
        ) {
            categoryList.forEach { category ->
                CustomDropdownMenuItem(
                    text = { Text(category, color = colors.primaryText) },
                    onClick = {
                        onTaskCategoryChange(if (category == "No Category") "" else category)
                        onDropdownExpand(false)
                    },
                    onLongPress = { onLongPressCategory(category) }
                )
            }
            DropdownMenuItem(
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Add icon only for "Add More" category
                        Icon(
                            imageVector = Icons.Filled.Add, // Change this to your desired icon
                            contentDescription = "Add More Icon",
                            tint = colors.highlight,
                            modifier = Modifier.size(20.dp)
                        )

                        Text("Add More", color = colors.highlight)
                    }
                },
                onClick = {
                    onDialogOpen()
                }
            )
        }
    }
}
