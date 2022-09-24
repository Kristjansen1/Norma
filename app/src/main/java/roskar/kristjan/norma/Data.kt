package roskar.kristjan.norma

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import roskar.kristjan.norma.room.AppDatabase
import roskar.kristjan.norma.room.Month
import roskar.kristjan.norma.room.Norma
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter

object Data {
    @OptIn(DelicateCoroutinesApi::class)
    @SuppressLint("NotifyDataSetChanged")
    fun addData(
        appDb: AppDatabase,
        date: Int,
        normaHours: Int,
        workingHours: Int,
        workplace: String,
        itemListArray: ArrayList<ItemList>,
        newRecyclerView:
        RecyclerView
    ) {


        val norma = Norma(null, date, normaHours, workingHours, workplace)
        GlobalScope.launch(Dispatchers.IO) {
            appDb.normaDao().insert_norma(norma)
        }


        itemListArray.add(ItemList(date, normaHours, workingHours, workplace))
        newRecyclerView.adapter!!.notifyDataSetChanged()
    }

    fun displayData(
        context: Context,
        recyclerView: RecyclerView,
        itemListArray: ArrayList<ItemList>
    ) {

    }

    @OptIn(DelicateCoroutinesApi::class)
    fun populate(appDb: AppDatabase, withMonth: String): ArrayList<ItemList> {
        val itemListArray: ArrayList<ItemList> = arrayListOf()

        GlobalScope.launch(Dispatchers.IO) {

            //Prikaze vnose za tekoci mesec...

            val match = "%$withMonth%"
            val dbData: List<Norma> = appDb.normaDao().findByMonth(match)
            Log.d("findbymonth", match)
            var itemList: ItemList
            dbData.forEach {

                itemList = ItemList(
                    date = it.norma_date!!,
                    normaHours = it.normaHours!!,
                    workingHours = it.workingHours!!,
                    workplace = it.workplace!!
                )

                Log.d("nevemkej", it.norma_date.toString())
                itemListArray.add((itemList))
            }
        }
        return itemListArray
    }

    fun deleteData() {

    }

    fun fillDB(appDb: AppDatabase) {
        var cDate = LocalDateTime.now()
        val formater = DateTimeFormatter.ofPattern("ddMMyy")
        var formated: String

        GlobalScope.launch {
            for (x in 1..1256) {
                val r = (1..8).shuffled().last()
                val nDate = cDate.plusDays(x.toLong())
                formated = nDate.format(formater)
                val n = Norma(null, formated.toInt(), r, 8, "linija")
                appDb.normaDao().insert_norma(n)
            }
        }
    }

    fun addMonth(year: Int, month: Int, day: Int, appDb: AppDatabase) {

        var fixMonth = month
        fixMonth++
        val date = "$day/$fixMonth/$year"
        val dateFormated = Date.formatDate(date, "d/M/yyyy", "MMyy")
        val monthToAdd = Month(null, dateFormated.toInt())
        var norma: Norma
        val yearMonth = YearMonth.of(year, fixMonth)
        var monthLength = yearMonth.lengthOfMonth()

        GlobalScope.launch(Dispatchers.IO) {
            appDb.normaDao().insert_month(monthToAdd)
            for (day1 in 1..monthLength) {
                var d = "$day1/$fixMonth/$year"
                val df = Date.formatDate(d, "d/M/yyyy", "dMMyy").toInt()

                norma = Norma(null, df, 0, 0, "linija")
                appDb.normaDao().insert_norma(norma)
            }
        }
    }

    fun returnPick(year: Int, month: Int, day: Int): String {
        var fixMonth = month
        fixMonth++
        val date = "$day/$fixMonth/$year"
        return date
    }
}


