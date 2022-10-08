package roskar.kristjan.norma

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.*
import roskar.kristjan.norma.adapter.MainActivityRvAdapter
import roskar.kristjan.norma.adapter.RemoveMonthRvAdapter
import roskar.kristjan.norma.databinding.ActivityMainBinding
import roskar.kristjan.norma.databinding.RemoveMonthBinding
import roskar.kristjan.norma.dialogFragments.NormaListAddDialog
import roskar.kristjan.norma.model.MonthList
import roskar.kristjan.norma.model.NormaList
import roskar.kristjan.norma.room.AppDatabase
import roskar.kristjan.norma.room.Month
import roskar.kristjan.norma.room.Norma
import java.time.YearMonth

/**
 * lol777
 */
@OptIn(DelicateCoroutinesApi::class)
class MainActivity : AppCompatActivity(), NormaListAddDialog.NormaListAddInterface {

    private lateinit var recyclerView: RecyclerView
    private var normaListArray: ArrayList<NormaList> = arrayListOf()
    private var monthListArray: ArrayList<MonthList> = arrayListOf()
    private lateinit var appDb: AppDatabase
    private lateinit var binding: ActivityMainBinding
    var clickedPosition = 82
    private var activeMonth = ""

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val addMonth = binding.addMonth
        val removeMonth = binding.removeMonth

        appDb = AppDatabase.getDatabase(this)
        recyclerView = binding.recylerView


        recyclerView.let {
            it.layoutManager = LinearLayoutManager(this)
            it.setHasFixedSize(true)
            it.addItemDecoration(
                DividerItemDecoration(
                    recyclerView.context,
                    DividerItemDecoration.VERTICAL
                )
            )
        }

        activeMonth = Date.currentDateWithFormat("M/yyyy")
        normaListArray = populateNormaList(activeMonth)
        monthListArray = populateMonthList()


        val adapter = MainActivityRvAdapter(normaListArray)
        recyclerView.adapter = adapter


        /**
         * RecyclerView onClick listener
         *
         */

        adapter.setOnItemClickListener(object : MainActivityRvAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                if (clickedPosition != position) {
                    val holderNewPosition =
                        recyclerView.findViewHolderForAdapterPosition(clickedPosition)
                    holderNewPosition?.let {
                        val listItemAdd =
                            (holderNewPosition as MainActivityRvAdapter.MyViewHolder).listItemAdd
                        val listItemRemove = holderNewPosition.listItemRemove
                        listItemRemove.visibility = View.INVISIBLE
                        listItemAdd.visibility = View.INVISIBLE
                    }
                    clickedPosition = position
                }
            }

            /**
             * RecyclerView add entry to norma_table
             */

            @SuppressLint("InflateParams")
            override fun onAddButtonClicked(position: Int) {

                val dialog: DialogFragment = NormaListAddDialog(position)
                dialog.show(supportFragmentManager, "lol")

            }

            /**
             * RecyclerView remove entry from norma_table
             */

