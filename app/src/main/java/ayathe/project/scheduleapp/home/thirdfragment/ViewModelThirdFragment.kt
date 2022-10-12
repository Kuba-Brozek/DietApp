package ayathe.project.scheduleapp.home.thirdfragment

import android.content.Context
import android.net.Uri
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModel
import ayathe.project.scheduleapp.repository.UserRepository
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_login.view.*
import kotlinx.android.synthetic.main.fragment_third.*
import kotlinx.android.synthetic.main.fragment_third.view.*

class ViewModelThirdFragment: ViewModel() {
    private val repo = UserRepository()

    private fun changePassword(password: String){
        repo.changePassword(password)
    }
    private fun changeEmail(email: String){
        repo.changeEmail(email)
    }

    fun emailChangeConfirmation(context: Context, view: View){
        MaterialAlertDialogBuilder(context).setTitle("Alert").setMessage("Are you sure you want to change your email?")
            .setNegativeButton("I'll keep it that way"){ _, _ -> }
            .setPositiveButton("Change my email!"){ _, _ ->
                changeEmail(view.newemailET.text.toString())
            }.show()
    }

    fun passwordChangeConfirmation(context: Context, view: View, password: String){
        MaterialAlertDialogBuilder(context).setTitle("Alert").setMessage("Are you sure you want to change your password?")
            .setNegativeButton("I'll keep it that way"){ _, _ -> }
            .setPositiveButton("Change my password!"){ _, _ ->
                changePassword(password)
                Toast.makeText(context, "Udało się zmienić hasło!", Toast.LENGTH_LONG).show()
            }.show()
    }


    fun showUserInfo(view: View){
        repo.showUserInfo(view)
    }

    fun loadProfileImage(context: Context, view: View){
        repo.loadProfileImage(context, view)
    }
    fun uploadProfileImage(imageFileUri: Uri){
        repo.uploadProfileImage(imageFileUri)
    }

}