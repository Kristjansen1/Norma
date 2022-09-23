package roskar.kristjan.norma

import android.app.ActionBar
import android.content.Context
import android.transition.TransitionManager
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.NonDisposableHandle.parent
import roskar.kristjan.norma.databinding.ActivityMainBinding
import roskar.kristjan.norma.databinding.AddDataBinding

object AdapterOnClick {

    var clickedPosition = 82
    fun onClickOperation(adapter: Adapter,
                         context: Context,
                         recyclerView: RecyclerView,
                         rootLayout: ConstraintLayout
    ) {

        adapter.setOnItemClickListener(object : Adapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                if (clickedPosition != position) {
                    val holder = recyclerView.findViewHolderForAdapterPosition(clickedPosition)
                    holder?.let {
                        val listItemAdd = (holder as Adapter.MyViewHolder).listItemAdd
                        val listItemRemove = holder.listItemRemove
                        listItemRemove.visibility = View.INVISIBLE
                        listItemAdd.visibility = View.INVISIBLE
                    }
                    clickedPosition = position
                }
            }
            override fun onAddButtonClicked(position: Int) {
                val inflater : LayoutInflater = LayoutInflater.from(context)
                val view = inflater.inflate(R.layout.add_data,null)

                val popupWindow = PopupWindow(
                    view,
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT
                )
                TransitionManager.beginDelayedTransition(rootLayout)
                popupWindow.isFocusable = true
                popupWindow.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING
                popupWindow.showAtLocation(
                    rootLayout,
                    Gravity.CENTER,
                    0,
                    0
                )
            }

            override fun onRemoveButtonClicked(position: Int) {
                Toast.makeText(
                    context,
                    "You Clicked remove $position",
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
    }
}