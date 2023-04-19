package com.example.dietapp2.fragments.usersummary

import android.annotation.SuppressLint
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.viewModels
import com.example.dietapp2.DTO.DayInfo
import com.example.dietapp2.DTO.ProductFromJSON
import com.example.dietapp2.DTO.User
import com.example.dietapp2.DTO.UserDetails
import com.example.dietapp2.R
import com.example.dietapp2.fragments.homeactivity.HomeActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.Locale
import kotlin.math.log


class UserSummaryFragment : Fragment() {

    private lateinit var starting_day_TV: TextView
    private lateinit var current_day_count_TV: TextView
    private lateinit var s_weight_TV: TextView
    private lateinit var dayX_TV: TextView
    private lateinit var diff_TV: TextView
    private lateinit var hello_user_TV: TextView
    private lateinit var current_day_TV: TextView
    private lateinit var premium_TV_1: TextView
    private lateinit var premium_TV_2: TextView
    private lateinit var premium_TV_3: TextView
    private lateinit var premium_TV_4: TextView
    private lateinit var latest_weight_ET: EditText
    private lateinit var change_weight_BTN: AppCompatButton
    private lateinit var buy_premium_BTN: AppCompatButton
    private lateinit var locked_content_IV: ImageView
    private val summaryVM by viewModels<UserSummaryViewModel>()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view: View = inflater.inflate(R.layout.fragment_home, container, false)
        starting_day_TV = view.findViewById(R.id.starting_day_TV)
        s_weight_TV = view.findViewById(R.id.s_weight_TV)
        current_day_count_TV = view.findViewById(R.id.current_day_count_TV)
        dayX_TV = view.findViewById(R.id.dayX_TV)
        latest_weight_ET = view.findViewById(R.id.latest_weight_ET)
        change_weight_BTN = view.findViewById(R.id.change_weight_BTN)
        buy_premium_BTN = view.findViewById(R.id.buy_premium_BTN)
        diff_TV = view.findViewById(R.id.diff_TV)
        hello_user_TV = view.findViewById(R.id.hello_user_TV)
        locked_content_IV = view.findViewById(R.id.locked_content_IV)
        current_day_TV = view.findViewById(R.id.current_day_TV)
        premium_TV_1 = view.findViewById(R.id.premium_TV_1)
        premium_TV_2 = view.findViewById(R.id.premium_TV_2)
        premium_TV_3 = view.findViewById(R.id.premium_TV_3)
        premium_TV_4 = view.findViewById(R.id.premium_TV_4)
        var user = User()
        var userDetails = UserDetails()

        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)


        latest_weight_ET.inputType = InputType.TYPE_CLASS_NUMBER
        summaryVM.readUserData {userInfo ->
            user = userInfo
            starting_day_TV.text = user.startingDate!!.also { Log.e("SD", it) }
            s_weight_TV.text = user.weight.toString().also { Log.e("SD", it) }
            val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault(Locale.Category.FORMAT))
            val helper = sdf.format(Date())
            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
            val currentDate: LocalDate = LocalDate.parse(helper, formatter)
            val startingDate: LocalDate = LocalDate.parse(user.startingDate, formatter)
            val userAccountAgeInDays = summaryVM.userAccountAgeInDays(startingDate, currentDate)
            current_day_count_TV.text = (userAccountAgeInDays.toInt()+1).toString()
            val dayX = "DAY ${(userAccountAgeInDays.toInt()+1)}"
            dayX_TV.text = dayX
            val userMessage = "Hello ${user.username}!"
            hello_user_TV.text = userMessage
            current_day_TV.text = currentDate.format(formatter)
            summaryVM.readUserDetails {
                userDetails = it
                if(userDetails.hasPremium){
                    buy_premium_BTN.visibility = View.GONE
                    locked_content_IV.visibility = View.GONE
                    premium_TV_1.visibility = View.GONE
                    premium_TV_2.visibility = View.GONE
                    premium_TV_3.visibility = View.GONE
                    premium_TV_4.visibility = View.GONE
                    fragmentsReplacement(SportsFragment())
                }
                latest_weight_ET.setText(it.weight.toString())
                diff_TV.text = (user.weight!! - it.weight).toString()
            }
        }


        change_weight_BTN.setOnClickListener {
            val latestWeight = latest_weight_ET.text
            if(latestWeight.toString().toDoubleOrNull() != null){
                userDetails.weight = latestWeight.toString().toDouble()
                diff_TV.text = (user.weight!! - latestWeight.toString().toDouble()).toString()
                summaryVM.addUserDetailsToDB(userDetails){}
            } else {
                Toast.makeText(requireContext(), "Please provide proper weight", Toast.LENGTH_SHORT).show()
            }
        }


        buy_premium_BTN.setOnClickListener {
            if(!userDetails.hasPremium){
            MaterialAlertDialogBuilder(requireContext()).setTitle("PREMIUM")
                .setMessage("Are you sure you want to buy premium version?")
                .setNegativeButton("No I'm good") { _, _ -> }
                .setPositiveButton("Yes, I want to buy premium") { _, _ ->
                    userDetails.hasPremium = true
                    buy_premium_BTN.visibility = View.GONE
                    locked_content_IV.visibility = View.GONE
                    premium_TV_1.visibility = View.GONE
                    premium_TV_2.visibility = View.GONE
                    premium_TV_3.visibility = View.GONE
                    premium_TV_4.visibility = View.GONE
                    summaryVM.addUserDetailsToDB(userDetails){}
                    fragmentsReplacement(SportsFragment())
                    }
                .show()
            }
        }



        return view
    }

    private fun fragmentsReplacement(fragment: Fragment){
        val fragmentContainer = childFragmentManager.beginTransaction()
        fragmentContainer.replace(R.id.container_sports, fragment)
        fragmentContainer.commit()
    }

}
