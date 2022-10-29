package ayathe.project.scheduleapp.home.thirdfragment

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import ayathe.project.scheduleapp.R
import ayathe.project.scheduleapp.home.homeactivity.HomeActivity
import kotlinx.android.synthetic.main.fragment_third.*
import kotlinx.android.synthetic.main.fragment_third.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class UserSettingsFragment : Fragment() {

    private val userSettingsVM by viewModels<UserSettingsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_third, container, false)
        lifecycleScope.launchWhenStarted {
            userSettingsVM.loadProfileImage(requireContext(), view)
            hide(view)
            userSettingsVM.showUserInfo(view)
            userSettingsVM.loadProfileImage(requireContext(), view)
            view.btn_change_password_visibility.setOnClickListener {
                showPasswordChange(view)
            }
            view.btn_change_email_visibility.setOnClickListener {
                showEmailChange(view)
                userSettingsVM.showUserInfo(view)
            }
            view.btn_hide.setOnClickListener {
                hide(view)
            }

            view.btn_change_passwd.setOnClickListener {
                if (newpassET.text.toString() == newpassConET.text.toString()
                    && newpassET.text.toString() != "" && newpassET.text.toString().length > 5) {
                    userSettingsVM.passwordChangeConfirmation(requireContext(), view, view.newpassET.text.toString())

                    hide(view)
                } else if (newpassET.text.toString() == newpassConET.text.toString()) {
                    Toast.makeText(requireContext(), "Hasła nie są takie same!", Toast.LENGTH_SHORT).show()
                } else if (newpassET.text.toString() == ""){
                    Toast.makeText(requireContext(), "Wprowadź hasło", Toast.LENGTH_SHORT).show()
                } else if ( newpassET.text.toString().length < 5) {
                    Toast.makeText(requireContext(), "Hasło za krótkie, wprowadź hasło o długości 6+!", Toast.LENGTH_SHORT).show()
                }
            }
            view.btn_change_email.setOnClickListener {
                if(newemailET.text.toString().isNotEmpty() &&
                    newemailET.text.toString() == newemailconfirmET.text.toString()){
                    userSettingsVM.emailChangeConfirmation(requireContext() ,view)
                    hide(view)
                } else{
                    Toast.makeText(requireContext(), "Wprowadź nowy email.", Toast.LENGTH_SHORT).show()
                }
            }
            view.profile_image.setOnClickListener {
                (activity as HomeActivity).openGallery()
            }
            view.btn_upload_image.setOnClickListener {
                replaceImage(view)
            }
        }
        return view
    }

    fun replaceImage(view: View) {
        try {
            val args = this.arguments
            val imageStringUri = args?.get("ImageUri")
            val imageUri: Uri = Uri.parse(imageStringUri.toString())
            view.profile_image.setImageURI(imageUri)
            runBlocking {
                userSettingsVM.uploadProfileImage(imageUri)
            }
        }catch (e: Exception){
            Log.e("Image Error", "Profile Image not found.")
        }
    }

    private fun hide(view: View){
        view.background_pass_email?.visibility = View.GONE
        view.btn_change_email?.visibility = View.GONE
        view.btn_change_passwd?.visibility = View.GONE
        view.newpassConET?.visibility = View.GONE
        view.newpassET?.visibility = View.GONE
        view.btn_hide?.visibility = View.GONE
        view.newemailET?.visibility = View.GONE
        view.newemailconfirmET?.visibility = View.GONE
        view.btn_change_password_visibility?.visibility = View.VISIBLE
        view.btn_change_email_visibility?.visibility = View.VISIBLE
    }

    private fun showEmailChange(view: View){
        view.btn_change_email_visibility.visibility = View.GONE
        view.btn_change_password_visibility.visibility = View.GONE
        view.background_pass_email.visibility = View.VISIBLE
        view.newemailET.visibility = View.VISIBLE
        view.newemailconfirmET.visibility = View.VISIBLE
        view.btn_hide.visibility = View.VISIBLE
        view.btn_change_email.visibility = View.VISIBLE
    }

    private fun showPasswordChange(view: View){
        view.btn_change_email_visibility.visibility = View.GONE
        view.btn_change_password_visibility.visibility = View.GONE
        view.background_pass_email.visibility = View.VISIBLE
        view.newpassConET.visibility = View.VISIBLE
        view.newpassET.visibility = View.VISIBLE
        view.btn_hide.visibility = View.VISIBLE
        view.btn_change_passwd.visibility = View.VISIBLE
    }
}

