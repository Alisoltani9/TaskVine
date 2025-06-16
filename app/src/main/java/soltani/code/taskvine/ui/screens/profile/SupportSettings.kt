package soltani.code.taskvine.ui.screens.profile

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import soltani.code.taskvine.R
import soltani.code.taskvine.helpers.LocalCustomColors

@Composable
fun SupportSettings(
    navController: NavController
) {
    val colors = LocalCustomColors.current
    val context = LocalContext.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = colors.SecondaryBackground)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "TaskVine",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = colors.primaryText
            )

            SupportSettingItem(
                title = "Recommend to Friends",
                iconRes = R.drawable.baseline_share_24,
                onClick = {
                    val shareMessage =
                        "Check out this awesome TaskReminder app! Download it now: https://play.google.com/store/apps/details?id=soltani.code.taskvine"

                    val shareIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, shareMessage)
                        type = "text/plain"
                    }

                    context.startActivity(Intent.createChooser(shareIntent, "Share via"))
                }
            )


            SupportSettingItem(
                title = "Help & Feedback",
                iconRes = R.drawable.baseline_forum_24,
                onClick = {
                    val emailIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "message/rfc822" //
                        putExtra(Intent.EXTRA_EMAIL, arrayOf("soltanicode@gmail.com"))
                        putExtra(Intent.EXTRA_SUBJECT, "Help & Feedback for TaskReminder")
                    }

                    // Try to launch Gmail directly first
                    val gmailPackage = "com.google.android.gm"
                    val intent = Intent(emailIntent).apply {
                        setPackage(gmailPackage) // Target Gmail directly
                    }

                    try {
                        // Try to launch Gmail directly
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        // If Gmail is not installed or available, fall back to generic email app chooser
                        try {
                            context.startActivity(
                                Intent.createChooser(
                                    emailIntent,
                                    "Choose an Email app"
                                )
                            )
                        } catch (ex: Exception) {
                            Toast.makeText(context, "No email app found", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            )

            SupportSettingItem(
                title = "About Us",
                iconRes = R.drawable.baseline_group_24,
                onClick = {
                    navController.navigate("about")
                }
            )
        }
    }
}

@Composable
fun SupportSettingItem(
    title: String,
    iconRes: Int,
    onClick: () -> Unit
) {
    val colors = LocalCustomColors.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = title,
            tint = colors.primaryText,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = title,
            color = colors.primaryText,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
