package roskar.kristjan.norma.utilities

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object Util {

    //returns current date in format d/M/yyyy
    fun getCurrentMonth(): String {
        val cDate = LocalDate.now()
        val dateFormat = DateTimeFormatter.ofPattern("d/M/yyyy")
        return cDate.format(dateFormat)
    }
    fun monthYearNumber(date: String,fromFormat: String) : String {
        return formatDate(date,Constants.MONTH_YEAR_NUMBER,fromFormat)
    }
    fun monthYearWord(date: String,fromFormat: String) : String {
        return formatDate(date,Constants.MONTH_YEAR_WORD,fromFormat)

    }
    private fun formatDate(date: String, newDateFormat: String, oldDateFormat: String): String {

        val formatFrom = DateTimeFormatter.ofPattern(oldDateFormat)
        val dateToFormat = LocalDate.parse(date,formatFrom)
        val formatTo = DateTimeFormatter.ofPattern(newDateFormat)
        return dateToFormat.format(formatTo)

    }
}