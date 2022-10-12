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
import ayathe.project.scheduleapp.R
import ayathe.project.scheduleapp.home.homeactivity.HomeActivity
import kotlinx.android.synthetic.main.fragment_third.*
import kotlinx.android.synthetic.main.fragment_third.view.*

class ThirdFragment : Fragment() {

    private val thirdVM by viewModels<ViewModelThirdFragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_third, container, false)
        thirdVM.loadProfileImage(requireContext(), view)
        hide(view)
        thirdVM.showUserInfo(view)
        thirdVM.loadProfileImage(requireContext(), view)
        view.btn_change_password_visibility.setOnClickListener {
            showPasswordChange(view)
        }
        view.btn_change_email_visibility.setOnClickListener {
            showEmailChange(view)
            thirdVM.showUserInfo(view)
        }
        view.btn_hide.setOnClickListener {
            hide(view)
        }

        view.btn_change_passwd.setOnClickListener{
            if(newpassET.text.toString() == newpassConET.text.toString()
                && newpassET.text.toString() != ""){
                thirdVM.passwordChangeConfirmation(requireContext(), view, view.newpassET.text.toString())

                hide(view)
            }
            else if(newpassET.text.toString() != newpassConET.text.toString()){
                Toast.makeText(requireContext(), "Hasła nie są takie same!", Toast.LENGTH_SHORT).show()
            }
        }
        view.btn_change_email.setOnClickListener {
            if(newemailET.text.toString().isNotEmpty() &&
                newemailET.text.toString() == newemailconfirmET.text.toString()){
                thirdVM.emailChangeConfirmation(requireContext() ,view)
                Toast.makeText(requireContext(), "Udało się zmienić email!", Toast.LENGTH_LONG).show()
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
        return view
    }

    private fun replaceImage(view: View){
        try {
            val args = this.arguments
            val imageStringUri = args?.get("ImageUri")
            val imageUri: Uri = Uri.parse(imageStringUri.toString())
            view.profile_image.setImageURI(imageUri)
            thirdVM.uploadProfileImage(imageUri)
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

