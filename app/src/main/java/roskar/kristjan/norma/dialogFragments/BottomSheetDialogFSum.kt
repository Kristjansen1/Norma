package roskar.kristjan.norma.dialogFragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import roskar.kristjan.norma.MainActivity
import roskar.kristjan.norma.R
import roskar.kristjan.norma.databinding.BottomSheetSumBinding
import roskar.kristjan.norma.model.NormaList

class BottomSheetDialogFSum(val norma: ArrayList<NormaList>) : BottomSheetDialogFragment() {
    @SuppressLint("InflateParams")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val rootview = inflater.inflate(R.layout.bottom_sheet_sum, null)
        val normaSumTxtView = BottomSheetSumBinding.bind(rootview).normaSum
        val whSumTxtView = BottomSheetSumBinding.bind(rootview).whsum

        var sumNh = 0.0
        var sumWh = 0.0
        for (x in norma) {
            sumNh += x.normaHours
            sumWh += x.workingHours
        }

        normaSumTxtView.text = sumNh.toString()
        whSumTxtView.text = sumWh.toString()
        return rootview
    }
}