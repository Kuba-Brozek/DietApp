package ayathe.project.scheduleapp.home.thirdfragment

import android.content.Context
import android.view.View
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

    fun passwordChangeConfirmation(context: Context, view: View){
        MaterialAlertDialogBuilder(context).setTitle("Alert").setMessage("Are you sure you want to change your password?")
            .setNegativeButton("I'll keep it that way"){ _, _ -> }
            .setPositiveButton("Change my password!"){ _, _ ->
                changePassword(view.newpassET.text.toString())
            }.show()
    }

    fun hide(view: View){
     view.background_pass_email.visibility = View.GONE
     view.btn_change_email.visibility = View.GONE
     view.btn_change_passwd.visibility = View.GONE
     view.newpassConET.visibility = View.GONE
     view.newpassET.visibility = View.GONE
     view.btn_hide.visibility = View.GONE
     view.newemailET.visibility = View.GONE
     view.newemailconfirmET.visibility = View.GONE
     view.btn_change_password_visibility.visibility = View.VISIBLE
     view.btn_change_email_visibility.visibility = View.VISIBLE
    }

    fun showEmailChange(view: View){
        view.btn_change_email_visibility.visibility = View.GONE
        view.btn_change_password_visibility.visibility = View.GONE
        view.background_pass_email.visibility = View.VISIBLE
        view.newemailET.visibility = View.VISIBLE
        view.newemailconfirmET.visibility = View.VISIBLE
        view.btn_hide.visibility = View.VISIBLE
        view.btn_change_email.visibility = View.VISIBLE
    }

    fun showPasswordChange(view: View){
     view.btn_change_email_visibility.visibility = View.GONE
     view.btn_change_password_visibility.visibility = View.GONE
     view.background_pass_email.visibility = View.VISIBLE
     view.newpassConET.visibility = View.VISIBLE
     view.newpassET.visibility = View.VISIBLE
     view.btn_hide.visibility = View.VISIBLE
     view.btn_change_passwd.visibility = View.VISIBLE
    }

    fun showUserInfo(view: View){
        repo.showUserInfo(view)
    }

}