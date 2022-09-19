package roskar.kristjan.norma

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import roskar.kristjan.norma.databinding.ActivityMainBinding
import roskar.kristjan.norma.room.AppDatabase



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
        //Data.fillDB(appDb)

        itemListArray = Data.populate(appDb,Date.currentDateWithFormat("MMyy"))

        Data.displayData(this,recyclerView,itemListArray)

    }

    //@SuppressLint("NotifyDataSetChanged")


}

