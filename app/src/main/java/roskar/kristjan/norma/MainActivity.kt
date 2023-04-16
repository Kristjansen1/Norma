package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import roskar.kristjan.norma.utilities.Constants
import roskar.kristjan.norma.utilities.Util
import roskar.kristjan.norma.viewModel.ViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val fragmentManager: FragmentManager = supportFragmentManager

    private val viewModel: ViewModel by viewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavigationView = binding.bottomNavigationView
        val textViewActiveMonth = binding.activeMonth


        val activeMonthObserver = Observer<String> {
            textViewActiveMonth.text = Util.monthYearWord(viewModel.activeMonth.value.toString(), Constants.DAY_MONTH_YEAR_NUMBER)
        }
        viewModel.activeMonth.observe(this,activeMonthObserver)

        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    fragmentManager.beginTransaction().replace(R.id.fragmentContainerView, HomeFragment()).commit()
                }
                R.id.productivity -> {
                    fragmentManager.beginTransaction().replace(R.id.fragmentContainerView, ProductivityFragment()).commit()
                }
                R.id.stats -> {
                    fragmentManager.beginTransaction().replace(R.id.fragmentContainerView, StatsFragment()).commit()
                }

            }
            return@setOnItemSelectedListener true
        }
    }
}