package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import roskar.kristjan.norma.viewModel.ViewModel


class HomeFragment : Fragment() {

    private val viewModel: ViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Log.d("nekineki",viewModel.nekineki.value.toString())
        viewModel.setNekiNeki("blabla")
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
}