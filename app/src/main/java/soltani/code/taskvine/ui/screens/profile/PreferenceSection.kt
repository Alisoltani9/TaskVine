package soltani.code.taskvine.ui.screens.profile

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import soltani.code.taskvine.helpers.LocalCustomColors
import soltani.code.taskvine.ui.theme.DarkCustomColors
import soltani.code.taskvine.ui.theme.GreenCustomColors
import soltani.code.taskvine.ui.theme.LightCustomColors

@Composable
fun PreferenceSection(
    onThemeChanged: (String) -> Unit,
    snackbarHostState: SnackbarHostState,

    ) {
    val context = LocalContext.current

    val sharedPreferences = context.getSharedPreferences("reminderSettings", Context.MODE_PRIVATE)
    val isSwitchChecked =
        remember { mutableStateOf(sharedPreferences.getBoolean("isReminderEnabled", true)) }
    val selectedTheme = remember {
        mutableStateOf(
            sharedPreferences.getString("themePreference", "Light") ?: "Light"
        )
    }
    val colors = LocalCustomColors.current


    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = colors.SecondaryBackground)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Text(
                text = "Preferences",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = colors.primaryText
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(text = "Reminders: ", color = colors.primaryText)
                Switch(
                    checked = isSwitchChecked.value,
                    onCheckedChange = { isChecked ->
                        isSwitchChecked.value = isChecked

                        // Save the state in SharedPreferences
                        sharedPreferences.edit().putBoolean("isReminderEnabled", isChecked).apply()

                        // Show a Snackbar with a dynamic message
                        CoroutineScope(Dispatchers.Main).launch {
                            snackbarHostState.showSnackbar(
                                message = if (isChecked) "Reminders enabled!" else "Reminders disabled.",
                                duration = SnackbarDuration.Short
                            )
                        }
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = colors.primaryText,
                        uncheckedThumbColor = colors.primaryText.copy(alpha = 0.3f),
                        checkedTrackColor = colors.primaryText.copy(alpha = 0.5f),
                        uncheckedTrackColor = colors.primaryText.copy(alpha = 0.1f),
                    )
                )

            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Theme: ",
                    color = colors.primaryText,
                    modifier = Modifier.padding(end = 8.dp)
                )

                val buttonModifier = Modifier
                    .padding(horizontal = 4.dp)
                    .height(30.dp)

                OutlinedButton(
                    onClick = {
                        selectedTheme.value = "Green"
                        onThemeChanged("Green")
                        sharedPreferences.edit().putString("themePreference", "Green").apply()


                    },
                    modifier = buttonModifier,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = if (selectedTheme.value == "Green") Color.White else Color.White,
                        containerColor = if (selectedTheme.value == "Green") GreenCustomColors.primaryText else GreenCustomColors.primaryText
                    ),
                    shape = RoundedCornerShape(50),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(text = "Green", fontSize = 12.sp)
                }

                OutlinedButton(
                    onClick = {
                        selectedTheme.value = "Dark"
                        onThemeChanged("Dark")
                        sharedPreferences.edit().putString("themePreference", "Dark").apply()

                    },
                    modifier = buttonModifier,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = if (selectedTheme.value == "Dark") Color.White else Color.White,
                        containerColor = if (selectedTheme.value == "Dark") DarkCustomColors.primaryBackground else DarkCustomColors.primaryBackground
                    ),
                    shape = RoundedCornerShape(50),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(text = "Dark", fontSize = 12.sp)
                }

                OutlinedButton(
                    onClick = {
                        selectedTheme.value = "Light"
                        onThemeChanged("Light")
                        sharedPreferences.edit().putString("themePreference", "Light").apply()


                    },
                    modifier = buttonModifier,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = if (selectedTheme.value == "Light") Color.Black else Color.Black,
                        containerColor = if (selectedTheme.value == "Light") LightCustomColors.primaryBackground else LightCustomColors.primaryBackground
                    ),
                    shape = RoundedCornerShape(50),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(text = "Light", fontSize = 12.sp)
                }

            }

        }
    }

}

