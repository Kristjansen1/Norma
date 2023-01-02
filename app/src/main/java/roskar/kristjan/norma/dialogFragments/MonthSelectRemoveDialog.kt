package roskar.kristjan.norma.dialogFragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import roskar.kristjan.norma.R
import roskar.kristjan.norma.adapter.MonthSelectRemoveRvAdapter
import roskar.kristjan.norma.databinding.RemoveMonthBinding
import roskar.kristjan.norma.model.MonthList

class MonthSelectRemoveDialog(private val monthListArray: ArrayList<MonthList>, context: Context) :
    BottomSheetDialogFragment() {

    private lateinit var listener: MonthSelectRemoveInterface

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val rootView = inflater.inflate((R.layout.remove_month), null)

        val monthListRecyclerView = RemoveMonthBinding.bind(rootView).monthList

        monthListRecyclerView.setHasFixedSize(true)
        monthListRecyclerView.layoutManager = LinearLayoutManager(context)

        val mLadapter = MonthSelectRemoveRvAdapter(monthListArray) {
            if (it < 100000) {

                listener.checkClearMainRv(it)
                listener.removeMonth(it, monthListRecyclerView)

            } else {
                listener.refreshMainRv(it)
            }
        }
        monthListRecyclerView.adapter = mLadapter
        return rootView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as MonthSelectRemoveInterface
        } catch (e: java.lang.ClassCastException) {
            throw java.lang.ClassCastException((context.toString() + "Must implement MonthSelectRemoveInterface"))
        }
    }


    interface MonthSelectRemoveInterface {
        //fun monthSelectRemoveClickedPos(position: Int)
        fun removeMonth(position: Int, rv: RecyclerView)
        fun checkClearMainRv(position: Int)
        fun refreshMainRv(position: Int)
    }
}