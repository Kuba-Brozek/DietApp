package ayathe.project.scheduleapp.registerlogin.register.userDataInput

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import ayathe.project.scheduleapp.R
import ayathe.project.scheduleapp.adapter.ViewPagerAdapter
import ayathe.project.scheduleapp.registerlogin.register.userDataInput.fragments.IntroductionFragment
import ayathe.project.scheduleapp.registerlogin.register.userDataInput.fragments.UserInfoFragment
import kotlinx.android.synthetic.main.activity_after_registration.*

class AfterRegistrationActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
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