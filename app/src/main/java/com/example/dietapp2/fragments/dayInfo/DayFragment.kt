package com.example.dietapp2.fragments.dayInfo

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dietapp2.DTO.DayInfo
import com.example.dietapp2.DTO.Meal
import com.example.dietapp2.DTO.ProductFromJSON
import com.example.dietapp2.DTO.User
import com.example.dietapp2.R
import com.example.dietapp2.adapters.MealAdapter
import com.example.dietapp2.adapters.OnMealClickListener
import com.example.dietapp2.databinding.CurrentDayFragmentBinding
import com.example.dietapp2.fragments.dayInfo.eventinfo.MealInfo
import com.example.dietapp2.fragments.homeactivity.HomeActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.util.*


class DayFragment : Fragment(), OnMealClickListener {


    private lateinit var mealArrayList: ArrayList<Meal>
    private lateinit var arrayAdapter: ArrayAdapter<*>
    private lateinit var mealAdapter: MealAdapter
    private lateinit var recyclerView: RecyclerView
    private val mdVM by viewModels<MealsDaysViewModel>()
    private val calendar = Calendar.getInstance()
    private val year = calendar.get(Calendar.YEAR)
    private val month = calendar.get(Calendar.MONTH)
    private val day = calendar.get(Calendar.DAY_OF_MONTH)
    private var categoryList =
        mutableListOf("biznes", "edukacja", "sprawy domowe", "trening", "inne")
    private var choice: String? = null
    private lateinit var date_TV: TextView
    private lateinit var meal_gramss_ET: EditText
    private lateinit var json_list_LV: ListView
    private lateinit var wegle_in_meal_TV: TextView
    private lateinit var bialka_in_meal_TV: TextView
    private lateinit var tluszcz_in_meal_TV: TextView
    private lateinit var kcal_in_meal_TV: TextView
    private lateinit var current_meal_name_TV: TextView
    private lateinit var meal_name_ET: EditText
    private lateinit var current_day_kcal_TVV: TextView
    private lateinit var kcal_goal_TV: TextView
    private lateinit var day_index_TV: TextView
    private lateinit var curr_weight_TV: TextView
    private lateinit var decrement_date_btn: AppCompatButton
    private lateinit var increment_date_btn: AppCompatButton
    private lateinit var btn_add_meal: AppCompatButton

