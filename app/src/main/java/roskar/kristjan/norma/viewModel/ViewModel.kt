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
import java.math.BigDecimal
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class ViewModel(application: Application) : AndroidViewModel(application) {

    private val appDatabase = AppDatabase.getDatabase(application)

    //active month format: d/M/yyyy
    private val _activeMonth = MutableLiveData<String>()
    val activeMonth: LiveData<String> = _activeMonth

    // store the clicked item on recyclerview edit icon click
    private val _clickedItem = MutableLiveData<Productivity>()
    val clickedItem: LiveData<Productivity> = _clickedItem

  /*  private val _activeMonthData = MutableLiveData<Month>()
    val activeMonthData: LiveData<Month> = _activeMonthData*/


    lateinit var activeMonthDates : LiveData<List<Productivity>>
    lateinit var activeMonthData : LiveData<Month>

    init {
        setActiveMonth(Util.getCurrentMonth())
        getActiveMonthData(
            Util.monthYearNumber(
                activeMonth.value.toString(),
                Constants.DAY_MONTH_YEAR_NUMBER
            )
        )
    }
    // set active month as livedata
    // on app start, and on user selection of new month
    private fun setActiveMonth(value: String) {
        _activeMonth.value = value
    }

/*    fun setActiveMonthData(month: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val match = "%$month%"

        }
    }*/

    // used to pass data to addUpdateEntry fragment
    // when user clicks the edit icon in recyclerview
    fun setClickedItem(item: Productivity) {
        _clickedItem.value = item
    }

    //query productivity_table for all dates in current activeMonth
    private fun getActiveMonthData(month: String){
        val match = "%$month%"
            activeMonthData = appDatabase.monthDao().getMonthData(match)
            activeMonthDates = appDatabase.productivityDao().findByMonth(match)
    }

    // add new month to DB
    // * add all dates of given month to productivity_table
    // * add entry to month_table

    fun addMonth(year: Int, month: Int, day: Int) {

        //list of all the dates in given month
        val allDatesList = ArrayList<Productivity>()
        var dayString: String
        var workDays = 0

        val fixedMonth = month +1

        val date = "$day/$fixedMonth/$year"
        val monthLength = YearMonth.of(year,fixedMonth).lengthOfMonth()

        for (day in 1..monthLength) {
            val productivity = Productivity(
                null,
                "$day/$fixedMonth/$year",
                0.0,
                0.0
            )
            dayString = LocalDate.of(year,fixedMonth,day).dayOfWeek.toString()
            if (dayString != "SATURDAY" && dayString != "SUNDAY") {
                Util.log("$dayString + increase")
                workDays++
            }
            allDatesList.add(productivity)
        }
        val monthToAdd = Month(null,date,workDays,0.0,0.0)

        viewModelScope.launch(Dispatchers.IO) {
            appDatabase.monthDao().insert(monthToAdd)
            appDatabase.productivityDao().insert(allDatesList)
        }
    }

    // update or add an entry to productivity_table
    fun updateEntryProductivityTable(item: Productivity) {
        viewModelScope.launch(Dispatchers.IO) {
           val x = appDatabase.productivityDao().update(
                item.date,
                item.productivityHours,
                item.workingHours
            )
            Util.log(x.toString())
        }
    }

    fun refreshMonthDb(case: String,date :String,value: BigDecimal) {

        when (case) {
            "addition_productivity" -> {
                val x = activeMonthData.value?.monthlyProgress?.toBigDecimal()
                val r = x?.plus(value)
                if (r != null) {
                    updateMonthlyProgress(date,r.toDouble())
                }
            }
            "subtract_productivity" -> {
                val x = activeMonthData.value?.monthlyProgress?.toBigDecimal()
                val r = x?.minus(value)
                if (r != null) {
                    updateMonthlyProgress(date,r.toDouble())
                }
            }
            "addition_workHours" -> {
                val x = activeMonthData.value?.workedHours?.toBigDecimal()
                val r = x?.plus(value)
                if (r != null) {
                    updateWorkedHours(date,r.toDouble())
                }
            }
            "subtract_workHours" -> {
                val x = activeMonthData.value?.workedHours?.toBigDecimal()
                val r = x?.minus(value)
                if (r != null) {
                    updateWorkedHours(date,r.toDouble())
                }
            }
        }

    }

    private fun updateMonthlyProgress(date: String,value: Double) {
        val df = Util.formatDate(date,"M/yyyy","d/M/yyyy")
        val match = "%$df%"
        viewModelScope.launch(Dispatchers.IO) {
            appDatabase.monthDao().updateMonthlyProgress(match,value)
        }
    }
    private fun updateWorkedHours(date: String,value: Double) {
        val df = Util.formatDate(date,"M/yyyy","d/M/yyyy")
        val match = "%$df%"
        viewModelScope.launch(Dispatchers.IO) {
            appDatabase.monthDao().updateWorkedHours(match,value)
        }
    }

}


