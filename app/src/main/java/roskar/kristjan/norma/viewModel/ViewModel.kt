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
import java.math.RoundingMode
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.YearMonth
import kotlin.math.abs
import kotlin.math.sign

class ViewModel(application: Application) : AndroidViewModel(application) {

    private val appDatabase = AppDatabase.getDatabase(application)

    //active month format: d/M/yyyy
    private val _activeMonth = MutableLiveData<String>()
    val activeMonth: LiveData<String> = _activeMonth

    // store the clicked item on recyclerview edit icon click
    private val _clickedItem = MutableLiveData<Productivity>()
    val clickedItem: LiveData<Productivity> = _clickedItem

    private var _activeMonthProgress = MutableLiveData<Double>()
    var activeMonthProgress: LiveData<Double> = _activeMonthProgress

    private var _activeMonthWorkedHours = MutableLiveData<Double>()
    var activeMonthWorkedHours: LiveData<Double> = _activeMonthWorkedHours

    private var _firstStart = MutableLiveData<Boolean>()
    var firstStart: LiveData<Boolean> = _firstStart

  /*  private val _activeMonthData = MutableLiveData<Month>()
    val activeMonthData: LiveData<Month> = _activeMonthData*/


    lateinit var activeMonthDates : LiveData<List<Productivity>>
    lateinit var activeMonthData : LiveData<Month>

    init {
        setFirstStart(true)
        setActiveMonth(Util.getCurrentMonth())
        Util.log("VIEWMODEL INIT")
        getActiveMonthData(
            Util.monthYearNumber(
                activeMonth.value.toString(),
                Constants.DAY_MONTH_YEAR_NUMBER
            )
        )
    }

    fun setFirstStart(value: Boolean) {
        _firstStart.value = value
    }
    fun setActiveMonthProgress(value: Double) {
        _activeMonthProgress.value = value
    }

    fun setActiveMonthWorkedHours(value: Double) {
        _activeMonthWorkedHours.value = value
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

    //yes i know, almost identical code repeating itself,.... :) its OK!

    fun addToMonthlyProgress(date: String, diff: Double) {
        val progressOld = _activeMonthProgress.value
        val progressNew = roundNumber(progressOld!! + diff)
        val df = Util.formatDate(date,Constants.MONTH_YEAR_NUMBER,Constants.DAY_MONTH_YEAR_NUMBER)
        val match = "%$df%"
        setActiveMonthProgress(progressNew)
        viewModelScope.launch(Dispatchers.IO) {
            appDatabase.monthDao().updateMonthlyProgress(match,progressNew)
        }

    }

    fun removeFromMonthlyProgress(date: String, diff: Double) {
        val progressOld = _activeMonthProgress.value
        val progressNew = roundNumber(progressOld!! - diff)
        val df = Util.formatDate(date,Constants.MONTH_YEAR_NUMBER,Constants.DAY_MONTH_YEAR_NUMBER)
        val match = "%$df%"
        setActiveMonthProgress(progressNew)
        viewModelScope.launch(Dispatchers.IO) {
            appDatabase.monthDao().updateMonthlyProgress(match,progressNew)
        }
    }

    private fun roundNumber(progressNew: Double): Double {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.DOWN
        val roundoff = df.format(progressNew)
        return roundoff.toDoubleOrNull()!!
    }

    fun addToWorkingHours(date: String, diff: Double) {
        val progressOld = _activeMonthWorkedHours.value
        val progressNew = roundNumber(progressOld!! + diff)
        val df = Util.formatDate(date,Constants.MONTH_YEAR_NUMBER,Constants.DAY_MONTH_YEAR_NUMBER)
        val match = "%$df%"
        setActiveMonthWorkedHours(progressNew)
        viewModelScope.launch(Dispatchers.IO) {
            appDatabase.monthDao().updateWorkedHours(match,progressNew)
        }
    }

    fun removeFromWorkingHours(date: String, diff: Double) {
        val progressOld = _activeMonthWorkedHours.value
        val progressNew = roundNumber(progressOld!! - diff)
        val df = Util.formatDate(date,Constants.MONTH_YEAR_NUMBER,Constants.DAY_MONTH_YEAR_NUMBER)
        val match = "%$df%"
        setActiveMonthWorkedHours(progressNew)
        viewModelScope.launch(Dispatchers.IO) {
            appDatabase.monthDao().updateWorkedHours(match,progressNew)
        }
    }

}


