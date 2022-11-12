package ayathe.project.dietapp.home.homeactivity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ayathe.project.dietapp.R
import ayathe.project.dietapp.home.homefragment.HomeFragment
import ayathe.project.dietapp.home.secondfragment.DayFragment
import ayathe.project.dietapp.home.usersettings.UserSettings
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    private val homeVM by viewModels<ViewModelHomeActivity>()
    private val homeFragment = HomeFragment()
    private val REQUEST_IMAGE_CAPTURE = 17
    private val secondFragment = DayFragment()
    private val thirdFragment = UserSettings()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        fragmentsReplacement(homeFragment)
        val intent = Intent(this, UserSettings::class.java)
        bottom_nav_main.setOnItemSelectedListener {
            when(it.itemId){
                R.id.one -> fragmentsReplacement(homeFragment)
                R.id.two -> fragmentsReplacement(secondFragment)
                R.id.three -> startActivity(intent)
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