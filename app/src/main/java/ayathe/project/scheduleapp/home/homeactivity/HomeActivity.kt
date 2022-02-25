package ayathe.project.scheduleapp.home.homeactivity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import ayathe.project.scheduleapp.R
import ayathe.project.scheduleapp.home.homefragment.HomeFragment
import ayathe.project.scheduleapp.home.secondfragment.SecondFragment
import ayathe.project.scheduleapp.home.secondfragment.eventinfo.EventInfo
import ayathe.project.scheduleapp.home.thirdfragment.ThirdFragment
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_third.view.*

class HomeActivity : AppCompatActivity() {
    private val homeVM by viewModels<ViewModelHomeActivity>()
    private val homeFragment = HomeFragment()
    private val REQUEST_IMAGE_CAPTURE = 17
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

    fun fragmentsReplacement(fragment: Fragment){
        val fragmentContainer = supportFragmentManager.beginTransaction()
        fragmentContainer.replace(R.id.container_main, fragment)
        fragmentContainer.commit()
    }

    fun openGallery() {

        val intent = Intent("android.intent.action.GET_CONTENT")
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            var uri = data?.data!!.toString()
            val bundle = Bundle()
            val thirdFragment = ThirdFragment()
            bundle.putString("ImageUri",uri)
            thirdFragment.arguments = bundle
            fragmentsReplacement(thirdFragment)
        }
    }

}