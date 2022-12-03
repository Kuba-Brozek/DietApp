package ayathe.project.dietapp.registerlogin.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import ayathe.project.dietapp.R
import ayathe.project.dietapp.registerlogin.activityreglog.LogRegActivity
import ayathe.project.dietapp.registerlogin.register.RegisterFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.view.*


class LoginFragment : Fragment() {

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
        view.buttonLogin.setOnClickListener{
            onClickLogin()
        }
        view.password_reset_TV.setOnClickListener{
            if (view.emailETL.text.toString() != "") {
                MaterialAlertDialogBuilder(this@LoginFragment.requireContext()).setTitle("Email to reset password").setMessage("Are you sure you want to change your email to" +
                        " ${view.emailETL.text}?")
                    .setNegativeButton("No, I just got it again"){ _, _ -> }
                    .setPositiveButton("Send that email already!"){ _, _ ->
                        loginVM.sendEmail(view.emailETL.text.toString())
                    }.show()
            } else {
                Toast.makeText(this@LoginFragment.requireContext(), "Enter your email in email field before sending email verification", Toast.LENGTH_SHORT).show()
            }
        }

        view.register_fragment_changer.setOnClickListener{
            val fragment = RegisterFragment()
            (activity as LogRegActivity).fragmentsReplacements(fragment)
        }

        return view
    }

    private fun onClickLogin(){
            (activity as LogRegActivity).userSignIn(emailETL.text.toString(), passwordETL.text.toString())
    }


}