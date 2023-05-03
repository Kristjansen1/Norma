package roskar.kristjan.norma.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentProductivityBinding
import com.example.myapplication.databinding.RecyclerViewItemBinding
import roskar.kristjan.norma.MainActivity
import roskar.kristjan.norma.fragments.AddUpdateEntryFragment
import roskar.kristjan.norma.model.Productivity
import roskar.kristjan.norma.viewModel.ViewModel

class RecyclerviewAdapter(
    val context: Context?,
    val rootView: View,
    val viewModel: ViewModel
) : RecyclerView.Adapter<RecyclerviewAdapter.ViewHolder>()  {

    private val allDatesList = ArrayList<Productivity>()
    var clickedPosition = RecyclerView.NO_POSITION


    inner class ViewHolder(binding: RecyclerViewItemBinding) : RecyclerView.ViewHolder(binding.root) {

        var rvItemDate = binding.rvItemDate
        var rvItemProductivity = binding.rvItemProductivity
        var rvEditButton = binding.rvEditButton
        var rvItemPercent = binding.rvItemPercent
        init {
            val recyclerView = FragmentProductivityBinding.bind(rootView).recyclerView
            binding.root.setOnClickListener {
                rvEditButton.visibility = View.VISIBLE
                if (clickedPosition == RecyclerView.NO_POSITION) {
                   clickedPosition = adapterPosition
               } else {
                   if (clickedPosition != adapterPosition) {
                       val vhForAdapterPosition = recyclerView.findViewHolderForAdapterPosition(clickedPosition)
                       vhForAdapterPosition?.let {
                           val btn = (vhForAdapterPosition as RecyclerviewAdapter.ViewHolder).rvEditButton
                           btn.visibility = View.INVISIBLE
                       }
                       clickedPosition = adapterPosition
                   } else {
                       if (clickedPosition == adapterPosition) {
                           if (rvEditButton.visibility == View.VISIBLE) {
                               rvEditButton.visibility = View.INVISIBLE
                               clickedPosition = RecyclerView.NO_POSITION
                           }
                       }
                   }
               }
           }
            rvEditButton.setOnClickListener {
                val c = context as MainActivity
                c.supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, AddUpdateEntryFragment()).addToBackStack("add").commit()
                viewModel.setClickedItem(allDatesList[adapterPosition])
               /* val x = c.findViewById<LinearLayout>(R.id.linearLayout)
                x.visibility = View.GONE*/
              /*  val intent = Intent(rootView.context,AddUpdateEntryActivity::class.java)
                rootView.context.startActivity(intent)*/
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerViewItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.rvItemDate.text = allDatesList[position].date
        holder.rvItemProductivity.text = allDatesList[position].productivityHours.toString()
        if (clickedPosition == position) {
            holder.rvEditButton.visibility = View.VISIBLE
        }
        holder.setIsRecyclable(false)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<Productivity>) {
        allDatesList.clear()
        allDatesList.addAll(newList)
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        return allDatesList.size
    }


}