package roskar.kristjan.norma

import android.app.DatePickerDialog
import android.content.Context
import android.icu.util.Calendar
import android.util.Log
import android.widget.DatePicker
import roskar.kristjan.norma.room.AppDatabase
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.properties.Delegates

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

    fun pickDate(context: Context, appDb: AppDatabase) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(
            context,
            DatePickerDialog.OnDateSetListener { _: DatePicker, mYear: Int, mMonth: Int, mDay: Int ->
                Data.addMonth(mYear, mMonth, mDay, appDb)
            },
            year,
            month,
            day
        ).show()
    }
}

