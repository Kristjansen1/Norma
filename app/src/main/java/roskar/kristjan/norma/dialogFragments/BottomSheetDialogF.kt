package roskar.kristjan.norma.dialogFragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import roskar.kristjan.norma.R
import roskar.kristjan.norma.databinding.BottomSheetMenuBinding

class BottomSheetDialogF : BottomSheetDialogFragment() {

    private lateinit var listener: BottomSheetDialogMenuInterface

    @SuppressLint("InflateParams")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val rootview = inflater.inflate(R.layout.bottom_sheet_menu, null)

        val addBtn = BottomSheetMenuBinding.bind(rootview).bsheetAddMonth
        val selectMonth = BottomSheetMenuBinding.bind(rootview).bsheetSelectMonth

        addBtn.setOnClickListener {
            listener.pickDate()
            dismiss()
        }
        selectMonth.setOnClickListener {
            listener.navViewRemoveMonth()
            dismiss()
        }
        return rootview
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as BottomSheetDialogMenuInterface
        } catch (e: java.lang.ClassCastException) {
            throw java.lang.ClassCastException((context.toString() + "Must implement BottomSheetDialogInterface"))
        }
    }

    interface BottomSheetDialogMenuInterface {
        fun pickDate()
        fun navViewRemoveMonth()
    }
}