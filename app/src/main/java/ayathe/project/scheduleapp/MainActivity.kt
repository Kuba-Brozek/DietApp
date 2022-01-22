package ayathe.project.scheduleapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.fragment.app.Fragment
import ayathe.project.scheduleapp.databinding.ActivityMainBinding
import ayathe.project.scheduleapp.fragments.HomeFragment
import ayathe.project.scheduleapp.fragments.SecondFragment
import ayathe.project.scheduleapp.fragments.ThirdFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private val homeFragment = HomeFragment()
    private val secondFragment = SecondFragment()
    private val thirdFragment = ThirdFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fragmentsReplacement(homeFragment)


        bottom_nav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> fragmentsReplacement(homeFragment)
                R.id.home2 -> fragmentsReplacement(secondFragment)
                R.id.home3 -> fragmentsReplacement(thirdFragment)
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