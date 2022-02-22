package ayathe.project.scheduleapp.home.thirdfragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import ayathe.project.scheduleapp.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.fragment_third.*
import kotlinx.android.synthetic.main.fragment_third.view.*

class ThirdFragment : Fragment() {

    private val thirdVM by viewModels<ViewModelThirdFragment>()
    private val REQUEST_IMAGE_CAPTURE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view: View = view!!
        val checkPermission =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    val uri: Uri = data?.data!!
                    view.profile_image.setImageURI(uri)
                    view.profile_image.tag = uri
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_third, container, false)
        thirdVM.loadProfileImage(requireContext(), view)
        thirdVM.hide(view)
        thirdVM.showUserInfo(view)
        view.btn_change_password_visibility.setOnClickListener {
            thirdVM.showPasswordChange(view)
        }
        view.btn_change_email_visibility.setOnClickListener {
            thirdVM.showEmailChange(view)
            thirdVM.showUserInfo(view)
        }
        view.btn_hide.setOnClickListener {
            thirdVM.hide(view)
        }

        view.btn_change_passwd.setOnClickListener{
            if(newpassET.text.toString() == newpassConET.text.toString()
                && newpassET.text.toString() != ""){
                thirdVM.passwordChangeConfirmation(requireContext(), view)
                Toast.makeText(requireContext(), "Udało się zmienić hasło!", Toast.LENGTH_LONG).show()
                thirdVM.hide(view)
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
                thirdVM.hide(view)
            } else{
                Toast.makeText(requireContext(), "Wprowadź nowy email.", Toast.LENGTH_SHORT).show()
            }
        }
        view.profile_image.setOnClickListener {

        }
        view.btn_upload_image.setOnClickListener {
            val imgURI = view.btn_upload_image.tag as Uri
            thirdVM.uploadProfileImage(imgURI)
        }
        return view
    }

    private fun openGallery(view: View) {
        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val uri: Uri = data?.data!!
                view.profile_image.setImageURI(uri)
                view.profile_image.tag = uri
            }
        }
        val intent = Intent("android.intent.action.GET_CONTENT")
        intent.type = "image/*"
        resultLauncher.launch(intent)
    }
}

