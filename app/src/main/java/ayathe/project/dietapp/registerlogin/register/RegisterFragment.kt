package ayathe.project.dietapp.registerlogin.register

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import ayathe.project.dietapp.R
import ayathe.project.dietapp.fragments.homeactivity.HomeActivity
import ayathe.project.dietapp.registerlogin.activityreglog.LogRegActivity
import ayathe.project.dietapp.registerlogin.login.LoginFragment
import ayathe.project.dietapp.registerlogin.register.userDataInput.fragments.PrivacyPolicyFragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.fragment_register.view.*


class RegisterFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private val registerVM by viewModels<ViewModelRegister>()
    val privacyPolicyFragment = PrivacyPolicyFragment()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_register, container, false)

        auth = FirebaseAuth.getInstance()
        view.buttonRegister.setOnClickListener {
            if (view.check_box_privacy_policy.isChecked) {
                onClickSignUp()
            } else {
                Toast.makeText(this@RegisterFragment.requireContext(), "You must accept privacy policy before signing up.", Toast.LENGTH_LONG).show()
            }
        }

        view.privacy_policy_TV.setOnClickListener{
            (activity as LogRegActivity).fragmentsReplacements(privacyPolicyFragment)
        }

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
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
