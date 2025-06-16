package soltani.code.taskvine.helpers

import java.util.Calendar

fun isReminderToday(reminderTime: Long): Boolean {
    val reminderCalendar = Calendar.getInstance().apply {
        timeInMillis = reminderTime
    }
    val todayCalendar = Calendar.getInstance()

    return (reminderCalendar.get(Calendar.YEAR) == todayCalendar.get(Calendar.YEAR)
            && reminderCalendar.get(Calendar.MONTH) == todayCalendar.get(Calendar.MONTH)
            && reminderCalendar.get(Calendar.DAY_OF_MONTH) == todayCalendar.get(Calendar.DAY_OF_MONTH))
}

fun isReminderInPast(reminderTime: Long): Boolean {
    val reminderCalendar = Calendar.getInstance().apply {
        timeInMillis = reminderTime
    }
    val todayCalendar = Calendar.getInstance()
    todayCalendar.set(Calendar.HOUR_OF_DAY, 0)
    todayCalendar.set(Calendar.MINUTE, 0)
    todayCalendar.set(Calendar.SECOND, 0)
    todayCalendar.set(Calendar.MILLISECOND, 0)

    return reminderCalendar.before(todayCalendar)
}

fun isReminderUpcoming(reminderTime: Long): Boolean {
    val reminderCalendar = Calendar.getInstance().apply {
        timeInMillis = reminderTime
    }
    val tomorrowCalendar = Calendar.getInstance().apply {
        // Move to the next day and set the time to the beginning of that day
        add(Calendar.DAY_OF_MONTH, 1)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }


    return reminderCalendar.after(tomorrowCalendar)
}