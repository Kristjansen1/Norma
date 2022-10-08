package roskar.kristjan.norma.dialogFragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import roskar.kristjan.norma.R
import roskar.kristjan.norma.databinding.AddDataBinding
import kotlin.properties.Delegates

class NormaListAddDialog(val position: Int) : DialogFragment() {
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

            if ((normaUre.text.isNotEmpty()) && (delUre.text.isNotEmpty())) {

                normaUreValue = normaUre.text.toString().toDoubleOrNull()!!
                delUreValue = delUre.text.toString().toDoubleOrNull()!!

                when (delMestoRGroup) {
                    R.id.hala -> {
                        delMesto = "Hala"
                    }
                    R.id.linija -> {
                        delMesto = "Linija"
                    }
                }

                listener.values(normaUreValue, delUreValue, delMesto, position)
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as NormaListAddInterface
        } catch (e: java.lang.ClassCastException) {
            throw java.lang.ClassCastException((context.toString() + "Must implement NormaListAddInterface"))
        }
    }

    interface NormaListAddInterface {
        fun values(normaUre: Double, delUre: Double, delMesto: String, position: Int)
    }
}