package roskar.kristjan.norma.utilities

import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentManager
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
    fun formatDate(date: String, newDateFormat: String, oldDateFormat: String): String {

        val formatFrom = DateTimeFormatter.ofPattern(oldDateFormat)
        val dateToFormat = LocalDate.parse(date,formatFrom)
        val formatTo = DateTimeFormatter.ofPattern(newDateFormat)
        return dateToFormat.format(formatTo)

    }

    fun log(toString: String) {
        Log.d("app log",toString)
    }
    fun toast(context: Context,text: String) {
        Toast.makeText(context,text, Toast.LENGTH_SHORT).show()
    }

}