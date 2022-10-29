package ayathe.project.scheduleapp.home.usersettings

import android.content.Context
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ayathe.project.scheduleapp.repository.UserRepository
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_login.view.*
import kotlinx.coroutines.*

class UserSettingsViewModel: ViewModel() {
    private val repo = UserRepository()
    private val dispatcherIO: CoroutineDispatcher = Dispatchers.IO

    private fun changePassword(password: String, context: Context) {
        viewModelScope.launch {
            repo.changePassword(password, context)
        }
    }

    private fun changeEmail(email: String, context: Context) {
        viewModelScope.launch {
            repo.changeEmail(email, context)
        }

    }

    fun emailChangeConfirmation(context: Context, textView: TextView) = viewModelScope.launch {
        MaterialAlertDialogBuilder(context).setTitle("Alert")
            .setMessage("Are you sure you want to change your email?")
            .setNegativeButton("I'll keep it that way") { _, _ -> }
            .setPositiveButton("Change my email!") { _, _ ->
                changeEmail(textView.text.toString(), context)
            }.show()
    }


    fun passwordChangeConfirmation(context: Context, password: String)= viewModelScope.launch{
        MaterialAlertDialogBuilder(context).setTitle("Alert").setMessage("Are you sure you want to change your password?")
            .setNegativeButton("I'll keep it that way"){ _, _ -> }
            .setPositiveButton("Change my password!"){ _, _ ->
                changePassword(password, context)
            }.show()
    }


    fun showUserInfo(): String{
        return repo.showUserInfo()
    }

    suspend fun loadProfileImage(context: Context, view: ImageView){
        viewModelScope.launch {
            repo.loadProfileImage(context, view)
        }.join()
    }
    suspend fun uploadProfileImage(imageFileUri: Uri) {
        viewModelScope.launch {
            repo.uploadProfileImage(imageFileUri)
        }.join()
    }

}