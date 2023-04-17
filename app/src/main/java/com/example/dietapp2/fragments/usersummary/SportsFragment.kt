package com.example.dietapp2.fragments.usersummary

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
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

class SportsFragment : Fragment() {

    private val summaryVM by viewModels<UserSummaryViewModel>()
    private var sportList = getSportsList()
    private lateinit var arrayAdapter: ArrayAdapter<*>
    private lateinit var sport_list_LV: ListView
    private lateinit var search_sport_ET: EditText
    private lateinit var sport_name_TV: TextView
    private lateinit var kcal_sport_TV: TextView
    private lateinit var time_trained_ET: EditText
    private lateinit var send_kcal_to_db_IV: AppCompatButton
    private lateinit var increment_date_btn_sport: AppCompatButton
    private lateinit var decrement_date_btn_sport: AppCompatButton
    private lateinit var curr_date_TV: TextView
    private lateinit var manage_sports_btn: AppCompatButton
    private val calendar = Calendar.getInstance()
    private val year = calendar.get(Calendar.YEAR)
    private val month = calendar.get(Calendar.MONTH)
    private val day = calendar.get(Calendar.DAY_OF_MONTH)

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sports, container, false)


        sport_list_LV = view.findViewById(R.id.sport_list_LV)
        search_sport_ET = view.findViewById(R.id.search_sport_ET)
        sport_name_TV = view.findViewById(R.id.sport_name_TV)
        kcal_sport_TV = view.findViewById(R.id.kcal_sport_TV)
        time_trained_ET = view.findViewById(R.id.time_trained_ET)
        send_kcal_to_db_IV = view.findViewById(R.id.send_kcal_to_db_IV)
        increment_date_btn_sport = view.findViewById(R.id.increment_date_btn_sport)
        decrement_date_btn_sport = view.findViewById(R.id.decrement_date_btn_sport)
        curr_date_TV = view.findViewById(R.id.curr_date_TV)
        manage_sports_btn = view.findViewById(R.id.manage_sports_btn)



        var userDetails = UserDetails()
        var user = User()
        var sport = Sport()
        var dayInfo = DayInfo()
        val sdf = SimpleDateFormat("dd.MM.yyyy")
        val currentDate = sdf.format(Date())
        curr_date_TV.text = currentDate
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        arrayAdapterFilter(sportList)
        sport_list_LV.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView: AdapterView<*>, _: View, i: Int, _: Long ->
                sport = sportList.find { it.toString() == adapterView.getItemAtPosition(i).toString() }!!
                sport_name_TV.text = sport.name
                kcal_sport_TV.text = ((sport.kcal / 60) * time_trained_ET.text.toString().toInt()).toString()

            }
        summaryVM.readUserDetails {
            userDetails = it
        }
        var userAccountAgeInDays = ""
        summaryVM.readUserData {
            user = it
            val startingDate: LocalDate = LocalDate.parse(user.startingDate, formatter)
            val date: LocalDate = LocalDate.parse(currentDate, formatter)
            userAccountAgeInDays = summaryVM.userAccountAgeInDays(startingDate, date)

        }




        increment_date_btn_sport.setOnClickListener {
            val result = summaryVM.dataParserFromDate(curr_date_TV.text.toString())
            val period = Period.of(0, 0, 1)
            val parsed = LocalDate.parse(result)
            val r = parsed.plus(period)
            val final = summaryVM.dataParserToDate(r.toString())
            curr_date_TV.text = final
        }

        decrement_date_btn_sport.setOnClickListener {
            val result = summaryVM.dataParserFromDate(curr_date_TV.text.toString())
            val period = Period.of(0, 0, 1)
            val parsed = LocalDate.parse(result)
            val r = parsed.minus(period)
            val final = summaryVM.dataParserToDate(r.toString())
            curr_date_TV.text = final
        }

        manage_sports_btn.setOnClickListener {
            fragmentsReplacement(ManageSports())
        }


        summaryVM.readDayInfo(curr_date_TV.text.toString()) {
            dayInfo = it
        }

        curr_date_TV.setOnClickListener {
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
                    curr_date_TV.text = "$mmDay.$stringMonth.$mYear"
                }, year, month, day
            )
            dpd.show()
        }


        curr_date_TV.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                summaryVM.readDayInfo(curr_date_TV.text.toString()) {
                    dayInfo = it
                }
            }

        })

        search_sport_ET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(queryText: Editable?) {
                if (queryText.toString() != "") {
                    arrayAdapterFilter(sportList.filter {
                        it.name.lowercase().contains(queryText.toString().lowercase())
                    })
                } else {
                    arrayAdapterFilter(sportList)
                }
            }
        })

        send_kcal_to_db_IV.setOnClickListener {
            if(dayInfo.kcalGoal!! > 9998) dayInfo.kcalGoal = summaryVM.kcalGoalCalc(user) + kcal_sport_TV.text.toString().toInt()
            else dayInfo.kcalGoal = dayInfo.kcalGoal!! + kcal_sport_TV.text.toString().toInt()
            dayInfo.dayIndex = userAccountAgeInDays.toInt()
            if (dayInfo.kcalBurnt!! > 9998) dayInfo.kcalBurnt = kcal_sport_TV.text.toString().toInt()
            else dayInfo.kcalBurnt = dayInfo.kcalBurnt!! + kcal_sport_TV.text.toString().toInt()
            if(dayInfo.weight!! > 9999) dayInfo.weight = user.weight
            if (dayInfo.kcalEaten!! > 9998) dayInfo.kcalEaten = 0
            val list =  mutableListOf("${sport.name}, kcal Burned: ${kcal_sport_TV.text}")
            list.addAll(dayInfo.activitiesMade!!)
            dayInfo.activitiesMade = list
            summaryVM.updateDayInfo(curr_date_TV.text.toString(), dayInfo)
        }

        time_trained_ET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isNotBlank()) {
                    kcal_sport_TV.text = (s.toString().toInt() * (sport.kcal / 60)).toString()
                } else {
                    time_trained_ET.setText("0")
                    kcal_sport_TV.text = sport.kcal.toString()
                }
            }
        })

        return view
    }

    private fun arrayAdapterFilter(list: List<Sport>) {
        arrayAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            list)
        sport_list_LV.adapter = arrayAdapter
    }

    private fun getSportsList(): List<Sport> {
        val list = mutableListOf<Sport>()
        list.add(Sport("Wchodzenie po schodach", 948))
        list.add(Sport("Bieg (szybki - 5 min/km)", 780))
        list.add(Sport("Szybki marsz (7 km/h)", 293))
        list.add(Sport("Spacer", 228))
        list.add(Sport("Pływanie", 468))
        list.add(Sport("Pływanie", 468))
        list.add(Sport("Energiczny taniec", 366))
        list.add(Sport("Aerobik", 300))
        list.add(Sport("Boks", 558))
        list.add(Sport("Gra w kręgle", 204))
        list.add(Sport("Jazda konna", 258))
        list.add(Sport("Jazda na łyżwach", 426))
        list.add(Sport("Jazda na nartach", 438))
        list.add(Sport("Gra w kosza", 504))
        list.add(Sport("Odkurzanie", 135))
        list.add(Sport("Skakanka", 492))
        list.add(Sport("Tenis", 432))
        list.add(Sport("Brzuszki", 400))
        list.add(Sport("Rower stacjonarny", 422))
        list.add(Sport("Jazda na deskorolce", 318))
        list.add(Sport("EMS", 2001))
        list.add(Sport("Trening siłowy", 532))
        return list
    }

    private fun fragmentsReplacement(fragment: Fragment){
        val fragmentContainer = requireParentFragment().childFragmentManager.beginTransaction()
        fragmentContainer.replace(R.id.container_sports, fragment)
        fragmentContainer.commit()
    }
}

