package roskar.kristjan.norma.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import roskar.kristjan.norma.Date
import roskar.kristjan.norma.model.NormaList
import roskar.kristjan.norma.databinding.ListItemBinding
import roskar.kristjan.norma.room.Norma

class MainActivityRvAdapter(private val normaList: ArrayList<NormaList>) :
    RecyclerView.Adapter<MainActivityRvAdapter.MyViewHolder>() {

    var clickedPosition = 82
    private lateinit var mListener: onItemClickListener

    inner class MyViewHolder(binding: ListItemBinding, listener: onItemClickListener) :
        RecyclerView.ViewHolder(binding.root) {
        val listItemDate = binding.listItemDate
        val listItemNormaHours = binding.listItemNormaHours
        val listItemWorkingHours = binding.listItemWorkingHours
        val listItemWorkplace = binding.listItemWorkplace
        val listItemAdd = binding.listItemAdd
        val listItemRemove = binding.listItemRemove
        val root = binding.listItemLayout
        init {
            binding.root.setOnClickListener {
                if (listItemAdd.isVisible) {
                    listItemAdd.visibility = View.INVISIBLE
                    listItemRemove.visibility = View.INVISIBLE
                    clickedPosition = 82
                } else {
                    listItemAdd.visibility = View.VISIBLE
                    listItemRemove.visibility = View.VISIBLE
                    listener.onItemClick(adapterPosition)
                    clickedPosition = adapterPosition

                }
            }
            listItemAdd.setOnClickListener {
                listener.onAddButtonClicked(adapterPosition)
            }
            listItemRemove.setOnClickListener {
                listener.onRemoveButtonClicked(adapterPosition)
            }
        }
    }

    interface onItemClickListener {
        fun onItemClick(position: Int)
        fun onAddButtonClicked(position: Int)
        fun onRemoveButtonClicked(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding, mListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        if (clickedPosition == position) {
            holder.listItemAdd.visibility = View.VISIBLE
            holder.listItemRemove.visibility = View.VISIBLE
        }

        val currentItem = normaList[position]
        Log.d("itemcur", "$currentItem $position")

        val dateFormatted = Date.formatDate(currentItem.date.toString(), "d/M/yyyy", "dd. EE")
        holder.listItemDate.text = dateFormatted
        holder.listItemNormaHours.text = currentItem.normaHours.toString()
        holder.listItemWorkingHours.text = currentItem.workingHours.toString()
        holder.listItemWorkplace.text = currentItem.workplace
        holder.setIsRecyclable(false)
    }

    override fun getItemCount(): Int {
        return normaList.size
    }

}