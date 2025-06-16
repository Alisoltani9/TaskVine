package soltani.code.taskvine.ui.screens.dialogs


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import soltani.code.taskvine.helpers.LocalCustomColors

@Composable
fun EmailSignupAuthDialog(
    isVisible: Boolean,
    isSignUp: Boolean,
    onDismiss: () -> Unit,
    onSubmit: (email: String, password: String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val colors = LocalCustomColors.current

    if (isVisible) {
        Dialog(onDismissRequest = onDismiss) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = colors.cardBackground,
                modifier = Modifier.padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = if (isSignUp) "Sign Up" else "Sign In",
                        style = MaterialTheme.typography.titleMedium,
                        color = colors.primaryText
                    )

                    TextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email", color = colors.primaryText) },
                        textStyle = TextStyle(color = colors.primaryText),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = colors.primaryText,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    TextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password", color = colors.primaryText) },
                        visualTransformation = PasswordVisualTransformation(),
                        textStyle = TextStyle(color = colors.primaryText),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = colors.primaryText,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row {
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
                                onSubmit(email.trim(), password.trim())
                                email = ""
                                password = ""
                            },
                            shape = RoundedCornerShape(5.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = colors.primaryText
                            ),
                            border = BorderStroke(1.dp, colors.primaryText)
                        ) {
                            Text(if (isSignUp) "Sign Up" else "Sign In")
                        }
                    }
                }
            }
        }
    }
}
