package roskar.kristjan.norma

import android.content.Context
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.PopupWindow
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams
import androidx.recyclerview.widget.RecyclerView
import roskar.kristjan.norma.databinding.AddDataBinding

object AdapterOnClick {

    var clickedPosition = 82
    fun onClickOperation(
        adapter: Adapter,
        context: Context,
        recyclerView: RecyclerView,
        rootLayout: ConstraintLayout
    ) {
        adapter.setOnItemClickListener(object : Adapter.onItemClickListener {
            override fun onItemClick(position: Int) {

                if (clickedPosition != position) {
                    val holderNewPosition =
                        recyclerView.findViewHolderForAdapterPosition(clickedPosition)
                    holderNewPosition?.let {
                        val listItemAdd = (holderNewPosition as Adapter.MyViewHolder).listItemAdd
                        val listItemRemove = holderNewPosition.listItemRemove
                        listItemRemove.visibility = View.INVISIBLE
                        listItemAdd.visibility = View.INVISIBLE
                    }

                    clickedPosition = position

                }
            }

            override fun onAddButtonClicked(position: Int) {

                val listener = object : RecyclerView.SimpleOnItemTouchListener() {
                    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                        return true
                    }
                }

                recyclerView.addOnItemTouchListener(listener)
                val inflater: LayoutInflater = LayoutInflater.from(context)
                val view = inflater.inflate(R.layout.add_data, null)

                val buttonDismiss = AddDataBinding.bind(view).popupDismiss
                val buttonAdd = AddDataBinding.bind(view).popupAdd
                val popupWindow = PopupWindow(
                    view,
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT
                )
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    popupWindow.isTouchModal = false
                }
                popupWindow.isOutsideTouchable = false
                popupWindow.isFocusable = true
                popupWindow.showAtLocation(
                    rootLayout,
                    Gravity.CENTER,
                    0,
                    0
                )
                popupWindow.setOnDismissListener {
                    recyclerView.removeOnItemTouchListener(listener)
                }
                buttonDismiss.setOnClickListener {
                    popupWindow.dismiss()
                }
                buttonAdd.setOnClickListener {

                }

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