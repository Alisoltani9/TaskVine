package soltani.code.taskvine.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import soltani.code.taskvine.helpers.LocalCustomColors
import soltani.code.taskvine.ui.screens.profile.AchievementSection
import soltani.code.taskvine.ui.screens.profile.PreferenceSection
import soltani.code.taskvine.ui.screens.profile.ProfileSection
import soltani.code.taskvine.ui.screens.profile.SupportSettings
import soltani.code.taskvine.ui.screens.profile.TaskStatistics
import soltani.code.taskvine.viewmodel.AchievementProvider
import soltani.code.taskvine.viewmodel.AuthViewModel
import soltani.code.taskvine.viewmodel.TaskViewModel


@Composable
fun ProfileScreen(
    taskViewModel: TaskViewModel,
    authViewModel: AuthViewModel,
    achievementProvider: AchievementProvider,
    navController: NavController,
    onThemeChanged: (String) -> Unit,
    onLogout: () -> Unit

) {
    val snackbarHostState = remember { SnackbarHostState() } // Snackbar state
    val completedTasks by taskViewModel.completedTaskCount.observeAsState(0)
    val pendingTasks by taskViewModel.pendingTaskCount.observeAsState(0)
    val overdueTasks by taskViewModel.overdueTaskCount.observeAsState(0)
    val colors = LocalCustomColors.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.primaryBackground)
    ) {
        // Main Content
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ProfileSection(
                    authViewModel = authViewModel,
                    navigateToWelcome = {
                    navController.navigate("welcome") {
                        popUpTo("profile") { inclusive = true }
                    }
                })
            }

            item {
                TaskStatistics(completed = completedTasks, pending = pendingTasks, overdue = overdueTasks)
            }

            item {
                PreferenceSection(
                    snackbarHostState = snackbarHostState, // Pass SnackbarHostState to PreferenceSection
                    onThemeChanged = onThemeChanged
                )
            }

            item {
                AchievementSection(achievementProvider, taskViewModel)
            }

            item {
                SupportSettings(navController)
            }
        }

        // Overlay SnackbarHost
        SnackbarHost(
            hostState = snackbarHostState,
            snackbar = { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = colors.primaryBackground,
                    contentColor = colors.primaryText
                )
            },
            modifier = Modifier
                .align(Alignment.BottomCenter) // Align to bottom
                .padding(16.dp) // Add padding to avoid overlapping
        )
    }
}

