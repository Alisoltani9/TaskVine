package soltani.code.taskvine.helpers

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.widget.Toast
import java.util.Calendar
import java.util.Date

// Function to show date picker
fun showDatePicker(
    context: Context,
    onDateSelected: (Date) -> Unit,
    onClearReminder: () -> Unit // Callback for clearing reminder
) {
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            // Save selected date
            calendar.set(year, month, dayOfMonth)
            onDateSelected(calendar.time) // Call the callback with the selected date
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    // Restrict to future dates only
    datePickerDialog.datePicker.minDate = calendar.timeInMillis

    // Add the "Clear Reminder" button
    datePickerDialog.setButton(
        DialogInterface.BUTTON_NEGATIVE,
        "Clear"
    ) { dialog, _ ->
        onClearReminder() // Invoke the callback to clear the reminder
        dialog.dismiss()  // Close the dialog
    }

    datePickerDialog.show() // Show the dialog
}


fun showTimePicker(
    context: Context,
    initialDate: Date,
    onTimeSelected: (Date) -> Unit
) {
    val currentCalendar = Calendar.getInstance()
    val selectedCalendar = Calendar.getInstance().apply { time = initialDate }

    val timePickerDialog = TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            // Update the selected time
            selectedCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            selectedCalendar.set(Calendar.MINUTE, minute)

            // Check if the selected time is in the past
            if (selectedCalendar.time.before(currentCalendar.time)) {
                // Show error feedback (e.g., a toast)
                Toast.makeText(context, "Cannot select a past time!", Toast.LENGTH_SHORT).show()

                // Optionally, re-open the time picker dialog
                showTimePicker(context, initialDate, onTimeSelected)
            } else {
                onTimeSelected(selectedCalendar.time)
            }
        },
        selectedCalendar.get(Calendar.HOUR_OF_DAY),
        selectedCalendar.get(Calendar.MINUTE),
        true
    )

    timePickerDialog.show()
}
