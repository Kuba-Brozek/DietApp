package ayathe.project.scheduleapp.home.usersettings

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import ayathe.project.scheduleapp.R
import ayathe.project.scheduleapp.databinding.ActivitySettingsBinding
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class UserSettings : AppCompatActivity() {

    private val userSettingsVM by viewModels<UserSettingsViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivitySettingsBinding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lifecycleScope.launch {
            userSettingsVM.loadProfileImage(this@UserSettings, profile_image)
            hide()
            email_displayTV.text = userSettingsVM.showUserInfo()
            userSettingsVM.loadProfileImage(this@UserSettings, profile_image)
            btn_change_password_visibility.setOnClickListener {
                showPasswordChange()
            }
            btn_change_email_visibility.setOnClickListener {
                showEmailChange()
                userSettingsVM.showUserInfo()
            }
            btn_hide.setOnClickListener {
                hide()
            }

            btn_change_passwd.setOnClickListener {
                if (newpassET.text.toString() == newpassConET.text.toString()
                    && newpassET.text.toString() != "" && newpassET.text.toString().length > 5) {
                    userSettingsVM.passwordChangeConfirmation(this@UserSettings,
                        newpassET.text.toString())
                    hide()
                } else if (newpassET.text.toString() == newpassConET.text.toString()) {
                    Toast.makeText(
                        this@UserSettings,
                        "Hasła nie są takie same!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (newpassET.text.toString() == "") {
                    Toast.makeText(this@UserSettings, "Wprowadź hasło", Toast.LENGTH_SHORT).show()
                } else if (newpassET.text.toString().length < 5) {
                    Toast.makeText(
                        this@UserSettings,
                        "Hasło za krótkie, wprowadź hasło o długości 6+!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            btn_change_email.setOnClickListener {
                if (newemailET.text.toString().isNotEmpty() &&
                    newemailET.text.toString() == newemailconfirmET.text.toString()
                ) {
                    userSettingsVM.emailChangeConfirmation(this@UserSettings, email_displayTV)
                    hide()
                } else {
                    Toast.makeText(this@UserSettings, "Wprowadź nowy email.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            var uri = Uri.parse(" ")
            val loadImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
                binding.profileImage.setImageURI(it)
                replaceImage(it)
                uri = it
            }

            profile_image.setOnClickListener {
                loadImage.launch("image/*")
            }
            btn_upload_image.setOnClickListener {
                replaceImage(uri)
            }
        }
    }



    private fun hide() {
        background_pass_email?.visibility = View.GONE
        btn_change_email?.visibility = View.GONE
        btn_change_passwd?.visibility = View.GONE
        newpassConET?.visibility = View.GONE
        newpassET?.visibility = View.GONE
        btn_hide?.visibility = View.GONE
        newemailET?.visibility = View.GONE
        newemailconfirmET?.visibility = View.GONE
        btn_change_password_visibility?.visibility = View.VISIBLE
        btn_change_email_visibility?.visibility = View.VISIBLE
    }

    private fun showEmailChange() {
        btn_change_email_visibility.visibility = View.GONE
        btn_change_password_visibility.visibility = View.GONE
        background_pass_email.visibility = View.VISIBLE
        newemailET.visibility = View.VISIBLE
        newemailconfirmET.visibility = View.VISIBLE
        btn_hide.visibility = View.VISIBLE
        btn_change_email.visibility = View.VISIBLE
    }

    private fun showPasswordChange() {
        btn_change_email_visibility.visibility = View.GONE
        btn_change_password_visibility.visibility = View.GONE
        background_pass_email.visibility = View.VISIBLE
        newpassConET.visibility = View.VISIBLE
        newpassET.visibility = View.VISIBLE
        btn_hide.visibility = View.VISIBLE
        btn_change_passwd.visibility = View.VISIBLE
    }

    fun replaceImage(uri: Uri?) {
        try {
            val imageUri: Uri = Uri.parse(uri.toString())
            profile_image.setImageURI(imageUri)
            runBlocking {
                userSettingsVM.uploadProfileImage(imageUri)
            }
        } catch (e: Exception) {
            Log.e("Image Error", "Profile Image not found.")
        }
    }

    private fun fragmentsReplacement(fragment: Fragment) {
        val fragmentContainer = supportFragmentManager.beginTransaction()
        fragmentContainer.replace(R.id.container_main, fragment)
        fragmentContainer.commit()
    }
}

