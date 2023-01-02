package roskar.kristjan.norma.dialogFragments

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Insets
import android.graphics.Point
import android.hardware.display.DisplayManager
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import roskar.kristjan.norma.R
import roskar.kristjan.norma.databinding.AddDataBinding
import roskar.kristjan.norma.model.NormaList
import kotlin.properties.Delegates

class NormaListAddDialog(val position: Int) : BottomSheetDialogFragment() {
    private var normaUreValue by Delegates.notNull<Double>()
    private var delUreValue by Delegates.notNull<Double>()
    private lateinit var listener: NormaListAddInterface

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val rootView = inflater.inflate(R.layout.add_data, null)

        val addBtn = AddDataBinding.bind(rootView).normaDialogAdd
        val cancelBtn = AddDataBinding.bind(rootView).normaDialogCancel

        val normaUre = AddDataBinding.bind(rootView).popupNormaUre
        val delUre = AddDataBinding.bind(rootView).popupDelUre

        var delMesto = ""


        addBtn.setOnClickListener {
            val delMestoRGroup = AddDataBinding.bind(rootView).delMesto.checkedRadioButtonId
            val item = NormaList("", 0.0, 0.0, "")
            if ((normaUre.text?.isNotEmpty() == true) && (delUre.text?.isNotEmpty() == true)) {

                item.normaHours = normaUre.text.toString().toDoubleOrNull()!!
                item.workingHours = delUre.text.toString().toDoubleOrNull()!!

                when (delMestoRGroup) {
                    R.id.hala -> {
                        item.workplace = "Hala"
                    }
                    R.id.linija -> {
                        item.workplace = "Linija"
                    }
                }
                listener.normaListUpdateValue(item, position)
                dismiss()

            } else {
                normaUre.setHint("Vnesi Številko")
            }
        }

        cancelBtn.setOnClickListener {
            dismiss()
        }
        return rootView
    }

    /**
     * STACKOVERFLOW FTW
     */

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as NormaListAddInterface
        } catch (e: java.lang.ClassCastException) {
            throw java.lang.ClassCastException((context.toString() + "Must implement NormaListAddInterface"))
        }
    }

    interface NormaListAddInterface {
        fun normaListUpdateValue(item: NormaList, position: Int)
    }
}
//normaUre: Double, delUre: Double, delMesto: String, position: Int