package roskar.kristjan.norma.viewModel

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import roskar.kristjan.norma.database.AppDatabase
import roskar.kristjan.norma.model.Month
import roskar.kristjan.norma.model.Productivity
import roskar.kristjan.norma.utilities.Constants
import roskar.kristjan.norma.utilities.Util
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class ViewModel(application: Application) : AndroidViewModel(application) {

    private val appDatabase = AppDatabase.getDatabase(application)

    //active month format: d/M/yyyy
    private val _activeMonth = MutableLiveData<String>()
    val activeMonth: LiveData<String> = _activeMonth

    lateinit var activeMonthDates : LiveData<Productivity>
    init {

        setActiveMonth(Util.getCurrentMonth())
        getListOfDates(
            Util.monthYearNumber(
                activeMonth.value.toString(),
                Constants.DAY_MONTH_YEAR_NUMBER
            )
        )
    }
    fun setActiveMonth(value: String) {
        _activeMonth.value = value
    }

    //query DB for all dates in specified month
    private fun getListOfDates(month: String){
        viewModelScope.launch(Dispatchers.IO) {
            val match = "%$month%"
            activeMonthDates = appDatabase.productivityDao().findByMonth(match)
        }
    }

    fun addMonth(year: Int, month: Int, day: Int) {

        //list of all the dates in given month
        val allDatesList = ArrayList<Productivity>()

        val fixedMonth = month +1

        val date = "$day/$fixedMonth/$year"
        val monthToAdd = Month(null,date)
        val monthLength = YearMonth.of(year,fixedMonth).lengthOfMonth()

        for (day in 1..monthLength) {

            val productivity = Productivity(
                null,
                "$day/$fixedMonth/$year",
                0.0,
                0.0
            )
            allDatesList.add(productivity)
        }

        viewModelScope.launch(Dispatchers.IO) {
            appDatabase.monthDao().insert(monthToAdd)
            appDatabase.productivityDao().insert(allDatesList)
        }



    }

}

