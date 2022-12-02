package ayathe.project.dietapp.registerlogin.activityreglog

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ayathe.project.dietapp.R
import ayathe.project.dietapp.DTO.User
import ayathe.project.dietapp.fragments.homeactivity.HomeActivity
import ayathe.project.dietapp.registerlogin.login.LoginFragment
import ayathe.project.dietapp.registerlogin.register.RegisterFragment
import ayathe.project.dietapp.registerlogin.register.userDataInput.AfterRegistrationActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class LogRegActivity : AppCompatActivity() {

    private val loginFragment = LoginFragment()
    private val registerFragment = RegisterFragment()
    private lateinit var mainActivityVm: ViewModelMainActivity
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        mainActivityVm = ViewModelProvider(this)[ViewModelMainActivity::class.java]
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fragmentsReplacement(loginFragment)
        auth = FirebaseAuth.getInstance()
        bottom_nav_reg_log.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> fragmentsReplacement(loginFragment)
                R.id.home2 -> fragmentsReplacement(registerFragment)
            }
            true
        }
    }

    private fun fragmentsReplacement(fragment: Fragment){
        val fragmentContainer = supportFragmentManager.beginTransaction()
        fragmentContainer.replace(R.id.container_reg_log, fragment)
        fragmentContainer.commit()
    }

    @SuppressLint("SimpleDateFormat")
    fun userCreation(email: String, password: String){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->

                if (task.isSuccessful) {
                    val sdf = SimpleDateFormat("dd.MM.yyyy")
                    val currentDate = sdf.format(Date())
                    Log.d(TAG, "User created successfully")
                    val user = User(auth.currentUser!!.uid, email, email, "",0 , 0.0, 160, "", currentDate)
                    mainActivityVm.addUserToDatabase(user)
                    val intent = Intent(this, AfterRegistrationActivity::class.java)
                    startActivity(intent)

                } else {

                    Log.w(TAG, "signInWithEmail failed", task.exception)
                    Toast.makeText(baseContext, "Authentication failed. $password, $password",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun userSignIn(email: String, password: String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }







}