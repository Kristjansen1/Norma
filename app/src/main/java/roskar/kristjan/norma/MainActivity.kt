package roskar.kristjan.norma

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import com.example.myapplication.R
import roskar.kristjan.norma.fragments.HomeFragment
import com.example.myapplication.databinding.ActivityMainBinding
import roskar.kristjan.norma.fragments.ProductivityFragment
import roskar.kristjan.norma.fragments.SettingsFragment
import roskar.kristjan.norma.model.Month
import roskar.kristjan.norma.model.Productivity
import roskar.kristjan.norma.utilities.Constants
import roskar.kristjan.norma.utilities.Util
import roskar.kristjan.norma.viewModel.ViewModel
import kotlin.Exception
import kotlin.NullPointerException
import kotlin.math.max
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val fragmentManager: FragmentManager = supportFragmentManager

    private val viewModel: ViewModel by viewModels()
    private var lastSelectedMenuItem = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavigationView = binding.bottomNavigationView
        val textViewActiveMonth = binding.activeMonth


        val activeMonthObserver = Observer<String> {
            textViewActiveMonth.text = Util.monthYearWord(
                viewModel.activeMonth.value.toString(),
                Constants.DAY_MONTH_YEAR_NUMBER
            )
        }

      /*  val monthDataObserver = Observer<Month> {
            Util.log(it.month)
        }

        viewModel.activeMonthData.observe(this,monthDataObserver)*/



        viewModel.activeMonth.observe(this,activeMonthObserver)


        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    if (notSelected(it.itemId)) {
                        lastSelectedMenuItem = it.itemId
                        fragmentManager.beginTransaction().replace(R.id.fragmentContainerView, HomeFragment(),"home").addToBackStack("home").commit()
                        Util.log("visible")
                    }
                }
                R.id.productivity -> {
                    if (notSelected(it.itemId)) {
                        lastSelectedMenuItem = it.itemId
                        fragmentManager.beginTransaction().replace(R.id.fragmentContainerView, ProductivityFragment(),"productivity").addToBackStack("productivity").commit()

                        Util.log("visible")
                    }
                }
                R.id.settings -> {
                    if (notSelected(it.itemId)) {
                        lastSelectedMenuItem = it.itemId
                        fragmentManager.beginTransaction().replace(R.id.fragmentContainerView, SettingsFragment(),"settings").addToBackStack("settings").commit()

                    }
                }
            }
            return@setOnItemSelectedListener true
        }
    }

    private fun notSelected(itemId: Int): Boolean {
        if (itemId == lastSelectedMenuItem) {
            return false
        }
        return true
    }
}