package com.example.dietapp2.registerlogin.register

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.viewModels
import com.example.dietapp2.R
import com.example.dietapp2.registerlogin.activityreglog.LogRegActivity
import com.example.dietapp2.registerlogin.login.LoginFragment
import com.example.dietapp2.registerlogin.register.userDataInput.fragments.PrivacyPolicyFragment
import com.google.firebase.auth.FirebaseAuth


class RegisterFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private val registerVM by viewModels<ViewModelRegister>()
    val privacyPolicyFragment = PrivacyPolicyFragment()
    private lateinit var buttonRegister: AppCompatButton
    private lateinit var check_box_privacy_policy: CheckBox
    private lateinit var privacy_policy_TV: TextView
    private lateinit var emailET: EditText
    private lateinit var passwordET: EditText
    private lateinit var password_confirmET: EditText
    private lateinit var error_msg: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_register, container, false)
        buttonRegister = view.findViewById(R.id.buttonRegister)
        check_box_privacy_policy = view.findViewById(R.id.check_box_privacy_policy)
        privacy_policy_TV = view.findViewById(R.id.privacy_policy_TV)
        emailET = view.findViewById(R.id.emailET)
        passwordET = view.findViewById(R.id.passwordET)
        password_confirmET = view.findViewById(R.id.password_confirmET)
        error_msg = view.findViewById(R.id.error_msg)

        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        auth = FirebaseAuth.getInstance()
        buttonRegister.setOnClickListener {
            if (check_box_privacy_policy.isChecked) {
                onClickSignUp()
            } else {
                Toast.makeText(this@RegisterFragment.requireContext(), "You must accept privacy policy before signing up.", Toast.LENGTH_LONG).show()
            }
        }

        privacy_policy_TV.setOnClickListener{
            (activity as LogRegActivity).fragmentsReplacements(privacyPolicyFragment)
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val fragment = LoginFragment()
                (activity as LogRegActivity).fragmentsReplacements(fragment)
            }
        })

        return view

    }

    @SuppressLint("SetTextI18n")
    private fun onClickSignUp() {

        if (emailET.text.toString().isEmpty()) {
            error_msg.text = "Please enter email!"
            emailET.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailET.text.toString()).matches()) {
            error_msg.text = "Please enter VALID email!"
            emailET.requestFocus()
            return
        }
        if (passwordET.text.toString().isEmpty()) {
            error_msg.text = "Please enter password!"
            passwordET.requestFocus()
            return
        }
        if (password_confirmET.text.toString() != passwordET.text.toString()) {
            error_msg.text = "Passwords ain't the same!"
            password_confirmET.requestFocus()
            return
        }
        (activity as LogRegActivity)
            .userCreation(emailET.text.toString(),
                passwordET.text.toString())
    }

}
