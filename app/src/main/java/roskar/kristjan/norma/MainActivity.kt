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
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.*
import roskar.kristjan.norma.adapter.MainActivityRvAdapter
import roskar.kristjan.norma.adapter.MonthSelectRemoveRvAdapter
import roskar.kristjan.norma.databinding.ActivityMainBinding
import roskar.kristjan.norma.databinding.RemoveMonthBinding
import roskar.kristjan.norma.dialogFragments.BottomSheetDialogF
import roskar.kristjan.norma.dialogFragments.BottomSheetDialogFSum
import roskar.kristjan.norma.dialogFragments.MonthSelectRemoveDialog
import roskar.kristjan.norma.dialogFragments.NormaListAddDialog
import roskar.kristjan.norma.model.MonthList
import roskar.kristjan.norma.model.NormaList
import roskar.kristjan.norma.room.AppDatabase
import roskar.kristjan.norma.room.Month
import roskar.kristjan.norma.room.Norma
import java.time.YearMonth

//lol1
@OptIn(DelicateCoroutinesApi::class)
class MainActivity : AppCompatActivity(), NormaListAddDialog.NormaListAddInterface,
    BottomSheetDialogF.BottomSheetDialogMenuInterface,
    MonthSelectRemoveDialog.MonthSelectRemoveInterface {

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
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        appDb = AppDatabase.getDatabase(this)
        recyclerView = binding.recyclerView


        recyclerView.let {
            it.layoutManager = LinearLayoutManager(this)
            it.setHasFixedSize(true)
            /*   it.addItemDecoration(
                   DividerItemDecoration(
                       recyclerView.context,
                       DividerItemDecoration.VERTICAL
                   )
               )*/
        }
        activeMonth = Date.currentDateWithFormat("M/yyyy")
        normaListArray = populateNormaList(activeMonth)
        monthListArray = populateMonthList()
        supportActionBar?.title = Date.currentDateWithFormat("MMM/yyyy")


        val adapter = MainActivityRvAdapter(normaListArray)
        recyclerView.adapter = adapter

        val navView = binding.navView
        navView.setOnItemSelectedListener {

            when (it.itemId) {
                R.id.menu -> {
                    val dialog: BottomSheetDialogFragment = BottomSheetDialogF()
                    dialog.show(supportFragmentManager, "lol")
                }
                R.id.sum -> {
                    val dialog: BottomSheetDialogFragment = BottomSheetDialogFSum(normaListArray)
                    dialog.show(supportFragmentManager, "lol")
                }
            }

            return@setOnItemSelectedListener true
        }

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
                if (!normaListArray[position].workingHours.isNaN()) {
                    val n = normaListArray[position].normaHours
                    val wh = normaListArray[position].workingHours
                    val wp = normaListArray[position].workplace
                    val dialog: BottomSheetDialogFragment = NormaListAddDialog(position, n, wh, wp)
                    //dialog.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.BottomSheet)
                    dialog.show(supportFragmentManager, "lol")
                } else {
                    val n = 0.0
                    val wh = 0.0
                    val wp = "Linija"
                    val dialog: BottomSheetDialogFragment = NormaListAddDialog(position, n, wh, wp)
                    //dialog.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.BottomSheet)
                    dialog.show(supportFragmentManager, "lol")
                }


            }

            /**
             * RecyclerView remove entry from norma_table
             */

            override fun onRemoveButtonClicked(position: Int) {

                normaListUpdateValue(NormaList("", 0.0, 0.0, "Linija"), position)

            }
        })

        /** ###################################
         *  END OF recyclerView onClick listener
         * #################################### */

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun refreshMainRv(position: Int) {
        val m = monthListArray[position - 100000]
        activeMonth = Date.monthAndYearNumber(m.month)
        normaListArray.clear()
        populateNormaList(activeMonth)
        supportActionBar?.title = Date.monthAndYear(m.month)


    }

    override fun removeMonth(position: Int, rv: RecyclerView) {
        val m = monthListArray[position]
        val monthToRemove = Date.monthAndYearNumber(m.month)
        val match = "%$monthToRemove%"
        Log.d("remove", match)

        monthListArray.removeAt(position)
        rv.adapter?.notifyItemRemoved(position)
        GlobalScope.launch(Dispatchers.IO) {
            val x = appDb.normaDao().deleteByMonthFromNormaTable(match)
            Log.d("remove", x.toString())
            appDb.normaDao().deleteByMonthFromMonthTable(m.month)

        }

    }

    /** #########################################################################################
     *  END OF onCreate
     *  ######################################################################################### */


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
            val dbData = appDb.normaDao().findByMonth(match)
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
            val dbData = appDb.normaDao().getAllMonth()
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


    @SuppressLint("NotifyDataSetChanged")
    private fun clearMainRv() {
        normaListArray.clear()
        recyclerView.adapter!!.notifyDataSetChanged()
    }

    /**
     * Remove month from month_table
     */
    override fun navViewRemoveMonth() {
        val dialog: BottomSheetDialogFragment = MonthSelectRemoveDialog(monthListArray)
       /* dialog.setStyle(
            DialogFragment.STYLE_NO_TITLE,R.style.BottomSheet
        )*/
        dialog.show(supportFragmentManager, "lol")
    }


    /**
     * Select new month to add
     * TODO: create only month selector
     */

    override fun pickDate() {
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
    /**
     * Update value for selected position in norma_table & normaListArray
     */

    @SuppressLint("NotifyDataSetChanged")
    override fun normaListUpdateValue(item: NormaList, position: Int) {

        with(normaListArray[position]) {
            this.normaHours = item.normaHours
            this.workingHours = item.workingHours
            this.workplace = item.workplace
            recyclerView.adapter!!.notifyDataSetChanged()

            GlobalScope.launch(Dispatchers.IO) {
                appDb.normaDao().update(
                    normaListArray[position].date,
                    item.normaHours,
                    item.workingHours,
                    item.workplace
                )
            }
        }
    }

    override fun checkClearMainRv(position: Int) {
        val currentMonth = Date.monthAndYearNumber(monthListArray[position].month)
        if (currentMonth == activeMonth) {
            clearMainRv()
        }
    }
}


