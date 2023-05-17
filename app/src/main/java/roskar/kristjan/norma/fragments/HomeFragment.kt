package roskar.kristjan.norma.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.rotationMatrix
import androidx.fragment.app.activityViewModels
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentHomeBinding
import roskar.kristjan.norma.utilities.Util
import roskar.kristjan.norma.viewModel.ViewModel
import kotlin.math.roundToInt


class HomeFragment : Fragment() {

    private val viewModel: ViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        val progressBar = FragmentHomeBinding.bind(rootView).progressBar
        val progressTxt = FragmentHomeBinding.bind(rootView).progressPercent
        val progresHourTxt = FragmentHomeBinding.bind(rootView).progressHours
        val toDate = FragmentHomeBinding.bind(rootView).textView2
        progressBar.progress = 75

        val vmamd = viewModel.activeMonthData
        //if (vmamd.value != null) {
            vmamd.observe(viewLifecycleOwner) {

                Util.log("homefragment starting")
                val firstStart = viewModel.firstStart.value

                if (firstStart == true) {
                    viewModel.setActiveMonthProgress(it.monthlyProgress)
                    viewModel.setActiveMonthWorkedHours(it.workedHours)
                    viewModel.setFirstStart(false)
                }
                val progress = it.monthlyProgress
                val workedHours = it.workedHours
                val workingDays = it.workingDays
                val maxProductivity = workingDays * 7.2

                var diff = maxProductivity - progress

                val percent = (progress / maxProductivity) * 100
                Util.log(percent.toString())

                progressBar.progress = percent.toInt()
                val p = String.format("%.2f", percent) + " %"
                val ph = "$progress h / $maxProductivity h"
                progressTxt.text = p
                progresHourTxt.text = ph

                val nhMax = workedHours * 0.9
                diff = progress - nhMax

                val procenti = (diff / maxProductivity) * 100

                val s = String.format("%.2f", procenti) + " % / " + String.format("%.2f", (diff)) + " h"

                toDate.text = s



           // }
        }
        return rootView
    }
}