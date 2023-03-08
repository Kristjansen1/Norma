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
        val preProcTxtView = BottomSheetSumBinding.bind(rootview).presegProcent
        val presUreTxtView = BottomSheetSumBinding.bind(rootview).presegUre

        var sumNh = 0.0
        var sumWh = 0.0
        var nHtotal = 0
        for (x in norma) {
            if (x.normaHours > 0.0) {
                sumNh += x.normaHours
                sumWh += x.workingHours
                nHtotal++
            }

        }
        //val nhMax = nHtotal * 7.3
        val nhMax = sumWh * 0.9125
        val diff = sumNh - nhMax
        val r = String.format("%.2f", sumNh) + " / $nhMax"
        normaSumTxtView.text = r
        whSumTxtView.text = sumWh.toString()
        presUreTxtView.text = String.format("%.2f", (diff)) + " h"

        val procenti = (diff / nhMax) * 100


        preProcTxtView.text = String.format("%.2f", procenti) + " %"


        return rootview
    }
}