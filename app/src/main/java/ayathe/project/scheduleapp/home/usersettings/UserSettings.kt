package ayathe.project.scheduleapp.home.usersettings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
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
import android.view.ViewGroup
import ayathe.project.scheduleapp.DTO.User
import ayathe.project.scheduleapp.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
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
                    username_TV.text = it.destination.toString()
                }
                userSettingsVM.loadProfileImage(this@UserSettings, profile_image)
            } catch ( ex: Exception){

            }
        }
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


}

