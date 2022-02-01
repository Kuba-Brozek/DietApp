package ayathe.project.scheduleapp.home.homeactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import ayathe.project.scheduleapp.R
import ayathe.project.scheduleapp.home.homefragment.HomeFragment
import ayathe.project.scheduleapp.home.secondfragment.SecondFragment
import ayathe.project.scheduleapp.home.thirdfragment.ThirdFragment
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_main.*

class HomeActivity : AppCompatActivity() {
    private val homeVM by viewModels<ViewModelHomeActivity>()
    private val homeFragment = HomeFragment()
    private val secondFragment = SecondFragment()
    private val thirdFragment = ThirdFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        fragmentsReplacement(homeFragment)
        bottom_nav_main.setOnItemSelectedListener {
            when(it.itemId){
                R.id.one -> fragmentsReplacement(homeFragment)
                R.id.two -> fragmentsReplacement(secondFragment)
                R.id.three -> fragmentsReplacement(thirdFragment)
            }
            true
        }
    }

    private fun fragmentsReplacement(fragment: Fragment){
        val fragmentContainer = supportFragmentManager.beginTransaction()
        fragmentContainer.replace(R.id.container_main, fragment)
        fragmentContainer.commit()
    }
}