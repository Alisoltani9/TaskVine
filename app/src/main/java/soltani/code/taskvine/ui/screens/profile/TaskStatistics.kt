package soltani.code.taskvine.ui.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import soltani.code.taskvine.helpers.LocalCustomColors

@Composable
fun TaskStatistics(completed: Int, pending: Int, overdue: Int) {
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
                text = "Task Statistics",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = colors.primaryText
            )

            // Check if all task counts are zero
            if (completed == 0 && pending == 0 && overdue == 0) {
                // No data available, show a message instead of a progress indicator
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No Tasks Yet",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.primaryText
                    )
                }
            } else {
                // Show task count and render the pie chart
                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        text = "Completed: $completed ",
                        color = colors.primaryText
                    )
                    Text(
                        text = "Pending: $pending ",
                        color = colors.primaryText
                    )
                    Text(
                        text = "Overdue: $overdue ",
                        color = colors.primaryText
                    )
                }

                // Only render the pie chart if there are tasks to show
                TaskStatisticsPieChart(
                    completed = completed.toFloat(),
                    pending = pending.toFloat(),
                    overdue = overdue.toFloat()
                )
            }
        }
    }
}
