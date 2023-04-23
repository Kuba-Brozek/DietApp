package com.example.dietapp2.registerlogin.afterregistration

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.dietapp2.R
import com.example.dietapp2.adapters.ViewPagerAdapter
import com.example.dietapp2.registerlogin.register.ViewModelRegister

class AfterRegistrationActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private val vm by viewModels<ViewModelRegister>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_after_registration)
        viewPager = findViewById(R.id.view_pager)

        val fragmentList: ArrayList<Fragment> = arrayListOf(
            IntroductionFragment(),
            UserInfoFragment()
        )
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)


        val adapter = ViewPagerAdapter(fragmentList, this)
        viewPager.adapter = adapter
    }

    override fun onBackPressed() {
        if (viewPager.currentItem == 0){
            finishAffinity()
            finish()
        } else {
            viewPager.currentItem = viewPager.currentItem -1
        }
    }

    fun swipeRight(){
        viewPager.currentItem = 1
    }
}