    @SuppressLint("SetTextI18n", "SimpleDateFormat", "NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        val view: View = inflater.inflate(R.layout.current_day_fragment, container, false)
        date_TV = view.findViewById(R.id.date_TV)
        meal_gramss_ET = view.findViewById(R.id.meal_gramss_ET)
        json_list_LV = view.findViewById(R.id.json_list_LV)
        wegle_in_meal_TV = view.findViewById(R.id.wegle_in_meal_TV)
        bialka_in_meal_TV = view.findViewById(R.id.bialka_in_meal_TV)
        tluszcz_in_meal_TV = view.findViewById(R.id.tluszcz_in_meal_TV)
        kcal_in_meal_TV = view.findViewById(R.id.kcal_in_meal_TV)
        current_meal_name_TV = view.findViewById(R.id.current_meal_name_TV)
        meal_name_ET = view.findViewById(R.id.meal_name_ET)
        current_day_kcal_TVV = view.findViewById(R.id.current_day_kcal_TVV)
        kcal_goal_TV = view.findViewById(R.id.kcal_goal_TV)
        day_index_TV = view.findViewById(R.id.day_index_TV)
        curr_weight_TV = view.findViewById(R.id.curr_weight_TV)
        decrement_date_btn = view.findViewById(R.id.decrement_date_btn)
        increment_date_btn = view.findViewById(R.id.increment_date_btn)
        btn_add_meal = view.findViewById(R.id.btn_add_meal)

        val sdf = SimpleDateFormat("dd.MM.yyyy")

        val currentDate = sdf.format(Date())
        date_TV.text = currentDate
        var jsonElement = ProductFromJSON()
        var dayInfo = DayInfo()
        var userInfo = User()

        meal_gramss_ET.inputType = InputType.TYPE_NULL
        recyclerView = view.findViewById(R.id.meal_list_RV)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.setHasFixedSize(true)
        mealArrayList = arrayListOf()
        mealAdapter = MealAdapter(mealArrayList, this@DayFragment)
        recyclerView.adapter = mealAdapter
        val jsonString = mdVM.getJsonDataFromAsset(this@DayFragment.requireContext(), "json.json")
        val mealList = mdVM.dataClassFromJsonString(jsonString!!)

        arrayAdapterFilter(mealList, view)
        json_list_LV.onItemClickListener =
            AdapterView.OnItemClickListener { parent, v, position, id ->
                jsonElement = mealList.find {
                    it.name.toString() == parent?.getItemAtPosition(position).toString()
                }!!
                Log.i("Clicked LV Item", jsonElement.name.toString())
                meal_gramss_ET.inputType = InputType.TYPE_CLASS_NUMBER

                current_meal_name_TV.text = jsonElement.name
                meal_gramss_ET.setText("100")
                wegle_in_meal_TV.text =
                    mdVM.nutritionalValuesCalc(100, jsonElement.carbs!!.toInt()).toString()
                bialka_in_meal_TV.text =
                    mdVM.nutritionalValuesCalc(100, jsonElement.protein!!.toInt()).toString()
                tluszcz_in_meal_TV.text =
                    mdVM.nutritionalValuesCalc(100, jsonElement.fat!!.toInt()).toString()
                kcal_in_meal_TV.text =
                    mdVM.nutritionalValuesCalc(100, jsonElement.kcal!!.toInt()).toString()
            }

        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    mealAdapter.notifyDataSetChanged()
                }
            })

        meal_name_ET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(queryText: Editable?) {
                if (queryText.toString() != "") {
                    arrayAdapterFilter(mealList.filter {
                        it.name!!.lowercase().contains(queryText.toString().lowercase())
                    }, view)
                } else {
                    arrayAdapterFilter(mealList, view)
                }
            }
        })



        mdVM.dayInfoReader(date_TV.text.toString()) { dayInfoReaded ->
            mdVM.readUserData {
                userInfo = it
            }
            if (dayInfoReaded.kcalEaten != 9999 &&
                dayInfoReaded.kcalGoal != 9999 &&
                dayInfoReaded.dayIndex != 9999
            ) {
                current_day_kcal_TVV.text = dayInfoReaded.kcalEaten.toString()
                kcal_goal_TV.text = dayInfoReaded.kcalGoal.toString()
                day_index_TV.text = dayInfoReaded.dayIndex.toString()
                curr_weight_TV.text = dayInfoReaded.weight.toString()
                dayInfo = dayInfoReaded
            } else {
                current_day_kcal_TVV.text = "KCAL"
                kcal_goal_TV.text = "GOAL"
                mdVM.readUserData {
                    val x = mdVM.dayIndexCalc(
                        mdVM.getLocalDateFromString(it.startingDate!!, "dd.MM.yyyy"),
                        mdVM.getLocalDateFromString(date_TV.text.toString(), "dd.MM.yyyy")
                    ).toString()
                    day_index_TV.text = x
                }
                if (day_index_TV.text == "") {
                    day_index_TV.text = "DAY"
                }
            }
        }

        mdVM.eventChangeListener(recyclerView, this, date_TV.text.toString())

        decrement_date_btn.setOnClickListener {
            val result = mdVM.dataParserFromDate(date_TV.text.toString())
            val period = Period.of(0, 0, 1)
            val parsed = LocalDate.parse(result)
            val r = parsed.minus(period)
            val final = mdVM.dataParserToDate(r.toString())

            date_TV.text = final
        }

        increment_date_btn.setOnClickListener {
            val result = mdVM.dataParserFromDate(date_TV.text.toString())
            val period = Period.of(0, 0, 1)
            val parsed = LocalDate.parse(result)
            val r = parsed.plus(period)
            val final = mdVM.dataParserToDate(r.toString())
            date_TV.text = final
        }

        date_TV.setOnClickListener {
            val dpd = DatePickerDialog(this@DayFragment.requireContext(),
                { _, mYear, mMonth, mDay ->
                    var stringMonth = (mMonth + 1).toString()
                    var mmDay = mDay.toString()
                    if (stringMonth.toInt() < 10) {
                        stringMonth = "0${stringMonth}"
                    }
                    if (mmDay.toInt() < 10) {
                        mmDay = "0$mmDay"
                    }
                    date_TV.text = "$mmDay.$stringMonth.$mYear"
                }, year, month, day
            )
            dpd.show()
        }

        date_TV.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                mdVM.eventChangeListener(recyclerView, this@DayFragment, date_TV.text.toString())
                mealAdapter.notifyDataSetChanged()

                mdVM.dayInfoReader(date_TV.text.toString()) {

                    if (it.kcalEaten != 9999 &&
                        it.kcalGoal != 9999 &&
                        it.dayIndex != 9999
                    ) {
                        current_day_kcal_TVV.text = it.kcalEaten.toString()
                        kcal_goal_TV.text = it.kcalGoal.toString()
                        day_index_TV.text = mdVM.dayIndexCalc(
                            mdVM.getLocalDateFromString(userInfo.startingDate!!, "dd.MM.yyyy"),
                            mdVM.getLocalDateFromString(date_TV.text.toString(), "dd.MM.yyyy")
                        ).toString()
                        dayInfo = it
                    } else {
                        current_day_kcal_TVV.text = "KCAL"
                        kcal_goal_TV.text = "GOAL"
                        day_index_TV.text = mdVM.dayIndexCalc(
                            mdVM.getLocalDateFromString(userInfo.startingDate!!, "dd.MM.yyyy"),
                            mdVM.getLocalDateFromString(date_TV.text.toString(), "dd.MM.yyyy")
                        ).toString()
                    }
                }
            }

        })

        meal_gramss_ET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(grams: Editable?) {
                if (mealList.map { it.name }.contains(jsonElement.name) && grams.toString() != "") {

                    wegle_in_meal_TV.text = mdVM.nutritionalValuesCalc(
                        grams.toString().toInt(),
                        jsonElement.carbs!!.toInt()
                    ).toString()
                    bialka_in_meal_TV.text = mdVM.nutritionalValuesCalc(
                        grams.toString().toInt(),
                        jsonElement.protein!!.toInt()
                    ).toString()
                    tluszcz_in_meal_TV.text = mdVM.nutritionalValuesCalc(
                        grams.toString().toInt(),
                        jsonElement.fat!!.toInt()
                    ).toString()
                    kcal_in_meal_TV.text = mdVM.nutritionalValuesCalc(
                        grams.toString().toInt(),
                        jsonElement.kcal!!.toInt()
                    ).toString()
                    return
                } else {
                    wegle_in_meal_TV.text = "0"
                    bialka_in_meal_TV.text = "0"
                    tluszcz_in_meal_TV.text = "0"
                    kcal_in_meal_TV.text = "0"
                    return
                }
            }
        })

        btn_add_meal.setOnClickListener {
            if (meal_gramss_ET.text.isNotEmpty()) {
                if (mealList.map { it.name }.contains(jsonElement.name.toString())
                    && meal_gramss_ET.text.toString() != "grams"
                    && meal_gramss_ET.text.toString().isEmpty().not()
                ) {

                    val grams = meal_gramss_ET.text.toString().toInt()
                    val caloriesFromMeal =
                        mdVM.nutritionalValuesCalc(grams, jsonElement.kcal!!.toInt())

                    val meal = Meal(
                        jsonElement.name.toString(),
                        date_TV.text.toString(),
                        grams,
                        caloriesFromMeal
                    )
                    CoroutineScope(Dispatchers.IO).launch {
                        val a = CoroutineScope(Dispatchers.IO).async {
                            val kcalEaten =
                                if (current_day_kcal_TVV.text.toString() == "KCAL") 0 else current_day_kcal_TVV.text.toString()
                                    .toInt()
                            mdVM.addMeal(meal, kcalEaten, date_TV.text.toString()) {
                                current_day_kcal_TVV.text = it.kcalEaten.toString()
                                kcal_goal_TV.text = it.kcalGoal.toString()
                                day_index_TV.text = mdVM.dayIndexCalc(
                                    mdVM.getLocalDateFromString(
                                        userInfo.startingDate!!,
                                        "dd.MM.yyyy"
                                    ),
                                    mdVM.getLocalDateFromString(
                                        date_TV.text.toString(),
                                        "dd.MM.yyyy"
                                    )
                                ).toString()
                                dayInfo = it
                                Log.i("added meal:", jsonElement.name.toString())
                            }
                        }
                        a.await()
                    }
                } else {
                    Toast.makeText(
                        this@DayFragment.requireContext(),
                        "Please insert valid data", Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    this@DayFragment.requireContext(),
                    "Please enter grams or choose meal type",
                    Toast.LENGTH_LONG
                ).show()
            }
            mealAdapter.notifyDataSetChanged()
        }

        return view
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onMealLongClick(meal: Meal, position: Int) {
        MaterialAlertDialogBuilder(requireContext()).setTitle(meal.name.toString())
            .setMessage("Are you sure you want to delete meal?")
            .setNegativeButton("Keep it that way") { _, _ -> }
            .setPositiveButton("Delete meal") { _, _ ->
                mdVM.deleteMeal(meal) {
                    date_TV.text = it.date
                    current_day_kcal_TVV.text = it.kcalEaten.toString()
                }

            }.show()
        Toast.makeText(requireContext(), meal.name, Toast.LENGTH_SHORT).show()
    }

    override fun onMealClick(meal: Meal, position: Int) {
        val name = meal.name.toString()
        val date = meal.date.toString()
        val bundle = Bundle()
        val mealInfo = MealInfo()
        bundle.putString("MealName", name)
        bundle.putString("MealDate", date)
        mealInfo.arguments = bundle
        (activity as HomeActivity).fragmentsReplacement(mealInfo)
    }

    fun arrayAdapterFilter(list: List<ProductFromJSON>, view: View) {
        arrayAdapter = ArrayAdapter(
            this@DayFragment.requireContext(),
            android.R.layout.simple_list_item_1,
            list.map { it.name })
        json_list_LV.adapter = arrayAdapter
    }

}