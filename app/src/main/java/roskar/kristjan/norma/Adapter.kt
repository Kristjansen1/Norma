package roskar.kristjan.norma

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import roskar.kristjan.norma.databinding.ListItemBinding
import roskar.kristjan.norma.databinding.ListItemEtBinding

class Adapter (private val itemList: ArrayList<ItemList>) : RecyclerView.Adapter<Adapter.MyViewHolder>() {

    inner class MyViewHolder(binding: ListItemBinding, listener: onItemClickListener): RecyclerView.ViewHolder(binding.root) {
        val listItemDate = binding.listItemDate
        val listItemNormaHours = binding.listItemNormaHours
        val listItemWorkingHours = binding.listItemWorkingHours
        val listItemWorkplace = binding.listItemWorkplace
        val selectedPos = RecyclerView.NO_POSITION


        init {
            binding.root.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    private lateinit var mListener: onItemClickListener
    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding,mListener)

    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = itemList[position]
        Log.d("itemcur",currentItem.toString())

        val dateFormatted = Date.formatDate(currentItem.date.toString(),"dMMyy","dd. EE")
        holder.listItemDate.text = dateFormatted
        //holder.listItemHours.setText(currentItem.hours.toString())
        holder.listItemNormaHours.text = currentItem.normaHours.toString()
        holder.listItemWorkingHours.text = currentItem.workingHours.toString()
        holder.listItemWorkplace.text = currentItem.workplace.toString()

    }

    override fun getItemCount(): Int {
        return itemList.size
    }

}