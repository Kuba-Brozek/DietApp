package com.example.dietapp2.registerlogin.register.userDataInput.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.example.dietapp2.R
import com.example.dietapp2.registerlogin.activityreglog.LogRegActivity
import com.example.dietapp2.registerlogin.register.RegisterFragment


class PrivacyPolicyFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.privacy_policy_fragment, container, false)



        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val fragment = RegisterFragment()
                (activity as LogRegActivity).fragmentsReplacements(fragment)
            }
        })

        return view
    }
}