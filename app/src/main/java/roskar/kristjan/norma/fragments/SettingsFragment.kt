package roskar.kristjan.norma.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.example.myapplication.R
import roskar.kristjan.norma.MainActivity


class SettingsFragment() : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = (context as MainActivity).findViewById<LinearLayout>(R.id.linearLayout)
        // Inflate the layout for this fragment

        view.visibility = View.GONE
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        val view = (context as MainActivity).findViewById<LinearLayout>(R.id.linearLayout)
        view.visibility = View.VISIBLE

    }
}