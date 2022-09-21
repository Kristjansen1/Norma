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
    var clickedPosition = 82
    var firstClick = true
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



        val adapter = Adapter(itemListArray)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter.setOnItemClickListener(object : Adapter.onItemClickListener {

            override fun onItemClick(position: Int) {
                if (firstClick) {
                    clickedPosition = position
                    firstClick = false
                }
                var holder = recyclerView.findViewHolderForAdapterPosition(position)
                val listItemAdd = (holder as Adapter.MyViewHolder).listItemAdd
                val listItemRemove = holder.listItemRemove

                if (clickedPosition != position && !firstClick) {
                    Log.d("clicked", "$clickedPosition lol $position")
                    var holder = recyclerView.findViewHolderForAdapterPosition(clickedPosition)
                    val listItemAdd = (holder as Adapter.MyViewHolder).listItemAdd
                    val listItemRemove = holder.listItemRemove
                    listItemRemove.visibility = View.INVISIBLE
                    listItemAdd.visibility = View.INVISIBLE
                    clickedPosition = position
                }
                Toast.makeText(
                    this@MainActivity,
                    "You Clicked on item no. $position",
                    Toast.LENGTH_SHORT
                ).show()

            }
            override fun onAddButtonClicked(position: Int) {
                Toast.makeText(
                    this@MainActivity,
                    "You Clicked ADD $position",
                    Toast.LENGTH_SHORT
                ).show()
            }

            /* override fun onAddButtonClicked(position: Int, listItemAdd: ImageView) {
             }*/

        })
    }

    //@SuppressLint("NotifyDataSetChanged")


}

