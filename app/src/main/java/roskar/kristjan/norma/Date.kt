package roskar.kristjan.norma

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Date {
    fun currentDateWithFormat(format: String): String {
        val currentDate = LocalDateTime.now()
        val dateFormat = DateTimeFormatter.ofPattern(format)
        return currentDate.format(dateFormat)
    }
    fun formatDate(date: String,formatOld: String,formatNew: String): String {
        val formatFrom = DateTimeFormatter.ofPattern(formatOld)
        val dateToFormat = LocalDate.parse(date,formatFrom)
        val formatTo = DateTimeFormatter.ofPattern(formatNew)
        return dateToFormat.format(formatTo).toString()
    }
}