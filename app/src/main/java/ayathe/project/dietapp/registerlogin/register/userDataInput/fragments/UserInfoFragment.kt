package ayathe.project.dietapp.registerlogin.register.userDataInput.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import ayathe.project.dietapp.R
import ayathe.project.dietapp.fragments.homeactivity.HomeActivity
import ayathe.project.dietapp.registerlogin.register.ViewModelRegister
import kotlinx.android.synthetic.main.fragment_user_info.view.*
import kotlinx.coroutines.*

class UserInfoFragment : Fragment() {

    private val vm by viewModels<ViewModelRegister>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_info, container, false)

            view.finish_btn.setOnClickListener{
                CoroutineScope(Dispatchers.Main).launch {
                    val height = view.user_height_ET.text.toString().toInt()
                    val weight = view.user_weight_ET.text.toString().toInt()
                    val age = view.user_age_ET.text.toString().toInt()
                    sendData(height, weight, age)
                    val intent = Intent(requireContext(), HomeActivity::class.java)
                    startActivity(intent)
                }
            }


        return view
    }

    private fun sendData(height: Int, weight: Int, age: Int){
        vm.readUserData {
            val user = it
            user.height = height
            user.weight = weight.toDouble()
            user.age = age
            vm.addUserToDatabase(user)
        }
    }
}