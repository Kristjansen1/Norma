package roskar.kristjan.norma.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import roskar.kristjan.norma.Date
import roskar.kristjan.norma.model.MonthList
import roskar.kristjan.norma.databinding.SelectMonthLineBinding

class RemoveMonthRvAdapter(private val monthList: ArrayList<MonthList>) :
    RecyclerView.Adapter<RemoveMonthRvAdapter.ViewHolder>() {

    class ViewHolder(binding: SelectMonthLineBinding) : RecyclerView.ViewHolder(binding.root) {
        val removeMonth = binding.remove
        val month1 = binding.month
        val root = binding.root
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            SelectMonthLineBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = monthList[position]
        var m = currentItem.month.toString()
        var mf = Date.formatDate(m, "d/M/yyyy", "LLLL yyyy")
        holder.month1.text = mf

    }

    override fun getItemCount(): Int {
        return monthList.size
    }


}
