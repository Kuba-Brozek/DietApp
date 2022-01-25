package ayathe.project.scheduleapp.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ayathe.project.scheduleapp.R
import ayathe.project.scheduleapp.fragments.HomeFragment
import ayathe.project.scheduleapp.fragments.SecondFragment
import ayathe.project.scheduleapp.fragments.ThirdFragment
import ayathe.project.scheduleapp.fragments.register.RegisterFragment
import ayathe.project.scheduleapp.login.LoginFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val homeFragment = HomeFragment()
    private val secondFragment = SecondFragment()
    private val thirdFragment = ThirdFragment()
    private val loginFragment = LoginFragment()
    private val registerFragment = RegisterFragment()
    private lateinit var mainActivityVm: ViewModelMainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        mainActivityVm = ViewModelProvider(this)[ViewModelMainActivity::class.java]
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fragmentsReplacement(registerFragment)


        bottom_nav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> fragmentsReplacement(loginFragment)
                R.id.home2 -> fragmentsReplacement(registerFragment)
            }
            true
        }

    }

    private fun fragmentsReplacement(fragment: Fragment){
        val fragmentContainer = supportFragmentManager.beginTransaction()
        fragmentContainer.replace(R.id.container, fragment)
        fragmentContainer.commit()
    }





}