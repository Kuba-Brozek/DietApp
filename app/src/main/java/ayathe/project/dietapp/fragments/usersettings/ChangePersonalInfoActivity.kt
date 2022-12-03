package ayathe.project.dietapp.fragments.usersettings

import android.os.Bundle
import android.os.Handler
import android.text.InputType
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ayathe.project.dietapp.databinding.ActivityChangePersonalInfoBinding
import ayathe.project.dietapp.fragments.homeactivity.HomeActivity
import ayathe.project.dietapp.fragments.homefragment.HomeFragment
import ayathe.project.dietapp.fragments.secondfragment.DayFragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_change_personal_info.*
import kotlinx.android.synthetic.main.activity_change_personal_info.bottom_nav_main
import kotlinx.android.synthetic.main.activity_home.*

class ChangePersonalInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangePersonalInfoBinding
    private val userSettingsVM by viewModels<UserSettingsViewModel>()
    private var auth = FirebaseAuth.getInstance()
    private val homeActivity = HomeActivity()
    private val homeFragment = HomeFragment()
    private val dayFragment = DayFragment()
    private val userSettings = UserSettingsFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            ActivityChangePersonalInfoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)



            try {
                userSettingsVM.readUserData {
                    username_TV_CPI.text = it.username.toString()
                    email_TV_CPI.text = it.email.toString()
                    age_TV_CPI.text = it.age.toString()
                    weight_TV_CPI.text = it.weight.toString()
                    height_TV_CPI.text = it.height.toString()
                    destination_TV_CPI.text = it.destination.toString()
                }

                userSettingsVM.loadProfileImage(
                    this@ChangePersonalInfoActivity, profile_image_IV_CPI)

            } catch (ex: Exception) {

            }

            val loadImage = registerForActivityResult(
                ActivityResultContracts
                    .GetContent()) {
                profile_image_IV_CPI.setImageURI(it)
                Glide.with(this@ChangePersonalInfoActivity)
                    .load(it).circleCrop().into(profile_image_IV_CPI)
                val a = it
                profile_image_BTN_CPI.setOnClickListener {
                    loading_changes.visibility = View.VISIBLE

                        userSettingsVM.uploadProfileImage(a)
                        Handler().postDelayed({
                            loading_changes.visibility = View.GONE
                        }, 2000)
                }
            }
        age_ET_CPI.inputType = InputType.TYPE_CLASS_NUMBER
        starting_weight_ET_CPI.inputType = InputType.TYPE_CLASS_NUMBER
        height_ET_CPI.inputType = InputType.TYPE_CLASS_NUMBER

        save_changes_btn.setOnClickListener {
            userSettingsVM.readUserData {
                val user = it
                if (username_ET_CPI.text.toString() != "") {
                    user.username = username_ET_CPI.text.toString()
                }
                if (email_ET_CPI.text.toString() != "") {
                    user.email = email_ET_CPI.text.toString()
                }
                if (age_ET_CPI.text.toString() != "") {
                    user.age = age_ET_CPI.text.toString().toInt()
                }
                if (starting_weight_ET_CPI.text.toString() != "") {
                    user.weight = starting_weight_ET_CPI.text.toString().toDouble()
                }
                if (height_ET_CPI.text.toString() != "") {
                    user.height = height_ET_CPI.text.toString().toInt()
                }
                if (destination_ET_CPI.text.toString() != "") {
                    user.destination = starting_weight_ET_CPI.text.toString()
                }

                userSettingsVM.dataChangeConfirmation(this@ChangePersonalInfoActivity, user)
            }
        }

        bottom_nav_main.setOnItemSelectedListener {
            when(it.itemId){
                Rid.one -> homeActivity.fragmentsReplacement(homeFragment)
                Rid.two -> homeActivity.fragmentsReplacement(dayFragment)
                Rid.three -> homeActivity.fragmentsReplacement(userSettings)
            }
            true
        }

            profile_image_IV_CPI.setOnClickListener {
                loadImage.launch("image/*")
            }
    }
}


typealias Rid = ayathe.project.dietapp.R.id