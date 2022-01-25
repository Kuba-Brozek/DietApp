package ayathe.project.scheduleapp.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ayathe.project.scheduleapp.R
import ayathe.project.scheduleapp.databinding.ActivityMainBinding
import ayathe.project.scheduleapp.fragments.HomeFragment
import ayathe.project.scheduleapp.fragments.SecondFragment
import ayathe.project.scheduleapp.fragments.ThirdFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val homeFragment = HomeFragment()
    private val secondFragment = SecondFragment()
    private val thirdFragment = ThirdFragment()

    private val mainActivityVm by viewModels<ViewModelMainActivity>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        var binding: ViewDataBinding? = DataBindingUtil.setContentView(this, R.layout.activity_main)
        fragmentsReplacement(homeFragment)

        return inflater.inflate(R.layout.activity_main, container, false)

    }

    private fun fragmentsReplacement(fragment: Fragment){
        val fragmentContainer = supportFragmentManager.beginTransaction()
        fragmentContainer.replace(R.id.container, fragment)
        fragmentContainer.commit()
    }





}