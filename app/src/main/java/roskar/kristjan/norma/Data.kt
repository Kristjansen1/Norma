package roskar.kristjan.norma

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Data {

     @SuppressLint("NotifyDataSetChanged")
     fun addData(appDb: AppDatabase, date: Int, hours: Int, itemListArray: ArrayList<ItemList>, newRecyclerView: RecyclerView) {
        val norma = Norma(null, date, hours)
        GlobalScope.launch(Dispatchers.IO) {
            appDb.normaDao().insert(norma)
        }
        itemListArray.add(ItemList(date, hours))
        newRecyclerView.adapter!!.notifyDataSetChanged()
    }

    fun displayData (context: Context, recyclerView: RecyclerView, itemListArray: ArrayList<ItemList>) {

        val adapter = Adapter(itemListArray)
        recyclerView.adapter = adapter

        adapter.setOnItemClickListener(object : Adapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                Toast.makeText(
                    context,
                    "You Clicked on item no. $position",
                    Toast.LENGTH_SHORT
                ).show()

            }

        })
    }

    fun populate(appDb: AppDatabase,withMonth: String) : ArrayList<ItemList> {
        val itemListArray: ArrayList<ItemList> = arrayListOf()

        GlobalScope.launch(Dispatchers.IO) {

            //Prikaze vnose za tekoci mesec...

            //val month = currentDate.format(dateFormat)
            val month = Date.currentDateWithFormat(withMonth)
            val match = "%$month%"
            val dbData: List<Norma> = appDb.normaDao().findByMonth(match)
            var itemList: ItemList
            dbData.forEach {
                val date = it.date
                val hours = it.hours
                itemList = ItemList(date!!, hours!!)
                Log.d("nevemkej",it.date.toString())
                itemListArray.add((itemList))
            }
        }
        return itemListArray
    }
    fun deleteData() {

    }
}