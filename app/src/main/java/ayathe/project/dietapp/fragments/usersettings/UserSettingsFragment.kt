package ayathe.project.dietapp.fragments.usersettings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import ayathe.project.dietapp.R
import kotlinx.coroutines.launch
import ayathe.project.dietapp.fragments.homeactivity.HomeActivity
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.settings_fragment.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers


class UserSettingsFragment : Fragment() {

    private val userSettingsVM by viewModels<UserSettingsViewModel>()
    private val mainDispatcher = Dispatchers.Main

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.settings_fragment, container, false)
        lifecycleScope.launch {
            try {

                userSettingsVM.readUserData {
                    CoroutineScope(Dispatchers.Main).launch {
                        view.username_TV.text = it.username.toString()
                        view.email_displayTV.text = it.email.toString()
                        view.age_displayTV.text = it.age.toString()
                        view.height_displayTV.text = it.height.toString()
                        view.destination_displayTV.text = it.destination.toString()
                    }
                }
                userSettingsVM.loadProfileImage(this@UserSettingsFragment.requireContext(), view.profile_image)

            } catch ( ex: Exception){
                Log.e("LoadingError", "Loading image, or user data loading failed")
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@UserSettingsFragment.requireContext(), HomeActivity::class.java)
                startActivity(intent)
            }
        })
            val jsonString = userSettingsVM.getJsonDataFromAsset(
                this@UserSettingsFragment.requireContext(), "json.json")
//            val userList = userSettingsVM.dataClassFromJsonString(jsonString!!)
            val loadImage = registerForActivityResult(ActivityResultContracts
                .GetContent()) {
                view.profile_image.setImageURI(it)
                replaceImage(it, view)
                userSettingsVM.uploadProfileImage(it)
            }

        view.change_personal_info_btn.setOnClickListener {
            val intent = Intent(
                this@UserSettingsFragment.requireContext(),
                ChangePersonalInfoActivity::class.java)
            startActivity(intent)
        }
        view.profile_image.setOnClickListener {
            loadImage.launch("image/*")
        }

        return view
    }


    private fun replaceImage(uri: Uri?, view: View) {
        try {
            val imageUri: Uri = Uri.parse(uri.toString())
            view.profile_image.setImageURI(imageUri)

        } catch (e: Exception) {
            Log.e("Image Error", "Profile Image not found.")
        }
    }
}

