package soltani.code.taskvine.ui.screens.profile

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import soltani.code.taskvine.helpers.LocalCustomColors

@Composable
fun TaskStatisticsPieChart(
    completed: Float,
    pending: Float,
    overdue: Float
) {
    // State to trigger a re-render after the chart is fully initialized
    val chartReady = remember { mutableStateOf(false) }
    val customColors = LocalCustomColors.current

    // Prepare data for the chart
    val entries = listOf(
        PieEntry(completed, "Completed"),
        PieEntry(pending, "Pending"),
        PieEntry(overdue, "Overdue")
    )

    val dataSet = PieDataSet(entries, "").apply {
        colors = listOf(
            Color(0xFF588157).toArgb(), // FernGreen for Completed
            Color(0xFF8A9A5B).toArgb(), // MossGreen for Pending
            Color(0xFF2C3E50).toArgb()  // DeepNavy for Overdue
        )
        valueTextColor = android.graphics.Color.WHITE // White text color
        valueTextSize = 17f // Percentage text size
        setDrawValues(true) // Show percentage values on slices
    }

    val data = PieData(dataSet).apply {
        setValueTextSize(17f) // Set consistent size for percentage text
        setValueTextColor(android.graphics.Color.WHITE) // White text color for visibility
    }

    // Render the chart
    AndroidView(
        factory = { context ->
            PieChart(context).apply {
                this.data = data
                description.isEnabled = false

                // Center text
                centerText = "Task Status"
                setCenterTextSize(18f)
                setCenterTextColor(android.graphics.Color.DKGRAY)

                // Use percentage values
                setUsePercentValues(true)

                // Hide entry labels (slice names)
                setDrawEntryLabels(false)

                // Customize legend
                legend.apply {
                    isEnabled = true
                    form = Legend.LegendForm.CIRCLE
                    horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                    verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
                    orientation = Legend.LegendOrientation.HORIZONTAL
                    textColor = customColors.primaryText.hashCode()
                    textSize = 12f
                }

                // Mark the chart as ready once configurations are set
                chartReady.value = true
                invalidate() // Force a redraw
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    )

    // Trigger a re-composition to stabilize the chart rendering
    if (chartReady.value) {
        LaunchedEffect(Unit) {
            chartReady.value = false // Reset state after stabilization
        }
    }
}
