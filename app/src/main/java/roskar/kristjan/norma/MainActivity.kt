package roskar.kristjan.norma

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import roskar.kristjan.norma.databinding.ActivityMainBinding
import roskar.kristjan.norma.databinding.ListItemBinding
import roskar.kristjan.norma.room.AppDatabase
import java.lang.reflect.Type

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView : RecyclerView
    private var itemListArray : ArrayList<ItemList> = arrayListOf()
    private lateinit var appDb : AppDatabase
    private lateinit var binding: ActivityMainBinding
    private var clickedPosition = 82

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        appDb = AppDatabase.getDatabase(this)
        recyclerView = binding.recylerView


        recyclerView.let {
            it.layoutManager = LinearLayoutManager(this)
            it.setHasFixedSize(true)
            it.addItemDecoration(DividerItemDecoration(recyclerView.context,DividerItemDecoration.VERTICAL))
        }

        itemListArray = Data.populate(appDb,Date.currentDateWithFormat("MMyy"))
        Data.displayData(this,recyclerView,itemListArray)


        val adapter = Adapter(itemListArray)
        recyclerView.adapter = adapter


        adapter.setOnItemClickListener(object : Adapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                if (clickedPosition != position) {
                    val holder = recyclerView.findViewHolderForAdapterPosition(clickedPosition)
                    holder?.let {
                        val listItemAdd = (holder as Adapter.MyViewHolder).listItemAdd
                        val listItemRemove = holder.listItemRemove
                        listItemRemove.visibility = View.INVISIBLE
                        listItemAdd.visibility = View.INVISIBLE
                    }
                    clickedPosition = position
                }
            }
            override fun onAddButtonClicked(position: Int) {
                Toast.makeText(
                    this@MainActivity,
                    "You Clicked ADD $position",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onRemoveButtonClicked(position: Int) {
                Toast.makeText(
                    this@MainActivity,
                    "You Clicked remove $position",
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
    }

}

