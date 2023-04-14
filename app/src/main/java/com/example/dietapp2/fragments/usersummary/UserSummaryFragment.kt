package com.example.dietapp2.fragments.usersummary

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.example.dietapp2.R


class UserSummaryFragment : Fragment() {

    private lateinit var starting_day_TV: TextView
    private lateinit var s_weight_TV: TextView
    private val summaryVM by viewModels<UserSummaryViewModel>()


    @SuppressLint("Recycle")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view: View = inflater.inflate(R.layout.fragment_home, container, false)
        starting_day_TV = view.findViewById(R.id.starting_day_TV)
        s_weight_TV = view.findViewById(R.id.s_weight_TV)

        summaryVM.readUserData {
            starting_day_TV.text = it.startingDate.toString()
            s_weight_TV.text = it.weight.toString()
        }

        return view
    }
}