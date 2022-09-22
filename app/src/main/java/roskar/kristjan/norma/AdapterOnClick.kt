package roskar.kristjan.norma

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

object AdapterOnClick {

    var clickedPosition = 82
    fun onClickOperation(adapter: Adapter,
                         context: Context,
                         recyclerView: RecyclerView,
    ) {
        
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
                    context,
                    "You Clicked ADD $position",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onRemoveButtonClicked(position: Int) {
                Toast.makeText(
                    context,
                    "You Clicked remove $position",
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
    }
}