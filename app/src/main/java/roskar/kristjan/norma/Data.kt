package roskar.kristjan.norma

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import roskar.kristjan.norma.room.AppDatabase
import roskar.kristjan.norma.room.Norma
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Data {

     @SuppressLint("NotifyDataSetChanged")
     fun addData(
         appDb: AppDatabase,
         date: Int,
         normaHours: Int,
         workingHours: Int,
         workplace: String,
         itemListArray: ArrayList<ItemList>,
         newRecyclerView: RecyclerView
     ) {

        val norma = Norma(
            null,
            date,
            normaHours,
            workingHours,
            workplace
        )

        GlobalScope.launch(Dispatchers.IO) {
            appDb.normaDao().insert(norma)
        }
        itemListArray.add(
            ItemList(
                date,
                normaHours,
                workingHours,
                workplace
            )
        )
        newRecyclerView.adapter!!.notifyDataSetChanged()
    }

    fun displayData (context: Context, recyclerView: RecyclerView, itemListArray: ArrayList<ItemList>) {

        val adapter = Adapter(itemListArray)
        recyclerView.adapter = adapter

        adapter.setOnItemClickListener(object : Adapter.onItemClickListener {
            override fun onItemClick(
                position: Int,
                listItemAdd: ImageView,
                listItemRemove: ImageView
            ) {
                listItemAdd.visibility = View.VISIBLE
                listItemRemove.visibility = View.VISIBLE
                Toast.makeText(
                    context,
                    "You Clicked on item no. $position",
                    Toast.LENGTH_SHORT
                ).show()

            }

           /* override fun onAddButtonClicked(position: Int, listItemAdd: ImageView) {
            }*/

        })
    }

    fun populate(appDb: AppDatabase, withMonth: String) : ArrayList<ItemList> {
        val itemListArray: ArrayList<ItemList> = arrayListOf()

        GlobalScope.launch(Dispatchers.IO) {

            //Prikaze vnose za tekoci mesec...

            val match = "%$withMonth%"
            val dbData: List<Norma> = appDb.normaDao().findByMonth(match)
            var itemList: ItemList
            dbData.forEach {

                itemList = ItemList(
                    date = it.norma_date!!,
                    normaHours = it.normaHours!!,
                    workingHours = it.workingHours!!,
                    workplace = it.workplace!!
                )

                Log.d("nevemkej",it.norma_date.toString())
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
            val n = Norma(null,formated.toInt(),r,8,"linija")
            appDb.normaDao().insert(n)
            }
        }
    }
}