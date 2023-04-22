package com.example.dietapp2.fragments.usersettings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.example.dietapp2.R
import com.example.dietapp2.fragments.homeactivity.HomeActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers


class UserSettingsFragment : Fragment() {

    private lateinit var username_TV: TextView
    private lateinit var email_displayTV: TextView
    private lateinit var age_displayTV: TextView
    private lateinit var height_displayTV: TextView
    private lateinit var destination_displayTV: TextView
    private lateinit var profile_image: ImageView
    private lateinit var change_personal_info_btn: AppCompatButton
    private val userSettingsViewModel by viewModels<UserSettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.settings_fragment, container, false)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        username_TV = view.findViewById(R.id.username_TV)
        email_displayTV = view.findViewById(R.id.email_displayTV)
        age_displayTV = view.findViewById(R.id.age_displayTV)
        height_displayTV = view.findViewById(R.id.height_displayTV)
        destination_displayTV = view.findViewById(R.id.destination_displayTV)
        profile_image = view.findViewById(R.id.profile_image)
        change_personal_info_btn = view.findViewById(R.id.change_personal_info_btn)

        lifecycleScope.launch {
            try {

                userSettingsViewModel.readUserData {
                    CoroutineScope(Dispatchers.Main).launch {
                        val username = "Hello ${it.username.toString()}!"
                        username_TV.text = username
                        email_displayTV.text = it.email.toString()
                        val age = "${it.age.toString()} years old"
                        age_displayTV.text = age
                        val height = "${it.height.toString()} cm"
                        height_displayTV.text = height
                        destination_displayTV.text = it.destination.toString()
                    }
                }
                userSettingsViewModel.loadProfileImage(requireContext(), profile_image)

            } catch ( ex: Exception){
                ex.printStackTrace()
                Log.e("LoadingError", "Loading image, or user data loading failed")
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(requireContext(), HomeActivity::class.java)
                startActivity(intent)
            }
        })

        change_personal_info_btn.setOnClickListener {
            val intent = Intent(
                requireContext(),
                ChangePersonalInfoActivity::class.java)
            startActivity(intent)
        }


        return view
    }
}

