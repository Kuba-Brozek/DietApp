package ayathe.project.dietapp.registerlogin.register.userDataInput

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import ayathe.project.dietapp.R
import ayathe.project.dietapp.adapters.ViewPagerAdapter
import ayathe.project.dietapp.registerlogin.register.ViewModelRegister
import ayathe.project.dietapp.registerlogin.register.userDataInput.fragments.IntroductionFragment
import ayathe.project.dietapp.registerlogin.register.userDataInput.fragments.UserInfoFragment

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
}