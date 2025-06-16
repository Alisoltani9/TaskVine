package soltani.code.taskvine.ui.screens.profile

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import soltani.code.taskvine.helpers.LocalCustomColors
import soltani.code.taskvine.model.Achievement
import soltani.code.taskvine.viewmodel.AchievementProvider
import soltani.code.taskvine.viewmodel.TaskViewModel

@Composable
fun AchievementSection(achievementProvider: AchievementProvider, taskViewModel: TaskViewModel) {
    val achievements by achievementProvider.achievementList.observeAsState(emptyList()) // Observe LiveData
    val colors = LocalCustomColors.current



    LaunchedEffect(Unit) {
        // Check and update all achievements when this section is loaded
        achievementProvider.checkAndUpdateAllAchievements()
    }



    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = colors.SecondaryBackground)
    )
    {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Add a header for achievements
            Text(
                text = "Achievements",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = colors.primaryText
            )

            // Show a message if there are no achievements
            if (achievements.isEmpty()) {
                Text(
                    text = "No achievements yet.",
                    fontSize = 14.sp,
                    color = colors.primaryText
                )
            } else {
                // Display achievements
                achievements.forEach { achievement ->
                    AchievementItem(achievement)
                }
            }
        }
    }

}

@Composable
fun AchievementItem(achievement: Achievement) {
    val colors = LocalCustomColors.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Checkbox(
            checked = achievement.isAchieved,
            onCheckedChange = { },
            enabled = false,
            colors = CheckboxDefaults.colors(
                checkedColor = colors.primaryText,
                uncheckedColor = colors.secondaryText,
                checkmarkColor = colors.primaryBackground,
                disabledCheckedColor = colors.primaryText,
                disabledUncheckedColor = colors.primaryText,
                disabledIndeterminateColor = colors.primaryText
            )
        )
        Text(
            text = achievement.title,
            color = colors.primaryText,
            fontWeight = if (achievement.isAchieved) FontWeight.Bold else FontWeight.Normal
        )
    }
}

