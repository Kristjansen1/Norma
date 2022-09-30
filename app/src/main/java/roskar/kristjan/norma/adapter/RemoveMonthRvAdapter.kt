package roskar.kristjan.norma.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import roskar.kristjan.norma.Date
import roskar.kristjan.norma.databinding.RemoveMonthLineBinding
import roskar.kristjan.norma.model.MonthList

class RemoveMonthRvAdapter(
    private val monthList: ArrayList<MonthList>,
    private val monthClicked: (Int) -> Unit
) :
    RecyclerView.Adapter<RemoveMonthRvAdapter.MyViewHolder>() {

    class MyViewHolder(binding: RemoveMonthLineBinding, clickedPosition: (Int) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {

        private val removeMonth = binding.remove
        val month = binding.month

        init {
            removeMonth.setOnClickListener {
                clickedPosition(adapterPosition)
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val holder =
            RemoveMonthLineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(holder) {
            monthClicked(it)
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = monthList[position]
        val m = currentItem.month
        val mf = Date.formatDate(m, "d/M/yyyy", "LLLL yyyy")
        holder.month.text = mf
    }

    override fun getItemCount(): Int {
        return monthList.size
    }


}
