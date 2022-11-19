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
        dialog?.setTitle("Add Entry")
        val rootView = inflater.inflate(R.layout.add_data, null)

        val addBtn = AddDataBinding.bind(rootView).normaDialogAdd
        val cancelBtn = AddDataBinding.bind(rootView).normaDialogCancel

        val normaUre = AddDataBinding.bind(rootView).popupNormaUre
        val delUre = AddDataBinding.bind(rootView).popupDelUre

        var delMesto = ""


        addBtn.setOnClickListener {
            val delMestoRGroup = AddDataBinding.bind(rootView).delMesto.checkedRadioButtonId
            val item = NormaList("", 0.0, 0.0, "")
            if ((normaUre.text.isNotEmpty()) && (delUre.text.isNotEmpty())) {

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
/*    override fun onResume() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics =
                requireActivity().windowManager.currentWindowMetrics
            val insets: Insets = windowMetrics.windowInsets
                .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            val width = windowMetrics.bounds.width() - insets.left -
                    insets.right
            val height = windowMetrics.bounds.height() - insets.top -
                    insets.bottom
            val window = dialog!!.window
            if (window != null) {
                window.setLayout((width * 0.50).toInt(), (height *
                        0.70).toInt()) // for width and height to be 90 % of screen
                window.setGravity(Gravity.CENTER)
            }
            super.onResume()
        } else {
            val window = dialog!!.window
            val size = Point()
            // Store dimensions of the screen in `size`
            val display = window!!.windowManager.defaultDisplay
            display.getSize(size)
            // Set the width of the dialog proportional to 90% of the screen width
            window.setLayout((size.x * 0.60).toInt(),
                ViewGroup.LayoutParams.WRAP_CONTENT)
            window.setGravity(Gravity.CENTER)
            // Call super onResume after sizing
            super.onResume()
        }
    }*/

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