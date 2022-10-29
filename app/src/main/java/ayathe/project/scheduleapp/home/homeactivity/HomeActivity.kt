package ayathe.project.scheduleapp.home.homeactivity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ayathe.project.scheduleapp.R
import ayathe.project.scheduleapp.home.homefragment.HomeFragment
import ayathe.project.scheduleapp.home.secondfragment.PreviousDaysFragment
import ayathe.project.scheduleapp.home.thirdfragment.UserSettingsFragment
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    private val homeVM by viewModels<ViewModelHomeActivity>()
    private val homeFragment = HomeFragment()
    private val REQUEST_IMAGE_CAPTURE = 17
    private val secondFragment = PreviousDaysFragment()
    private val thirdFragment = UserSettingsFragment()
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
            val uri = data?.data!!.toString()
            val bundle = Bundle()
            val thirdFragment = UserSettingsFragment()
            bundle.putString("ImageUri",uri)
            thirdFragment.arguments = bundle
            fragmentsReplacement(thirdFragment)
        }
    }


}