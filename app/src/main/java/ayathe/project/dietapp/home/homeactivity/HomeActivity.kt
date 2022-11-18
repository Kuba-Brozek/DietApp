package ayathe.project.dietapp.home.homeactivity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ayathe.project.dietapp.R
import ayathe.project.dietapp.home.homefragment.HomeFragment
import ayathe.project.dietapp.home.secondfragment.DayFragment
import ayathe.project.dietapp.home.usersettings.UserSettingsFragment
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    private val homeVM by viewModels<ViewModelHomeActivity>()
    private val homeFragment = HomeFragment()
    private val REQUEST_IMAGE_CAPTURE = 17
    private val dayFragment = DayFragment()
    private val userSettings = UserSettingsFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        fragmentsReplacement(homeFragment)
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