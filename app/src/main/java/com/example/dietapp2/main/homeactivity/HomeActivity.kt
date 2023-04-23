package com.example.dietapp2.main.homeactivity


import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.dietapp2.R
import com.example.dietapp2.main.food.FoodFragment
import com.example.dietapp2.main.usersettings.UserSettingsFragment
import com.example.dietapp2.main.sport.SportContainerFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class HomeActivity : AppCompatActivity() {
    private val homeVM by viewModels<HomeActivityViewModel>()
    private val sportContainerFragment = SportContainerFragment()
    private val foodFragment = FoodFragment()
    private val userSettings = UserSettingsFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        fragmentsReplacement(foodFragment)
        val bottom_nav_main = findViewById<BottomNavigationView>(R.id.bottom_nav_main)
        bottom_nav_main.setOnItemSelectedListener {
            when(it.itemId){
                R.id.one -> fragmentsReplacement(sportContainerFragment)
                R.id.two -> fragmentsReplacement(foodFragment)
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

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        finishAffinity()
        finish()
    }
}
