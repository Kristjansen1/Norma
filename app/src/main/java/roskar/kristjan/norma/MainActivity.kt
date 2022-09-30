package roskar.kristjan.norma

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.DatePicker
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import roskar.kristjan.norma.adapter.MainActivityRvAdapter
import roskar.kristjan.norma.adapter.RemoveMonthRvAdapter
import roskar.kristjan.norma.databinding.ActivityMainBinding
import roskar.kristjan.norma.databinding.AddDataBinding
import roskar.kristjan.norma.databinding.RemoveMonthBinding
import roskar.kristjan.norma.model.MonthList
import roskar.kristjan.norma.model.NormaList
import roskar.kristjan.norma.room.AppDatabase
import roskar.kristjan.norma.room.Month
import roskar.kristjan.norma.room.Norma
import java.time.YearMonth

/**
 * lol777
 */
class MainActivity : AppCompatActivity() {

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

        //itemListArray = Data.populate(appDb, Date.currentDateWithFormat("Myy"))
        normaListArray = populateNormaList("1/2023")
        activeMonth = "1/2023"
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

                val listener = object : RecyclerView.SimpleOnItemTouchListener() {
                    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                        return true
                    }
                }

                recyclerView.addOnItemTouchListener(listener)
                val inflater: LayoutInflater = LayoutInflater.from(this@MainActivity)
                val view = inflater.inflate(R.layout.add_data, null)

                val buttonDismiss = AddDataBinding.bind(view).popupDismiss
                val buttonAdd = AddDataBinding.bind(view).popupAdd
                val popupWindow = PopupWindow(
                    view,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
                )
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    popupWindow.isTouchModal = false
                }
                popupWindow.isOutsideTouchable = false
                popupWindow.isFocusable = true
                popupWindow.showAtLocation(
                    binding.rootLayout,
                    Gravity.CENTER,
                    0,
                    0
                )
                popupWindow.setOnDismissListener {
                    recyclerView.removeOnItemTouchListener(listener)
                }
                buttonDismiss.setOnClickListener {
                    popupWindow.dismiss()
                }
                buttonAdd.setOnClickListener {

                }
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
            val inflater: LayoutInflater = LayoutInflater.from(this@MainActivity)
            val rootView = inflater.inflate(R.layout.remove_month, null)
            val monthListRecyclerView = RemoveMonthBinding.bind(rootView).monthList
            val mLadapter = RemoveMonthRvAdapter(monthListArray) {
                monthListArray.removeAt(it)
                monthListRecyclerView.adapter?.notifyItemRemoved(it)
                val currentMonth = Date.currentDateWithFormat("M/yyyy")
                if (currentMonth == activeMonth) {
                    //*TODO gggg
                }
            }
            monthListRecyclerView.adapter = mLadapter
            monthListRecyclerView.setHasFixedSize(true)
            monthListRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)

            val selectMonthDialog: MaterialAlertDialogBuilder = MaterialAlertDialogBuilder(
                this@MainActivity,
                R.style.roundCornerDialog
            ).setView(rootView).setTitle("Remove Month").setPositiveButton("Close") { self, _ ->
                lol(1)
                self.dismiss()
            }

            selectMonthDialog.create()
            selectMonthDialog.show()


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
    private fun addData(date: String, normaHours: Int, workingHours: Int, workplace: String) {

        val norma = Norma(null, date, normaHours, workingHours, workplace)
        GlobalScope.launch(Dispatchers.IO) {
            appDb.normaDao().insert_norma(norma)
        }
        normaListArray.add(NormaList(date, normaHours, workingHours, workplace))
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

                norma = Norma(null, d, 0, 0, "linija")
                appDb.normaDao().insert_norma(norma)
            }
        }
    }

    /**
     * Read from norma_Table and add data to normaListArray: Arraylist
     */

    @OptIn(DelicateCoroutinesApi::class)
    private fun populateNormaList(withMonth: String): ArrayList<NormaList> {

        GlobalScope.launch(Dispatchers.IO) {

            //Prikaze vnose za tekoci mesec...

            val match = "%$withMonth%"
            val dbData: List<Norma> = appDb.normaDao().findByMonth(match)
            Log.d("findbymonth", match)
            var normaList: NormaList
            dbData.forEach {

                normaList = NormaList(
                    date = it.norma_date!!,
                    normaHours = it.normaHours!!,
                    workingHours = it.workingHours!!,
                    workplace = it.workplace!!
                )

                Log.d("nevemkej", it.norma_date.toString())
                normaListArray.add((normaList))
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


    private fun lol(a: Int) {
        Log.d("clicked", "$a")
    }

}

