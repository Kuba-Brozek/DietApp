package com.example.dietapp2.registerlogin.register.userDataInput.fragments

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.dietapp2.R
import com.example.dietapp2.registerlogin.register.ViewModelRegister
import com.example.dietapp2.registerlogin.register.userDataInput.AfterRegistrationActivity
import com.example.dietapp2.repository.UserRepository

class IntroductionFragment : Fragment() {


    private lateinit var user_nickname_ET: EditText
    private lateinit var goal_spinner: Spinner
    private lateinit var introduction: TextView
    private lateinit var btn_go_to_next_fragment: AppCompatButton
    private val vm by viewModels<ViewModelRegister>()
    private val repo = UserRepository()
    private val options = listOf("Your goal", "Weight loss", "Keep weight", "Gain weight")
    private var goal = "Your goal"
    private var nickname = "Your nickname"

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {



        val view: View =  inflater.inflate(R.layout.fragment_introduction, container, false)
        user_nickname_ET = view.findViewById(R.id.user_nickname_ET)
        goal_spinner = view.findViewById(R.id.goal_spinner)
        introduction = view.findViewById(R.id.textView4)
        btn_go_to_next_fragment = view.findViewById(R.id.btn_go_to_next_fragment)
        view.findViewById<ImageView>(R.id.work_out_IV).clipToOutline = true
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        btn_go_to_next_fragment.setOnClickListener {
            (activity as AfterRegistrationActivity).swipeRight()
        }

        repo.readUserData {
            introduction.text = "Hello ${it.email}"
        }




        goal_spinner.adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, options)



        user_nickname_ET.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                nickname = user_nickname_ET.text.toString()
            }
        })


        goal_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                goal = parent!!.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        return view
    }

    override fun onPause() {
        sendData(goal, nickname)
        super.onPause()
    }

    private fun sendData(goal: String, nickname: String){
        vm.readUserData {
                val user = it
                user.username = nickname
                user.destination = goal
                vm.addUserToDatabase(user)
        }
    }
}