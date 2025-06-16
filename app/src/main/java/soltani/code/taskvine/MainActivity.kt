package soltani.code.taskvine

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.FirebaseApp
import dagger.hilt.android.AndroidEntryPoint
import soltani.code.taskvine.ui.screens.MainScreen
import soltani.code.taskvine.ui.theme.TaskVineTheme
import soltani.code.taskvine.viewmodel.AchievementProvider
import soltani.code.taskvine.viewmodel.AuthViewModel
import soltani.code.taskvine.viewmodel.CategoryViewModel
import soltani.code.taskvine.viewmodel.TaskViewModel
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // Inject AchievementProvider directly
    @Inject
    lateinit var achievementProvider: AchievementProvider
    private val taskViewModel: TaskViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()

    private lateinit var launcher: ActivityResultLauncher<IntentSenderRequest>
    private var latestResultHandler: ((Intent?) -> Unit)? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)


        val onLogout = {
            // Add logout logic here
            // e.g., FirebaseAuth.getInstance().signOut()
            // Then restart or navigate to login
            finish()
        }
        launcher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                latestResultHandler?.invoke(result.data)
            }
        }



        val categoryViewModel = ViewModelProvider(this)[CategoryViewModel::class.java]

        // Load the saved theme preference
        val sharedPreferences = getSharedPreferences("reminderSettings", Context.MODE_PRIVATE)
        val savedTheme = sharedPreferences.getString("themePreference", "Green") ?: "Green"

        // Enable edge-to-edge content
        enableEdgeToEdge()

        // Ensure system bars adapt to content
        WindowCompat.setDecorFitsSystemWindows(window, false)





        setContent {
            // Observe the themeSelection as a state
            val themeSelection = remember { mutableStateOf(savedTheme) }

            TaskVineTheme(
                useDarkTheme = themeSelection.value == "Dark",
                useLightTheme = themeSelection.value == "Light"
            ) {
                // Update status bar icon color based on theme
                val isDarkModeEnabled = resources.configuration.uiMode and 0x30 == 0x20
                val isLightIcons = when {
                    themeSelection.value == "Dark" -> true // White icons for Dark theme
                    themeSelection.value == "Green" && isDarkModeEnabled -> true // White icons for Green theme in dark mode
                    themeSelection.value == "Light" && isDarkModeEnabled -> false // Dark icons for Light theme in dark mode
                    else -> themeSelection.value != "Light" // Dark icons for Green/Light themes in light mode
                }

                WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = !isLightIcons

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(
                        taskViewModel = taskViewModel,
                        categoryViewModel = categoryViewModel,
                        achievementProvider = achievementProvider,
                        onThemeChanged = { newTheme ->
                            themeSelection.value = newTheme
                            sharedPreferences.edit().putString("themePreference", newTheme).apply()

                            // Update status bar icon color on theme change
                            val updatedIsLightIcons = when {
                                newTheme == "Dark" -> true
                                newTheme == "Green" && isDarkModeEnabled -> true
                                newTheme == "Light" && isDarkModeEnabled -> false
                                else -> newTheme != "Light"
                            }
                            WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = !updatedIsLightIcons
                        },
                        authViewModel = authViewModel, // ✅ pass this
                        onLogout = onLogout,           // ✅ pass this
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
