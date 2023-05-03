package roskar.kristjan.norma.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.databinding.FragmentAddUpdateEntryBinding
import roskar.kristjan.norma.model.Month
import roskar.kristjan.norma.model.Productivity
import roskar.kristjan.norma.utilities.Constants
import roskar.kristjan.norma.utilities.Util
import roskar.kristjan.norma.viewModel.ViewModel
import java.math.BigDecimal
import java.time.LocalDate

class AddUpdateEntryFragment : Fragment() {

    private val viewModel: ViewModel by activityViewModels()
    private val item: Productivity = Productivity(null,"",0.0,0.0)
    private val month: Month = Month(null,"",0,0.0,0.0)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_add_update_entry, container, false)

        val editTxtProductivity = FragmentAddUpdateEntryBinding.bind(rootView).productivity
        val editTxtWorkHours = FragmentAddUpdateEntryBinding.bind(rootView).workHours
        val txtFieldDate = FragmentAddUpdateEntryBinding.bind(rootView).date
        val addUpdateBtn = FragmentAddUpdateEntryBinding.bind(rootView).addUpdate

        var productivity = viewModel.clickedItem.value?.productivityHours.toString()
        val workHours = viewModel.clickedItem.value?.workingHours.toString()
        val date = viewModel.clickedItem.value?.date.toString()



        if (productivity == "0.0" && workHours == "0.0") {
            addUpdateBtn.text = "Add"
            productivity = ""
        } else {
            addUpdateBtn.text = "Update"
        }
        if (workHours == "0.0") editTxtWorkHours.setText("8") else editTxtWorkHours.setText(workHours)

        editTxtProductivity.setText(productivity)
        addUpdateBtn.setOnClickListener {

            val p = editTxtProductivity.text.toString()
            val w = editTxtWorkHours.text.toString()

            if (productivity != p || workHours != w) {     // check if any changes ware actualy made
                if (p.isNotEmpty() && w.isNotEmpty()) {    // check if edit-boxes are not empty

                    // productivity_table update
                    item.productivityHours = p.toDoubleOrNull()!!
                    item.workingHours = w.toDoubleOrNull()!!
                    item.date = viewModel.clickedItem.value?.date.toString()
                    viewModel.updateEntryProductivityTable(item)

                    // month table update
                    val oldProductivityHours: Double = if (productivity.toDoubleOrNull() == null) {
                        0.0
                    } else {
                        productivity.toDoubleOrNull()!!
                    }

                    if (item.productivityHours > oldProductivityHours) {

                        val x = oldProductivityHours.toBigDecimal()
                        val y = item.productivityHours.toBigDecimal()
                        val r = y-x
                        Util.log("clicked add")
                        viewModel.refreshMonthDb("addToProgress",item.date,r)
                    }

                    if (item.productivityHours < oldProductivityHours) {
                        Util.log("clicked remove")
                        val x = BigDecimal(oldProductivityHours)
                        val y = BigDecimal(item.productivityHours.toString())
                        val r = x-y
                        viewModel.refreshMonthDb("removeFromProgress",item.date,r)
                    }

                    Util.toast(requireContext(), "Success!")
                    parentFragmentManager.popBackStackImmediate()

                } else {
                    Util.toast(requireContext(), "INPUT ERROR")
                }
            } else {
                Util.toast(requireContext(), "WE DID NOTHING.")
                parentFragmentManager.popBackStackImmediate()
            }
        }

        val day = Util.formatDate(date,"d. EEEE",Constants.DAY_MONTH_YEAR_NUMBER)

        editTxtProductivity.setText(productivity)
        txtFieldDate.text = day


        return rootView
    }
}