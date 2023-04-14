package com.example.dietapp2.registerlogin.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProvider
import com.example.dietapp2.R
import com.example.dietapp2.registerlogin.activityreglog.LogRegActivity
import com.example.dietapp2.registerlogin.register.RegisterFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginFragment : Fragment() {

    private lateinit var buttonLogin: AppCompatButton
    private lateinit var password_reset_TV: TextView
    private lateinit var emailETL: TextInputEditText
    private lateinit var register_fragment_changer: TextView
    private lateinit var passwordETL: TextInputEditText
    private lateinit var auth: FirebaseAuth
    private lateinit var loginVM: ViewModelLogin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        loginVM = ViewModelProvider(this)[ViewModelLogin::class.java]


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_login, container, false)
        buttonLogin = view.findViewById(R.id.buttonLogin)
        password_reset_TV = view.findViewById(R.id.password_reset_TV)
        emailETL = view.findViewById(R.id.emailETL)
        register_fragment_changer = view.findViewById(R.id.register_fragment_changer)
        passwordETL = view.findViewById(R.id.passwordETL)

        buttonLogin.setOnClickListener{
            onClickLogin()
        }
        password_reset_TV.setOnClickListener{
            if (emailETL.text.toString() != "") {
                MaterialAlertDialogBuilder(this@LoginFragment.requireContext()).setTitle("Email to reset password").setMessage("Are you sure you want to change your email to" +
                        " ${emailETL.text}?")
                    .setNegativeButton("No, I just got it again"){ _, _ -> }
                    .setPositiveButton("Send that email already!"){ _, _ ->
                        loginVM.sendEmail(emailETL.text.toString())
                    }.show()
            } else {
                Toast.makeText(this@LoginFragment.requireContext(), "Enter your email in email field before sending email verification", Toast.LENGTH_SHORT).show()
            }
        }

        register_fragment_changer.setOnClickListener{
            val fragment = RegisterFragment()
            (activity as LogRegActivity).fragmentsReplacements(fragment)
        }

        return view
    }

    private fun onClickLogin(){
            (activity as LogRegActivity).userSignIn(emailETL.text.toString(), passwordETL.text.toString())
    }


}