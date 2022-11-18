package ayathe.project.dietapp.home.usersettings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import ayathe.project.dietapp.R
import ayathe.project.dietapp.databinding.ActivitySettingsBinding
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.coroutines.launch
import ayathe.project.dietapp.home.homeactivity.HomeActivity
import ayathe.project.dietapp.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers


class UserSettings : AppCompatActivity() {

    private val userSettingsVM by viewModels<UserSettingsViewModel>()
    private lateinit var binding: ActivitySettingsBinding
    private var auth = FirebaseAuth.getInstance()
    private val repo = UserRepository()
    private val cloud = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        lifecycleScope.launch {
            try {

                userSettingsVM.readUserData {
                    CoroutineScope(Dispatchers.Main).launch {
                        username_TV.text = it.username.toString()
                        email_displayTV.text = it.email.toString()
                    }
                }
                    CoroutineScope(Dispatchers.Main).launch {
                        userSettingsVM.loadProfileImage(this@UserSettings, profile_image)
                    }
            } catch ( ex: Exception){
                Log.e("LoadingError", "Loading image, or user data failed")
            }
        }
            val jsonString = userSettingsVM.getJsonDataFromAsset(this@UserSettings, "json.json")
            val userList = userSettingsVM.dataClassFromJsonString(jsonString!!)
            val loadImage = registerForActivityResult(ActivityResultContracts
                .GetContent()) {
                profile_image.setImageURI(it)
                replaceImage(it)
                CoroutineScope(Dispatchers.Main).launch {
                    userSettingsVM.uploadProfileImage(it)
                }
            }

            profile_image.setOnClickListener {
                loadImage.launch("image/*")
            }

            change_personal_info_btn.setOnClickListener {
                val intent = Intent(this@UserSettings,
                    ChangePersonalInfoActivity::class.java)
                startActivity(intent)
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

    private fun fragmentsReplacement(fragment: Fragment) {
        val fragmentContainer = supportFragmentManager.beginTransaction()
        fragmentContainer.replace(R.id.container_main, fragment)
        fragmentContainer.commit()
    }

    override fun onBackPressed() {
        val intent = Intent(this@UserSettings, HomeActivity::class.java)
        startActivity(intent)
    }


}

