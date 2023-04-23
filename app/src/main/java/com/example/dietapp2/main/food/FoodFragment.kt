package com.example.dietapp2.main.food

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dietapp2.DTO.DayInfo
import com.example.dietapp2.DTO.Meal
import com.example.dietapp2.DTO.ProductFromJSON
import com.example.dietapp2.DTO.User
import com.example.dietapp2.DTO.UserDetails
import com.example.dietapp2.R
import com.example.dietapp2.adapters.MealAdapter
import com.example.dietapp2.adapters.OnMealClickListener
import com.example.dietapp2.main.homeactivity.HomeActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.util.*


class FoodFragment(private val dt: String = "") : Fragment(), OnMealClickListener {


    private lateinit var mealArrayList: ArrayList<Meal>
    private lateinit var arrayAdapter: ArrayAdapter<*>
    private lateinit var mealAdapter: MealAdapter
    private lateinit var recyclerView: RecyclerView
    private val foodViewModel by viewModels<FoodViewModel>()
    private val calendar = Calendar.getInstance()
    private val year = calendar.get(Calendar.YEAR)
    private val month = calendar.get(Calendar.MONTH)
    private val day = calendar.get(Calendar.DAY_OF_MONTH)
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


        val view: View = inflater.inflate(R.layout.fragment_food, container, false)
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
        if(dt.isEmpty()) {
            date_TV.text = currentDate
        } else {
            date_TV.text = dt
        }

        var jsonElement = ProductFromJSON()
        var dayInfo = DayInfo()
        var userInfo = User()
        var userDetails = UserDetails()

