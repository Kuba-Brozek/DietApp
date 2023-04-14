package com.example.dietapp2.fragments.homeactivity


import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.dietapp2.R
import com.example.dietapp2.fragments.dayInfo.DayFragment
import com.example.dietapp2.fragments.usersettings.UserSettingsFragment
import com.example.dietapp2.fragments.usersummary.UserSummaryFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class HomeActivity : AppCompatActivity() {
    private val homeVM by viewModels<ViewModelHomeActivity>()
    private val homeFragment = UserSummaryFragment()
    private val dayFragment = DayFragment()
    private val userSettings = UserSettingsFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        fragmentsReplacement(homeFragment)
        val bottom_nav_main = findViewById<BottomNavigationView>(R.id.bottom_nav_main)
        bottom_nav_main.setOnItemSelectedListener {
            when(it.itemId){
                R.id.one -> fragmentsReplacement(homeFragment)
                R.id.two -> fragmentsReplacement(dayFragment)
                R.id.three -> fragmentsReplacement(userSettings)
            }
            true
        }

    }

    fun fragmentsReplacement(fragment: Fragment){
        val fragmentContainer = supportFragmentManager.beginTransaction()
        fragmentContainer.replace(R.id.container_main, fragment)
        fragmentContainer.commit()
    }

    override fun onBackPressed() {
        finishAffinity()
        finish()
    }
}
