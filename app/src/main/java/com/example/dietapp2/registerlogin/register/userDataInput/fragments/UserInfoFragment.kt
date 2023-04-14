package com.example.dietapp2.registerlogin.register.userDataInput.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.viewModels
import com.example.dietapp2.R
import com.example.dietapp2.fragments.homeactivity.HomeActivity
import com.example.dietapp2.registerlogin.register.ViewModelRegister
import kotlinx.coroutines.*

class UserInfoFragment : Fragment() {

    private lateinit var user_height_ET: EditText
    private lateinit var user_weight_ET: EditText
    private lateinit var user_age_ET: EditText
    private lateinit var finish_btn: AppCompatButton
    private val vm by viewModels<ViewModelRegister>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_info, container, false)

        user_height_ET = view.findViewById(R.id.user_height_ET)
        finish_btn = view.findViewById(R.id.finish_btn)
        user_weight_ET = view.findViewById(R.id.user_weight_ET)
        user_age_ET = view.findViewById(R.id.user_age_ET)

        finish_btn.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val height = user_height_ET.text.toString().toInt()
                val weight = user_weight_ET.text.toString().toInt()
                val age = user_age_ET.text.toString().toInt()
                sendData(height, weight, age)
                val intent = Intent(requireContext(), HomeActivity::class.java)
                startActivity(intent)
            }
        }


        return view
    }

    private fun sendData(height: Int, weight: Int, age: Int) {
        vm.readUserData {
            val user = it
            user.height = height
            user.weight = weight.toDouble()
            user.age = age
            vm.addUserToDatabase(user)
        }
    }
}