        meal_gramss_ET.inputType = InputType.TYPE_NULL
        recyclerView = view.findViewById(R.id.meal_list_RV)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.setHasFixedSize(true)
        mealArrayList = arrayListOf()
        mealAdapter = MealAdapter(mealArrayList, this@FoodFragment)
        recyclerView.adapter = mealAdapter
        val jsonString = foodViewModel.getJsonDataFromAsset(this@FoodFragment.requireContext(), "json.json")
        val mealList = foodViewModel.dataClassFromJsonString(jsonString!!)

        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        arrayAdapterFilter(mealList)
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
                    foodViewModel.nutritionalValuesCalc(100, jsonElement.carbs!!.toInt()).toString()
                bialka_in_meal_TV.text =
                    foodViewModel.nutritionalValuesCalc(100, jsonElement.protein!!.toInt()).toString()
                tluszcz_in_meal_TV.text =
                    foodViewModel.nutritionalValuesCalc(100, jsonElement.fat!!.toInt()).toString()
                kcal_in_meal_TV.text =
                    foodViewModel.nutritionalValuesCalc(100, jsonElement.kcal!!.toInt()).toString()
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
                    })
                } else {
                    arrayAdapterFilter(mealList)
                }
            }
        })



        foodViewModel.dayInfoReader(date_TV.text.toString()) { currentDayInfo ->
            dayInfo = currentDayInfo
            foodViewModel.readUserData { user ->
                userInfo = user
                foodViewModel.readUserDetails { userDetailsFromDB ->
                    userDetails = userDetailsFromDB
                    if (dayInfo.kcalEaten!! > 9998) dayInfo.kcalEaten = 0
                    if (dayInfo.kcalGoal!! > 9998) dayInfo.kcalGoal = foodViewModel.kcalGoalCalc(userInfo)
                    if (dayInfo.dayIndex!! > 9998) dayInfo.dayIndex = foodViewModel.dayIndexCalc(
                        foodViewModel.getLocalDateFromString(userInfo.startingDate!!, "dd.MM.yyyy"),
                        foodViewModel.getLocalDateFromString(date_TV.text.toString(), "dd.MM.yyyy"))
                    current_day_kcal_TVV.text = dayInfo.kcalEaten.toString()
                    kcal_goal_TV.text = dayInfo.kcalGoal.toString()
                    day_index_TV.text = dayInfo.dayIndex.toString()
                    curr_weight_TV.text = if (dayInfo.weight.toString() == "0") userDetails.weight.toString()
                    else userDetails.weight.toString()
                }
            }
        }

        foodViewModel.eventChangeListener(recyclerView, this, date_TV.text.toString())

        decrement_date_btn.setOnClickListener {
            val result = foodViewModel.dataParserFromDate(date_TV.text.toString())
            val period = Period.of(0, 0, 1)
            val parsed = LocalDate.parse(result)
            val r = parsed.minus(period)
            val final = foodViewModel.dataParserToDate(r.toString())

            date_TV.text = final
        }

        increment_date_btn.setOnClickListener {
            val result = foodViewModel.dataParserFromDate(date_TV.text.toString())
            val period = Period.of(0, 0, 1)
            val parsed = LocalDate.parse(result)
            val r = parsed.plus(period)
            val final = foodViewModel.dataParserToDate(r.toString())
            date_TV.text = final
        }

        date_TV.setOnClickListener {
            val dpd = DatePickerDialog(this@FoodFragment.requireContext(),
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
                foodViewModel.eventChangeListener(recyclerView, this@FoodFragment, date_TV.text.toString())
                mealAdapter.notifyDataSetChanged()

                foodViewModel.dayInfoReader(date_TV.text.toString()) {
                    dayInfo = it
                    if (dayInfo.kcalEaten != 9999 ||
                        dayInfo.kcalGoal != 9999 ||
                        dayInfo.dayIndex != 9999
                    ) {
                        current_day_kcal_TVV.text = dayInfo.kcalEaten.toString()
                        kcal_goal_TV.text = dayInfo.kcalGoal.toString()
                        day_index_TV.text = foodViewModel.dayIndexCalc(
                            foodViewModel.getLocalDateFromString(userInfo.startingDate!!, "dd.MM.yyyy"),
                            foodViewModel.getLocalDateFromString(date_TV.text.toString(), "dd.MM.yyyy")
                        ).toString()

                    } else {
                        current_day_kcal_TVV.text = "---"
                        kcal_goal_TV.text = "---"
                        day_index_TV.text = foodViewModel.dayIndexCalc(
                            foodViewModel.getLocalDateFromString(userInfo.startingDate!!, "dd.MM.yyyy"),
                            foodViewModel.getLocalDateFromString(date_TV.text.toString(), "dd.MM.yyyy")
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

                    wegle_in_meal_TV.text = foodViewModel.nutritionalValuesCalc(
                        grams.toString().toInt(),
                        jsonElement.carbs!!.toInt()
                    ).toString()
                    bialka_in_meal_TV.text = foodViewModel.nutritionalValuesCalc(
                        grams.toString().toInt(),
                        jsonElement.protein!!.toInt()
                    ).toString()
                    tluszcz_in_meal_TV.text = foodViewModel.nutritionalValuesCalc(
                        grams.toString().toInt(),
                        jsonElement.fat!!.toInt()
                    ).toString()
                    kcal_in_meal_TV.text = foodViewModel.nutritionalValuesCalc(
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
                        foodViewModel.nutritionalValuesCalc(grams, jsonElement.kcal!!.toInt())

                    val meal = Meal(
                        jsonElement.name.toString(),
                        date_TV.text.toString(),
                        grams,
                        caloriesFromMeal
                    )
                    CoroutineScope(Dispatchers.IO).launch {
                        val a = CoroutineScope(Dispatchers.IO).async {
                            val kcalEaten =
                                if (current_day_kcal_TVV.text.toString() == "---") 0 else current_day_kcal_TVV.text.toString()
                                    .toInt()
                            foodViewModel.addMeal(meal, kcalEaten, meal.date!!) {
                                dayInfo = it
                                Log.i("added meal:", jsonElement.name.toString())
                                (activity as HomeActivity).fragmentsReplacement(FoodFragment(meal.date!!))
                            }
                        }
                        a.await()
                    }
                } else {
                    Toast.makeText(
                        this@FoodFragment.requireContext(),
                        "Please insert valid data", Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    this@FoodFragment.requireContext(),
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
                foodViewModel.deleteMeal(meal) {
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
        val mealInfoFragment = MealInfoFragment()
        bundle.putString("MealName", name)
        bundle.putString("MealDate", date)
        mealInfoFragment.arguments = bundle
        (activity as HomeActivity).fragmentsReplacement(mealInfoFragment)
    }

    fun arrayAdapterFilter(list: List<ProductFromJSON>) {
        arrayAdapter = ArrayAdapter(
            this@FoodFragment.requireContext(),
            android.R.layout.simple_list_item_1,
            list.map { it.name })
        json_list_LV.adapter = arrayAdapter
    }

}