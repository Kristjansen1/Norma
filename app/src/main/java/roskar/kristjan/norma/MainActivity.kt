package roskar.kristjan.norma

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import roskar.kristjan.norma.databinding.ActivityMainBinding
import roskar.kristjan.norma.room.AppDatabase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView : RecyclerView
    private var itemListArray : ArrayList<ItemList> = arrayListOf()
    private lateinit var appDb : AppDatabase


    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appDb = AppDatabase.getDatabase(this)
        recyclerView = binding.recylerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context,DividerItemDecoration.VERTICAL))

        itemListArray = Data.populate(appDb,Date.currentDateWithFormat("MMyy"))

        var cDate = LocalDateTime.now()
        val formater = DateTimeFormatter.ofPattern("ddMMyy")
        var formated: String

/*        GlobalScope.launch {
            for (x in 1..1256) {
                val r = (1..8).shuffled().last()
                val nDate = cDate.plusDays(x.toLong())
                formated = nDate.format(formater)

                val n = Norma(null,formated.toInt(),r,8,"linija")
                appDb.normaDao().insert(n)

            }
        }*/



        //Data.displayData(this,recyclerView,itemListArray)

        /*binding.btnAdd.setOnClickListener() {
            if ((binding.etDate.text.isNotEmpty()) && (binding.etHours.text.isNotEmpty())) {
                val date = binding.etDate.text.toString().toInt()
                val hours = binding.etHours.text.toString().toInt()
                Data.addData(appDb,date, hours,itemListArray,recyclerView)
            }
        }

        binding.btnDelete.setOnClickListener() {
            Data.deleteData()
        }*/

        val adapter = Adapter(itemListArray)
        recyclerView.adapter = adapter

        adapter.setOnItemClickListener(object : Adapter.onItemClickListener {
            override fun onItemClick(position: Int) {

                Toast.makeText(this@MainActivity,"You Clicked on item no. $position", Toast.LENGTH_SHORT).show()

            }

        })
    }

    //@SuppressLint("NotifyDataSetChanged")


}

