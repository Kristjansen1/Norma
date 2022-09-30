package roskar.kristjan.norma

import android.app.DatePickerDialog
import android.content.Context
import android.icu.util.Calendar
import android.widget.DatePicker
import roskar.kristjan.norma.room.AppDatabase
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Date {
    fun currentDateWithFormat(format: String): String {
        val currentDate = LocalDateTime.now()
        val dateFormat = DateTimeFormatter.ofPattern(format)
        return currentDate.format(dateFormat)
    }

    fun formatDate(date: String, formatOld: String, formatNew: String): String {
        val formatFrom = DateTimeFormatter.ofPattern(formatOld)
        val dateToFormat = LocalDate.parse(date, formatFrom)
        val formatTo = DateTimeFormatter.ofPattern(formatNew)
        return dateToFormat.format(formatTo).toString()
    }

    fun monthAndYear(date: String): String {
        return formatDate(date, "d/M/yyyy", "LLLL yyyy")
    }
}

