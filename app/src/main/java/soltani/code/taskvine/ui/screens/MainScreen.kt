package soltani.code.taskvine.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import soltani.code.taskvine.helpers.LocalCustomColors
import soltani.code.taskvine.helpers.PreferenceHelper
import soltani.code.taskvine.ui.component.BottomNavigation
import soltani.code.taskvine.ui.component.BottomNavigationItem
import soltani.code.taskvine.viewmodel.AchievementProvider
import soltani.code.taskvine.viewmodel.AuthViewModel
import soltani.code.taskvine.viewmodel.CategoryViewModel
import soltani.code.taskvine.viewmodel.TaskViewModel

@Composable
fun MainScreen(
    taskViewModel: TaskViewModel,
    categoryViewModel: CategoryViewModel,
    achievementProvider: AchievementProvider,
    authViewModel: AuthViewModel, // ✅ Add this parameter
    onLogout: () -> Unit,         // ✅ Add this parameter
    onThemeChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val navController = rememberNavController()
    val preferenceHelper = PreferenceHelper(context) // Initialize PreferenceHelper
    val colors = LocalCustomColors.current

    // Observe the current back stack entry to determine the current route
    val currentBackStackEntry by navController.currentBackStackEntryFlow.collectAsState(initial = null)
    val currentRoute = currentBackStackEntry?.destination?.route ?: "home"

    val bottomNavItems = listOf(
        BottomNavigationItem("Home", Icons.Default.Home, "home"),
        BottomNavigationItem("Search", Icons.Default.Search, "search"),
        BottomNavigationItem("Profile", Icons.Default.Person, "profile")
    )

    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarMessage by taskViewModel.snackbarMessage.observeAsState()

    // Check if the WelcomeScreen has been shown
    val startDestination = if (!preferenceHelper.isWelcomeScreenShown()) "welcome" else "home"

    // Snackbar handling
    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let { message ->
            val snackbarResult = snackbarHostState.showSnackbar(
                message,
                actionLabel = "Undo",
                duration = SnackbarDuration.Short
            )
            if (snackbarResult == SnackbarResult.ActionPerformed) {
                taskViewModel.undoCompleteTask()
            }
            taskViewModel.clearSnackbarMessage()
        }
    }

    // Scaffold with BottomNavigation
    Scaffold(
        bottomBar = {
            // Show the BottomNavigation only if the current route is not "welcome"
            if (currentRoute != "welcome") {
                BottomNavigation(
                    items = bottomNavItems,
                    currentRoute = currentRoute,
                    onItemSelected = { item ->
                        navController.navigate(item.route) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = colors.SecondaryBackground,
                    contentColor = colors.primaryText,
                    actionColor = colors.primaryText
                )
            }
        },
        containerColor = colors.primaryBackground
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = startDestination, // Start based on the flag
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("home") {
                HomeScreen(taskViewModel, categoryViewModel, achievementProvider)
            }
            composable("search") {
                SearchScreen(taskViewModel, categoryViewModel)
            }
            composable("about") {
                AboutScreen(navController = navController)
            }
            composable("welcome") {
                WelcomeScreen(navController = navController, preferenceHelper = preferenceHelper)
            }
            composable("profile") {
                ProfileScreen(
                    taskViewModel = taskViewModel,
                    onThemeChanged = onThemeChanged,
                    achievementProvider = achievementProvider,
                    navController = navController,
                    authViewModel = authViewModel,
                    onLogout = onLogout                    // ✅ FIXED


                )
            }
        }
    }
}
