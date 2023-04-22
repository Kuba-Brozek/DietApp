package com.example.dietapp2.fragments.sport

import android.annotation.SuppressLint
import android.app.DatePickerDialog
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
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.viewModels
import com.example.dietapp2.DTO.DayInfo
import com.example.dietapp2.DTO.Sport
import com.example.dietapp2.DTO.User
import com.example.dietapp2.DTO.UserDetails
import com.example.dietapp2.R
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date

class ManageSportsFragment : Fragment() {

    private val summaryVM by viewModels<SportContainerFragmentViewModel>()
    private lateinit var increment_date_btn_manage: AppCompatButton
    private lateinit var decrement_date_btn_manage: AppCompatButton
    private lateinit var delete_sport_BTN: AppCompatButton
    private lateinit var go_back_BTN: AppCompatButton
    private lateinit var curr_date_TV_manage: TextView
    private lateinit var sport_name_TV: TextView
    private lateinit var kcal_burnt_TV: TextView
    private lateinit var sport_list_LV_manage: ListView
    private lateinit var arrayAdapter: ArrayAdapter<*>
    private val calendar = Calendar.getInstance()
    private val year = calendar.get(Calendar.YEAR)
    private val month = calendar.get(Calendar.MONTH)
    private val day = calendar.get(Calendar.DAY_OF_MONTH)

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_manage_sports, container, false)
        increment_date_btn_manage = view.findViewById(R.id.increment_date_btn_manage)
        decrement_date_btn_manage = view.findViewById(R.id.decrement_date_btn_manage)
        curr_date_TV_manage = view.findViewById(R.id.curr_date_TV_manage)
        delete_sport_BTN = view.findViewById(R.id.delete_sport_BTN)
        sport_name_TV = view.findViewById(R.id.sport_name_TV)
        kcal_burnt_TV = view.findViewById(R.id.kcal_burnt_TV)
        go_back_BTN = view.findViewById(R.id.go_back_BTN)
        sport_list_LV_manage = view.findViewById(R.id.sport_list_LV_manage)
        val sdf = SimpleDateFormat("dd.MM.yyyy")
        val currentDate = sdf.format(Date())
        curr_date_TV_manage.text = currentDate
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        var userDetails = UserDetails()
        var user = User()
        var dayInfo = DayInfo()

        val sport = Sport()
        summaryVM.readUserDetails { userDetails = it }
        summaryVM.readUserData { user = it }
        summaryVM.readDayInfo(currentDate) {
            dayInfo = it
            arrayAdapterFilter(dayInfo.activitiesMade!!)
        }
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        increment_date_btn_manage.setOnClickListener {
            val result = summaryVM.dataParserFromDate(curr_date_TV_manage.text.toString())
            val period = Period.of(0, 0, 1)
            val parsed = LocalDate.parse(result)
            val r = parsed.plus(period)
            val final = summaryVM.dataParserToDate(r.toString())
            curr_date_TV_manage.text = final
        }

        curr_date_TV_manage.setOnClickListener {
            val dpd = DatePickerDialog(requireContext(),
                { _, mYear, mMonth, mDay ->
                    var stringMonth = (mMonth + 1).toString()
                    var mmDay = mDay.toString()
                    if (stringMonth.toInt() < 10) {
                        stringMonth = "0${stringMonth}"
                    }
                    if (mmDay.toInt() < 10) {
                        mmDay = "0$mmDay"
                    }
                    curr_date_TV_manage.text = "$mmDay.$stringMonth.$mYear"
                }, year, month, day
            )
            dpd.show()
        }

        decrement_date_btn_manage.setOnClickListener {
            val result = summaryVM.dataParserFromDate(curr_date_TV_manage.text.toString())
            val period = Period.of(0, 0, 1)
            val parsed = LocalDate.parse(result)
            val r = parsed.minus(period)
            val final = summaryVM.dataParserToDate(r.toString())
            curr_date_TV_manage.text = final
        }

        curr_date_TV_manage.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                    summaryVM.readDayInfo(curr_date_TV_manage.text.toString()) {di ->
                        dayInfo = di
                        arrayAdapterFilter(dayInfo.activitiesMade!!)
                    }
            }
        })

        sport_list_LV_manage.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView: AdapterView<*>, _: View, i: Int, _: Long ->
                val x = dayInfo.activitiesMade!!
                    .find { it == adapterView.getItemAtPosition(i).toString() }!!.split(", kcal Burnt: ")
                sport.name = x[0]
                sport.kcal = x[1].toInt()
                sport_name_TV.text = if(sport.name.length < 13) sport.name else sport.name.substring(0..12)
                kcal_burnt_TV.text = sport.kcal.toString()
            }

        delete_sport_BTN.setOnClickListener {
            if (sport_name_TV.text == "Sport name" || kcal_burnt_TV.text == "Kcal burnt"){
                Toast.makeText(requireContext(), "Please enter activity you want to delete", Toast.LENGTH_SHORT).show()
            } else {
                dayInfo.kcalGoal = dayInfo.kcalGoal!! - sport.kcal
                dayInfo.kcalBurnt = dayInfo.kcalBurnt!! - sport.kcal
                dayInfo.activitiesMade = dayInfo.activitiesMade?.minus(dayInfo.activitiesMade!!
                    .find {
                        it.contains(sport.name) && it.contains(sport.kcal.toString())
                    }!!)
                summaryVM.updateDayInfo(curr_date_TV_manage.text.toString(), dayInfo)
                summaryVM.readDayInfo(curr_date_TV_manage.text.toString()) {di ->
                    dayInfo = di
                    arrayAdapterFilter(dayInfo.activitiesMade!!)
                }
                sport_name_TV.text = "Sport name"
                kcal_burnt_TV.text = "Kcal burnt"
            }
        }

        go_back_BTN.setOnClickListener {
            fragmentsReplacement(SportsFragment())
        }

        return view

    }

    private fun arrayAdapterFilter(list: List<String>) {
        arrayAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            list)
        sport_list_LV_manage.adapter = arrayAdapter
    }

    private fun fragmentsReplacement(fragment: Fragment){
        val fragmentContainer = requireParentFragment().childFragmentManager.beginTransaction()
        fragmentContainer.replace(R.id.container_sports, fragment)
        fragmentContainer.commit()
    }

}