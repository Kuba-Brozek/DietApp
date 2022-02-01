package ayathe.project.scheduleapp.registerlogin.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import ayathe.project.scheduleapp.R
import ayathe.project.scheduleapp.registerlogin.activityreglog.LogRegActivity
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
        return view
    }

    override fun onStart() {
        super.onStart()
        val currentUser: FirebaseUser? = auth.currentUser
        loginVM.updateUI(currentUser)
    }

    private fun onClickLogin(){
            (activity as LogRegActivity).userSignIn(emailETL.text.toString(), passwordETL.text.toString())
    }
}