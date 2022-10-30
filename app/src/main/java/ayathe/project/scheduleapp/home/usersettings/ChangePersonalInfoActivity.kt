package ayathe.project.scheduleapp.home.usersettings

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import ayathe.project.scheduleapp.databinding.ActivityChangePersonalInfoBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_change_personal_info.*
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ChangePersonalInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangePersonalInfoBinding
    private val userSettingsVM by viewModels<UserSettingsViewModel>()
    private var auth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            ActivityChangePersonalInfoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        lifecycleScope.launch {
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
                replaceImage(it)
                    val a = it
                    profile_image_BTN_CPI.setOnClickListener {
                        CoroutineScope(Dispatchers.Main).launch {
                        userSettingsVM.uploadProfileImage(a)
                    }
                }
            }

            profile_image_IV_CPI.setOnClickListener {
                loadImage.launch("image/*")
            }
        }
    }

    private fun replaceImage(uri: Uri?) {
        try {
            val imageUri: Uri = Uri.parse(uri.toString())
            profile_image.setImageURI(imageUri)
        } catch (e: Exception) {
            Log.e("Image Error", "Profile Image not found.")
        }
    }
}