package ayathe.project.scheduleapp.registerlogin.activityreglog

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ayathe.project.scheduleapp.R
import ayathe.project.scheduleapp.home.homeactivity.HomeActivity
import ayathe.project.scheduleapp.registerlogin.login.LoginFragment
import ayathe.project.scheduleapp.registerlogin.register.RegisterFragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

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

    fun userCreation(email: String, password: String){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
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