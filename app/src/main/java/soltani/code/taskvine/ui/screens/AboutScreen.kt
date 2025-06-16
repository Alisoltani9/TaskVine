package soltani.code.taskvine.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import soltani.code.taskvine.helpers.LocalCustomColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(navController: NavController) {
    val tabs = listOf("About Us", "Privacy Policy")
    var selectedTabIndex by remember { mutableStateOf(0) }
    val colors = LocalCustomColors.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "About", color = colors.thirdText) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, tint = colors.primaryText, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colors.primaryBackground
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.primaryBackground)
                .padding(padding)
        ) {
            Column {
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = colors.primaryBackground,
                    contentColor = colors.thirdText,
                    indicator = { /* No indicator here */ },
                    divider = {}
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = {
                                Text(
                                    text = title,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontSize = if (index == selectedTabIndex) 17.sp else 14.sp,
                                        fontWeight = if (index == selectedTabIndex) FontWeight.Bold else FontWeight.Normal
                                    ),
                                    color = colors.thirdText
                                )
                            }
                        )
                    }
                }

                when (selectedTabIndex) {
                    0 -> AboutUsContent()
                    1 -> PrivacyPolicyContent()
                }
            }
        }
    }
}

@Composable
fun AboutUsContent() {
    val colors = LocalCustomColors.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "About Us",
            style = MaterialTheme.typography.headlineMedium,
            color = colors.thirdText
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = aboutUsText,
            style = MaterialTheme.typography.bodyMedium,
            color = colors.thirdText
        )
    }
}

@Composable
fun PrivacyPolicyContent() {
    val colors = LocalCustomColors.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Privacy Policy",
            style = MaterialTheme.typography.headlineMedium,
            color = colors.thirdText
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = privacyPolicyText,
            style = MaterialTheme.typography.bodyMedium,
            color = colors.thirdText
        )
    }
}


private val aboutUsText = """
TaskReminder is your go-to app for staying organized and on top of your tasks. Designed with simplicity and efficiency in mind, TaskReminder offers powerful features to manage your day-to-day responsibilities. Whether you're setting up tasks, prioritizing them, or tracking your progress, TaskReminder helps you get things done seamlessly.

Key Features:
• Easy task creation and management
• Due date reminders and notifications
• Progress tracking with completion stats
• Support for light and dark themes
• Achievements to keep you motivated

Our mission is to empower users with a tool that makes productivity easy and rewarding.

For questions or feedback, contact us at: soltanicode@gmail.com
""".trimIndent()

private val privacyPolicyText = """
We take your privacy seriously. This Privacy Policy outlines how we collect, use, and safeguard your data when you use TaskReminder.

Information We Collect:
• Personal details like email (if provided during feedback)
• App usage statistics (to improve the app)
• Device information (for troubleshooting)

How We Use Your Data:
• To enhance app functionality
• To respond to your feedback and inquiries
• To send notifications with your consent

Your data is never shared with third parties without your consent.

Contact us for questions about privacy at: soltanicode@gmail.com
""".trimIndent()
