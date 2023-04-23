package com.example.dietapp2.registerlogin.activityreglog

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.dietapp2.DTO.User
import com.example.dietapp2.R
import com.example.dietapp2.main.homeactivity.HomeActivity
import com.example.dietapp2.registerlogin.login.LoginFragment
import com.example.dietapp2.registerlogin.afterregistration.AfterRegistrationActivity
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

class LogRegActivity : AppCompatActivity() {

    private val loginFragment = LoginFragment()
    private lateinit var logRegActivityViewModel: LogRegActivityViewModel
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        logRegActivityViewModel = ViewModelProvider(this)[LogRegActivityViewModel::class.java]
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_reg)
        fragmentsReplacement(loginFragment)
        auth = FirebaseAuth.getInstance()
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

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
                    logRegActivityViewModel.addUserToDatabase(user)
                    val intent = Intent(this, AfterRegistrationActivity::class.java)
                    startActivity(intent)
                } else {
                    Log.w(TAG, "signInWithEmail failed", task.exception)
                    Toast.makeText(baseContext, "Account exists",
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
                    Toast.makeText(baseContext, "Account do not exist.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun fragmentsReplacements(fragment: Fragment){
        val fragmentContainer = supportFragmentManager.beginTransaction()
        fragmentContainer.replace(R.id.container_reg_log, fragment)
        fragmentContainer.commit()
    }

}