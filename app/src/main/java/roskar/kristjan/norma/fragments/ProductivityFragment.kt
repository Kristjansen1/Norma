package roskar.kristjan.norma.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentProductivityBinding
import roskar.kristjan.norma.adapter.RecyclerviewAdapter
import roskar.kristjan.norma.model.Month
import roskar.kristjan.norma.utilities.Util
import roskar.kristjan.norma.viewModel.ViewModel

class ProductivityFragment : Fragment() {


    private val viewModel: ViewModel by activityViewModels()
    //lateinit var binding: FragmentProductivityBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        super.onCreateView(inflater, container, savedInstanceState)
        val rootView = inflater.inflate((R.layout.fragment_productivity),container,false)

        //binding = FragmentProductivityBinding.inflate(layoutInflater)
        val recyclerView = FragmentProductivityBinding.bind(rootView).recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)

        val recyclerViewAdapter = RecyclerviewAdapter(context,rootView,viewModel)

        recyclerView.adapter = recyclerViewAdapter

        viewModel.activeMonthDates.observe(viewLifecycleOwner) {
            recyclerViewAdapter.updateList(it)
        }

        //viewModel.addMonth(2023,4,1)
        // Inflate the layout for this fragment
        //viewModel.setActiveMonth("may 2023")
        //viewModel.addMonth(2023,3,2)





        return rootView
    }
}