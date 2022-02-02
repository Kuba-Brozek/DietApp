package ayathe.project.scheduleapp.home.thirdfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import ayathe.project.scheduleapp.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_register.*
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
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_third, container, false)

        view.newpassET.visibility = View.GONE
        view.newpassConET.visibility = View.GONE
        view.btn_change_passwd.visibility = View.GONE
        view.btn_hide_password.visibility = View.GONE
        view.btn_change_password_visibility.setOnClickListener {
            view.newpassET.visibility = View.VISIBLE
            view.newpassConET.visibility = View.VISIBLE
            view.btn_change_passwd.visibility = View.VISIBLE
            view.btn_hide_password.visibility = View.VISIBLE
        }
        view.newemailET.visibility = View.GONE
        view.btn_change_email.visibility = View.GONE
        view.btn_hide_email.visibility = View.GONE
        view.btn_change_email_visibility.setOnClickListener {
            view.newemailET.visibility = View.VISIBLE
            view.btn_change_email.visibility = View.VISIBLE
            view.btn_hide_email.visibility = View.VISIBLE
        }
        view.btn_hide_password.setOnClickListener {
            view.newpassET.visibility = View.GONE
            view.newpassConET.visibility = View.GONE
            view.btn_change_passwd.visibility = View.GONE
            view.btn_hide_password.visibility = View.GONE
            view.btn_change_password_visibility.visibility = View.VISIBLE
        }
        view.btn_hide_email.setOnClickListener {
            view.newemailET.visibility = View.GONE
            view.btn_change_email.visibility = View.GONE
            view.btn_hide_email.visibility = View.GONE
            view.btn_change_email_visibility.visibility = View.VISIBLE
        }
        view.btn_change_passwd.setOnClickListener{
            if(newpassET.text.toString() == newpassConET.text.toString() && newpassET.text.toString() != ""){
                passwordChangeConfirmation()
                view.newpassET.visibility = View.GONE
                view.newpassConET.visibility = View.GONE
                view.btn_change_passwd.visibility = View.GONE
                view.btn_hide_password.visibility = View.GONE
                view.btn_change_password_visibility.visibility = View.VISIBLE
            }
            else if(newpassET.text.toString() != newpassConET.text.toString()){
                Toast.makeText(requireContext(), "Hasła nie są takie same!", Toast.LENGTH_SHORT).show()
            }
        }
        view.btn_change_email.setOnClickListener {
            if(newemailET.text.toString().isNotEmpty()){
                emailChangeConfirmation()
                view.newemailET.visibility = View.GONE
                view.btn_change_email.visibility = View.GONE
                view.btn_hide_email.visibility = View.GONE
                view.btn_change_email_visibility.visibility = View.VISIBLE
            }
            else{
                Toast.makeText(requireContext(), "Wprowadź nowy email.", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun passwordChangeConfirmation(){
            MaterialAlertDialogBuilder(requireContext()).setTitle("Alert").setMessage("Are you sure you want to change your password?")
                .setNegativeButton("I'll keep it that way"){ _, _ -> }
                .setPositiveButton("Change my password!"){ _, _ ->
                    thirdVM.changePassword(newpassET.text.toString())
                }.show()

    }

    private fun emailChangeConfirmation(){
        MaterialAlertDialogBuilder(requireContext()).setTitle("Alert").setMessage("Are you sure you want to change your email?")
            .setNegativeButton("I'll keep it that way"){ _, _ -> }
            .setPositiveButton("Change my email!"){ _, _ ->
                thirdVM.changeEmail(newemailET.text.toString())
            }.show()
    }
}

