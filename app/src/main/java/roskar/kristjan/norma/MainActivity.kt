package roskar.kristjan.norma

import android.os.Bundle
import android.util.Log
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

        val addMonth = binding.addMonth
        appDb = AppDatabase.getDatabase(this)
        recyclerView = binding.recylerView


        recyclerView.let {
            it.layoutManager = LinearLayoutManager(this)
            it.setHasFixedSize(true)
            it.addItemDecoration(DividerItemDecoration(recyclerView.context,DividerItemDecoration.VERTICAL))
        }

        itemListArray = Data.populate(appDb, Date.currentDateWithFormat("Myy"))
        Data.displayData(this, recyclerView, itemListArray)


        val adapter = Adapter(itemListArray)
        recyclerView.adapter = adapter
        AdapterOnClick.onClickOperation(adapter, context = this, recyclerView, binding.rootLayout)

        addMonth.setOnClickListener {
            Date.pickDate(this, appDb)
        }

    }

}

