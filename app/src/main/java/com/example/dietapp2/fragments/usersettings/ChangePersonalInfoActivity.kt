package com.example.dietapp2.fragments.usersettings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.InputType
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.bumptech.glide.Glide
import com.example.dietapp2.DTO.User
import com.example.dietapp2.R
import com.example.dietapp2.fragments.dayInfo.DayFragment
import com.example.dietapp2.fragments.homeactivity.HomeActivity
import com.example.dietapp2.fragments.usersummary.UserSummaryFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.*

class ChangePersonalInfoActivity : AppCompatActivity() {

    private lateinit var username_TV_CPI: TextView
    private lateinit var email_TV_CPI: TextView
    private lateinit var age_TV_CPI: TextView
    private lateinit var weight_TV_CPI: TextView
    private lateinit var height_TV_CPI: TextView
    private lateinit var destination_TV_CPI: TextView
    private lateinit var goal_spinner_settings: Spinner
    private lateinit var profile_image_IV_CPI: ImageView
    private lateinit var profile_image_BTN_CPI: AppCompatButton
    private lateinit var loading_changes: ProgressBar
    private lateinit var age_ET_CPI: EditText
    private lateinit var height_ET_CPI: EditText
    private lateinit var starting_weight_ET_CPI: EditText
    private lateinit var save_changes_btn: AppCompatButton
    private lateinit var username_ET_CPI: EditText
    private lateinit var email_ET_CPI: EditText
    private val userSettingsVM by viewModels<UserSettingsViewModel>()
    private val options = listOf("Your goal", "Weight loss", "Keep weight", "Gain weight")
    private var goal = "Your Goal"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_personal_info)

        username_TV_CPI = findViewById(R.id.username_TV_CPI)
        email_TV_CPI = findViewById(R.id.email_TV_CPI)
        age_TV_CPI = findViewById(R.id.age_TV_CPI)
        weight_TV_CPI = findViewById(R.id.weight_TV_CPI)
        height_TV_CPI = findViewById(R.id.height_TV_CPI)
        destination_TV_CPI = findViewById(R.id.destination_TV_CPI)
        goal_spinner_settings = findViewById(R.id.goal_spinner_settings)
        profile_image_IV_CPI = findViewById(R.id.profile_image_IV_CPI)
        profile_image_BTN_CPI = findViewById(R.id.profile_image_BTN_CPI)
        loading_changes = findViewById(R.id.loading_changes)
        age_ET_CPI = findViewById(R.id.age_ET_CPI)
        starting_weight_ET_CPI = findViewById(R.id.starting_weight_ET_CPI)
        height_ET_CPI = findViewById(R.id.height_ET_CPI)
        save_changes_btn = findViewById(R.id.save_changes_btn)
        username_ET_CPI = findViewById(R.id.username_ET_CPI)
        email_ET_CPI = findViewById(R.id.email_ET_CPI)

        try {
            userSettingsVM.readUserData {
                username_TV_CPI.text = it.username.toString()
                email_TV_CPI.text = it.email.toString()
                age_TV_CPI.text = it.age.toString()
                weight_TV_CPI.text = it.weight.toString()
                height_TV_CPI.text = it.height.toString()
                destination_TV_CPI.text = it.destination.toString()
                goal_spinner_settings.adapter =
                    ArrayAdapter(this, android.R.layout.simple_list_item_1, options)
            }

            userSettingsVM.loadProfileImage(
                this, profile_image_IV_CPI
            )

        } catch (ex: Exception) {

        }

        val loadImage = registerForActivityResult(
            ActivityResultContracts
                .GetContent()
        ) {
            profile_image_IV_CPI.setImageURI(it)
            Glide.with(this)
                .load(it)
                .circleCrop()
                .into(profile_image_IV_CPI)
            val a = it
            profile_image_BTN_CPI.setOnClickListener {
                loading_changes.visibility = View.VISIBLE

                if (a != null) {
                    userSettingsVM.uploadProfileImage(a)
                }
                Handler().postDelayed({
                    loading_changes.visibility = View.GONE
                }, 2000)
            }
        }

        goal_spinner_settings.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                userSettingsVM.readUserData { user ->
                    parent!!.setSelection(options.indexOf(user.destination))
                }
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (parent!!.getItemAtPosition(position).toString() != "Your Goal") {
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
                Toast.makeText(
                    this@ChangePersonalInfoActivity,
                    "Choose your goal",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        profile_image_IV_CPI.setOnClickListener {
            loadImage.launch("image/*")
        }
    }

    private fun dataChangeConfirmation(context: Context, user: User, callback: (User) -> Unit) {
        MaterialAlertDialogBuilder(context).setTitle("Alert")
            .setMessage("Are you sure you want to change your personal data?")
            .setNegativeButton("I'll keep it that way") { _, _ -> }
            .setPositiveButton("Change my data!") { _, _ ->
                userSettingsVM.addUserToDatabase(user)
                callback(user)
            }.show()
    }


    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val intent = Intent(this@ChangePersonalInfoActivity, HomeActivity::class.java)
        startActivity(intent)
    }
}