package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import roskar.kristjan.norma.model.Productivity
import roskar.kristjan.norma.viewModel.ViewModel

class ProductivityFragment : Fragment() {

    private val viewModel: ViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //viewModel.setActiveMonth("may 2023")
    /*    viewModel.addMonth(2023,1,2)
        viewModel.addMonth(2023,2,2)
        viewModel.addMonth(2023,3,2)
        viewModel.addMonth(2023,4,2)*/

   /*     val x = ArrayList<Productivity>()
        x.addAll(viewModel.activeMonthDates)

        x.forEach {

            Log.d("list",it.date)
        }*/


        return inflater.inflate(R.layout.fragment_productivity, container, false)
    }
}