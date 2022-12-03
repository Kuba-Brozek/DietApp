package ayathe.project.dietapp.fragments.homeactivity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ayathe.project.dietapp.fragments.homefragment.HomeFragment
import ayathe.project.dietapp.fragments.secondfragment.DayFragment
import ayathe.project.dietapp.fragments.usersettings.UserSettingsFragment
import kotlinx.android.synthetic.main.activity_home.*




class HomeActivity : AppCompatActivity() {
    private val homeVM by viewModels<ViewModelHomeActivity>()
    private val homeFragment = HomeFragment()
    private val dayFragment = DayFragment()
    private val userSettings = UserSettingsFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(Rlayout.activity_home)
        fragmentsReplacement(homeFragment)
        bottom_nav_main.setOnItemSelectedListener {
            when(it.itemId){
                Rid.one -> fragmentsReplacement(homeFragment)
                Rid.two -> fragmentsReplacement(dayFragment)
                Rid.three -> fragmentsReplacement(userSettings)
            }
            true
        }

    }

    fun fragmentsReplacement(fragment: Fragment){
        val fragmentContainer = supportFragmentManager.beginTransaction()
        fragmentContainer.replace(Rid.container_main, fragment)
        fragmentContainer.commit()
    }

    override fun onBackPressed() {
        finishAffinity()
        finish()
    }


}

typealias Rid = ayathe.project.dietapp.R.id
typealias Rlayout = ayathe.project.dietapp.R.layout