            override fun onRemoveButtonClicked(position: Int) {
                Toast.makeText(
                    this@MainActivity,
                    "You Clicked remove $position",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        /** ###################################
         *  END OF recyclerView onClick listener
         * #################################### */

        /**
         * Add new month to month_table
         */
        addMonth.setOnClickListener {
            pickDate()
        }

        /**
         * Remove month from month_table
         */
        removeMonth.setOnClickListener {
            var clearMainRv = false
            val inflater: LayoutInflater = LayoutInflater.from(this@MainActivity)
            val rootView = inflater.inflate(R.layout.remove_month, null)
            val monthListRecyclerView = RemoveMonthBinding.bind(rootView).monthList


            val selectMonthDialog: MaterialAlertDialogBuilder = MaterialAlertDialogBuilder(
                this@MainActivity
            ).setView(rootView).setTitle("Remove Month").setPositiveButton("Close") { self, _ ->
                clearMainRv(clearMainRv)
                self.dismiss()
            }

            selectMonthDialog.create()
            selectMonthDialog.show()

            monthListRecyclerView.setHasFixedSize(true)
            monthListRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
            val mLadapter = RemoveMonthRvAdapter(monthListArray) {
                if (it < 100000) {
                    removeMonth(it, monthListRecyclerView)
                    val currentMonth = Date.currentDateWithFormat("M/yyyy")
                    if (currentMonth == activeMonth) {
                        clearMainRv = true
                        activeMonth = ""
                    }
                } else {
                    clearMainRv = false
                    refreshMainRv(it)
                }

            }
            monthListRecyclerView.adapter = mLadapter
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun refreshMainRv(it: Int) {
        val m = monthListArray[it - 100000]
        val month = Date.monthAndYearNumber(m.month)
        normaListArray.clear()
        populateNormaList(month)

    }

    private fun removeMonth(it: Int, rv: RecyclerView) {
        val m = monthListArray[it]
        val monthToRemove = Date.monthAndYearWord(m.month)
        val match = "%$monthToRemove%"

        monthListArray.removeAt(it)
        rv.adapter?.notifyItemRemoved(it)
        GlobalScope.launch(Dispatchers.IO) {
            appDb.normaDao().deleteByMonthFromNormaTable(match)
            appDb.normaDao().deleteByMonthFromMonthTable(m.month)

        }

    }

    /** ##################
     *  END OF onCreate
     *  ################## */

    /**
     * Add new entry to norma_table & normaListArray: Arraylist
     */

    @OptIn(DelicateCoroutinesApi::class)
    @SuppressLint("NotifyDataSetChanged")
    private fun addData(date: String, normaHours: Double, workingHours: Double, workplace: String) {

        val norma = Norma(null, date, normaHours, workingHours, workplace)
        GlobalScope.launch(Dispatchers.IO) {
            appDb.normaDao().insert_norma(norma)
        }
        //normaListArray.add(NormaList(date, normaHours, workingHours, workplace))
        recyclerView.adapter!!.notifyDataSetChanged()
    }

    /**
     * Add new month to month_table and monthListArray: Arraylist
     * Create entries for added month in norma_table
     */

    private fun addMonth(year: Int, month: Int, day: Int) {

        var fixMonth = month
        fixMonth++
        val date = "$day/$fixMonth/$year"
        val monthToAdd = Month(null, date)
        var norma: Norma
        val yearMonth = YearMonth.of(year, fixMonth)
        val monthLength = yearMonth.lengthOfMonth()
        monthListArray.add(MonthList(date))

        GlobalScope.launch(Dispatchers.IO) {
            appDb.normaDao().insert_month(monthToAdd)
            for (day1 in 1..monthLength) {
                val d = "$day1/$fixMonth/$year"
                //val df = Date.formatDate(d, "d/M/yyyy", "dMMyy").toInt()

                norma = Norma(null, d, 0.0, 0.0, "linija")
                appDb.normaDao().insert_norma(norma)
            }
        }
    }

    /**
     * Read from norma_Table and add data to normaListArray: Arraylist
     */

    @SuppressLint("NotifyDataSetChanged")
    @OptIn(DelicateCoroutinesApi::class)
    private fun populateNormaList(withMonth: String): ArrayList<NormaList> {

        GlobalScope.launch(Dispatchers.IO) {
            //Prikaze vnose za tekoci mesec...

            val match = "%$withMonth%"
            val dbData: List<Norma> = appDb.normaDao().findByMonth(match)
            var normaList: NormaList
            dbData.forEach {

                normaList = NormaList(
                    date = it.norma_date!!,
                    normaHours = it.normaHours!!,
                    workingHours = it.workingHours!!,
                    workplace = it.workplace!!
                )

                normaListArray.add((normaList))
            }
            withContext(Dispatchers.Main) {
                recyclerView.adapter!!.notifyDataSetChanged()
            }
        }
        return normaListArray


    }

    /**
     * Read from month_table and add data to monthListArray: ArrayList
     */

    @OptIn(DelicateCoroutinesApi::class)
    private fun populateMonthList(): ArrayList<MonthList> {
        val monthListArray: ArrayList<MonthList> = arrayListOf()
        GlobalScope.launch(Dispatchers.IO) {
            val dbData: List<Month> = appDb.normaDao().getAllMonth()
            var monthList: MonthList
            dbData.forEach {
                monthList = MonthList(
                    month = it.month!!
                )
                monthListArray.add(monthList)
            }
        }
        return monthListArray
    }

    private fun pickDate() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(
            this,
            { _: DatePicker, mYear: Int, mMonth: Int, mDay: Int ->
                addMonth(mYear, mMonth, mDay)
            },
            year,
            month,
            day
        ).show()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun clearMainRv(clear: Boolean) {
        if (clear) {
            normaListArray.clear()
            recyclerView.adapter!!.notifyDataSetChanged()
        }
    }

    override fun values(normaUre: Double, delUre: Double, delMesto: String, position: Int) {
        with(normaListArray[position]) {
            this.normaHours = normaUre
            this.workingHours = delUre
            this.workplace = delMesto
            //recyclerView.adapter!!.notifyItemChanged(position)
            recyclerView.adapter!!.notifyDataSetChanged()
            GlobalScope.launch(Dispatchers.IO) {
                appDb.normaDao().update(normaListArray[position].date, normaUre, delUre, delMesto)
            }
        }
    }

}

