package ayathe.project.dietapp.fragments.usersettings

import android.R
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.InputType
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ayathe.project.dietapp.DTO.User
import ayathe.project.dietapp.databinding.ActivityChangePersonalInfoBinding
import ayathe.project.dietapp.fragments.homeactivity.HomeActivity
import ayathe.project.dietapp.fragments.usersummary.HomeFragment
import ayathe.project.dietapp.fragments.dayInfo.DayFragment
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_change_personal_info.*
import kotlinx.android.synthetic.main.activity_change_personal_info.view.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_introduction.view.*
import kotlinx.coroutines.*

class ChangePersonalInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangePersonalInfoBinding
    private val userSettingsVM by viewModels<UserSettingsViewModel>()
    private var auth = FirebaseAuth.getInstance()
    private val homeActivity = HomeActivity()
    private val homeFragment = HomeFragment()
    private val dayFragment = DayFragment()
    private val userSettings = UserSettingsFragment()
    private val options = listOf("Your goal", "Weight loss", "Keep weight", "Gain weight")
    private var goal = "Your Goal"
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
                    view.goal_spinner_settings.adapter =
                        ArrayAdapter(this@ChangePersonalInfoActivity, R.layout.simple_list_item_1, options)
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

        view.goal_spinner_settings.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                userSettingsVM.readUserData { user ->
                        parent!!.setSelection(options.indexOf(user.destination))
                }
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (parent!!.getItemAtPosition(position).toString() != "Your Goal"){
                    goal = parent.getItemAtPosition(position).toString()
                }
            }
        }

        age_ET_CPI.inputType = InputType.TYPE_CLASS_NUMBER
        starting_weight_ET_CPI.inputType = InputType.TYPE_CLASS_NUMBER
        height_ET_CPI.inputType = InputType.TYPE_CLASS_NUMBER

        save_changes_btn.setOnClickListener {
            if (goal_spinner_settings.selectedItem != "Your goal") {
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
                    user.destination = goal_spinner_settings.selectedItem.toString()



                    dataChangeConfirmation(this@ChangePersonalInfoActivity, user) {
                        username_TV_CPI.text = user.username
                        email_TV_CPI.text = user.email.toString()
                        age_TV_CPI.text = user.age.toString()
                        weight_TV_CPI.text = user.weight.toString()
                        height_TV_CPI.text = user.height.toString()
                        destination_TV_CPI.text = goal_spinner_settings.selectedItem.toString()
                        loading_changes.visibility = View.VISIBLE
                        Handler().postDelayed({ loading_changes.visibility = View.GONE }, 2000)
                    }
                }
            } else {
                Toast.makeText(this@ChangePersonalInfoActivity, "Choose your goal", Toast.LENGTH_LONG).show()
            }
        }

            profile_image_IV_CPI.setOnClickListener {
                loadImage.launch("image/*")
            }
    }
     private fun dataChangeConfirmation(context: Context, user: User, callback: (User) -> Unit) {
            MaterialAlertDialogBuilder(context).setTitle("Alert")
                .setMessage("Are you sure you want to change your personal data?")
                .setNegativeButton("I'll keep it that way") { _, _ ->  }
                .setPositiveButton("Change my data!") { _, _ ->
                    userSettingsVM.addUserToDatabase(user)
                    callback(user)
                }.show()
     }


    override fun onBackPressed() {
        val intent = Intent(this@ChangePersonalInfoActivity, HomeActivity::class.java)
        startActivity(intent)
    }
}