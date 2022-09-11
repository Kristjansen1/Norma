package roskar.kristjan.norma

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import roskar.kristjan.norma.databinding.ActivityMainBinding


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

        itemListArray = Data.populate(appDb,Date.currentDateWithFormat("MMyy"))
        Data.displayData(this,recyclerView,itemListArray)

        binding.btnAdd.setOnClickListener() {
            if ((binding.etDate.text.isNotEmpty()) && (binding.etHours.text.isNotEmpty())) {
                val date = binding.etDate.text.toString().toInt()
                val hours = binding.etHours.text.toString().toInt()
                Data.addData(appDb,date, hours,itemListArray,recyclerView)
            }
        }

        binding.btnDelete.setOnClickListener() {
            Data.deleteData()
        }
    }

    //@SuppressLint("NotifyDataSetChanged")


}

