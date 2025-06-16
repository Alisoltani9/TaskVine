package soltani.code.taskvine.ui.screens.dialogs


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import soltani.code.taskvine.helpers.LocalCustomColors

@Composable
fun AddCategoryDialog(
    isDialogOpen: Boolean,
    onDismiss: () -> Unit,
    onAddCategory: (String) -> Unit
) {
    var newCategoryName by remember { mutableStateOf("") }
    val colors = LocalCustomColors.current

    if (isDialogOpen) {
        Dialog(onDismissRequest = onDismiss) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = colors.cardBackground,
                modifier = Modifier.padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "Add New Category",
                        style = MaterialTheme.typography.titleMedium,
                        color = colors.primaryText
                    )

                    TextField(
                        value = newCategoryName,
                        onValueChange = { newCategoryName = it },
                        textStyle = TextStyle(
                            color = colors.primaryText,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        ),
                        placeholder = {
                            Text(
                                "Choose a Name!",
                                color = colors.primaryText,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleMedium,
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = colors.primaryText,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 0.dp)
                    )

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = onDismiss,
                            shape = RoundedCornerShape(5.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = colors.primaryText
                            ),
                            border = BorderStroke(1.dp, colors.primaryText)
                        ) {
                            Text("Cancel")
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Button(
                            onClick = {
                                onAddCategory(newCategoryName)
                                newCategoryName = ""
                                onDismiss()
                            },
                            shape = RoundedCornerShape(5.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = colors.primaryText
                            ),
                            border = BorderStroke(1.dp, colors.primaryText)
                        ) {
                            Text("Add")
                        }
                    }
                }
            }
        }
    }
}