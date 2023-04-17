package com.example.dietapp2.registerlogin.register.userDataInput.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.example.dietapp2.R
import com.example.dietapp2.registerlogin.activityreglog.LogRegActivity
import com.example.dietapp2.registerlogin.register.RegisterFragment


class PrivacyPolicyFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.privacy_policy_fragment, container, false)

        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.freeprivacypolicy.com/live/15d63160-ff3b-4e45-b2d9-0e6da8129cf3"))
        startActivity(browserIntent)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val fragment = RegisterFragment()
                (activity as LogRegActivity).fragmentsReplacements(fragment)
            }
        })

        return view
    }
}