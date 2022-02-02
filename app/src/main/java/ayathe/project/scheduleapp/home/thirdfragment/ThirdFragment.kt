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
        view.btn_change_passwd.setOnClickListener{
            if(newpassET.text.toString() == newpassConET.text.toString()){
                passwordChangeConfirmation()
            }
            else if(newpassET.text.toString() != newpassConET.text.toString()){
                Toast.makeText(requireContext(), "Hasła nie są takie same!", Toast.LENGTH_SHORT).show()
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
}

