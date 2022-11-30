package ayathe.project.dietapp.home.secondfragment

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
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ayathe.project.dietapp.DTO.DayInfo
import ayathe.project.dietapp.R
import ayathe.project.dietapp.adapters.MealAdapter
import ayathe.project.dietapp.adapters.OnMealClickListener
import ayathe.project.dietapp.DTO.Meal
import ayathe.project.dietapp.DTO.ProductFromJSON
import ayathe.project.dietapp.DTO.User
import ayathe.project.dietapp.home.homeactivity.HomeActivity
import ayathe.project.dietapp.home.secondfragment.eventinfo.MealInfo
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.current_day_fragment.*
import kotlinx.android.synthetic.main.current_day_fragment.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
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
    private var categoryList = mutableListOf("biznes", "edukacja", "sprawy domowe", "trening", "inne")
    private var choice: String? = null

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n", "SimpleDateFormat", "NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.current_day_fragment, container, false)
        val sdf = SimpleDateFormat("dd.MM.yyyy")
        val currentDate = sdf.format(Date())
        view.date_TV.text = currentDate
        var jsonElement = ProductFromJSON()
        var dayInfo = DayInfo()
        var userInfo = User()

        view.meal_gramss_ET.inputType = InputType.TYPE_NULL
        recyclerView = view.findViewById(R.id.meal_list_RV)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.setHasFixedSize(true)
        mealArrayList = arrayListOf()
        mealAdapter = MealAdapter(mealArrayList, this@DayFragment)
        recyclerView.adapter = mealAdapter
        val jsonString = mdVM.getJsonDataFromAsset(requireContext(), "json.json")
        val mealList = mdVM.dataClassFromJsonString(jsonString!!)

        arrayAdapterFilter(mealList, view)
        view.json_list_LV.onItemClickListener = AdapterView.OnItemClickListener { parent, v, position, id ->
            jsonElement = mealList.find { it.Nazwa.toString() == parent?.getItemAtPosition(position).toString() }!!
            Log.i("Clicked LV Item", jsonElement.Nazwa.toString())
            view.meal_gramss_ET.inputType = InputType.TYPE_CLASS_NUMBER

            view.current_meal_name_TV.text = jsonElement.Nazwa
            view.wegle_in_meal_TV.text = mdVM.nutritionalValuesCalc(100, jsonElement.Weglowodany!!.toInt()).toString()
            view.bialka_in_meal_TV.text = mdVM.nutritionalValuesCalc(100, jsonElement.Bialko!!.toInt()).toString()
            view.tluszcz_in_meal_TV.text =  mdVM.nutritionalValuesCalc(100, jsonElement.Tluszcz!!.toInt()).toString()
            view.kcal_in_meal_TV.text = mdVM.nutritionalValuesCalc(100, jsonElement.Kcal!!.toInt()).toString()
        }

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                mealAdapter.notifyDataSetChanged()
            }
        })

        view.meal_name_ET.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(queryText: Editable?) {
                if (queryText.toString() != ""){
                    arrayAdapterFilter(mealList.filter { it.Nazwa!!.lowercase().contains(queryText.toString().lowercase()) }, view)

                    mdVM.dayInfoReader(view.date_TV.text.toString()) {
                        if ( view.current_day_kcal_TVV.text.toString().toInt() != 9999 &&
                            view.kcal_goal_TV.text.toString().toInt() != 9999 &&
                            view.day_index_TV.text.toString().toInt() != 9999 ) {
                            view.current_day_kcal_TVV.text = it.kcalEaten.toString()
                            view.kcal_goal_TV.text = it.kcalGoal.toString()
                            view.day_index_TV.text = it.dayIndex.toString()
                            dayInfo = it
                        }
                    }
                } else {
                    arrayAdapterFilter(mealList, view)
                }
            }
        })

        mdVM.readUserData {
            userInfo = it
        }

        mdVM.dayInfoReader(view.date_TV.text.toString()) {
            if ( view.current_day_kcal_TVV.text.toString().toInt() != 9999 &&
                view.kcal_goal_TV.text.toString().toInt() != 9999 &&
                view.day_index_TV.text.toString().toInt() != 9999 ) {
                view.current_day_kcal_TVV.text = it.kcalEaten.toString()
                view.kcal_goal_TV.text = it.kcalGoal.toString()
                view.day_index_TV.text = it.dayIndex.toString()
                dayInfo = it
            }
        }


        mdVM.eventChangeListener(recyclerView, this, view.date_TV.text.toString())

        view.decrement_date_btn.setOnClickListener {
            val result = mdVM.dataParserFromDate(view.date_TV.text.toString())
            val period = Period.of(0,0,1)
            val parsed = LocalDate.parse(result)
            val r = parsed.minus(period)
            val final = mdVM.dataParserToDate(r.toString())

            mdVM.dayInfoReader(view.date_TV.text.toString()) {
                if ( view.current_day_kcal_TVV.text.toString().toInt() != 9999 &&
                    view.kcal_goal_TV.text.toString().toInt() != 9999 &&
                    view.day_index_TV.text.toString().toInt() != 9999 ) {
                    view.current_day_kcal_TVV.text = it.kcalEaten.toString()
                    view.kcal_goal_TV.text = it.kcalGoal.toString()
                    view.day_index_TV.text = it.dayIndex.toString()
                    dayInfo = it
                }
            }
            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
            if (LocalDate.parse(final, formatter).isBefore(LocalDate.parse(userInfo.startingDate, formatter)).not()) {
                date_TV.text = final
            } else {
                Toast.makeText(this@DayFragment.requireContext(),
                    "You can't search meals at date before your sign in date", Toast.LENGTH_LONG).show()
            }
            mdVM.dayInfoReader(view.date_TV.text.toString()) {
                if ( view.current_day_kcal_TVV.text.toString().toInt() != 9999 &&
                    view.kcal_goal_TV.text.toString().toInt() != 9999 &&
                    view.day_index_TV.text.toString().toInt() != 9999 ) {
                    view.current_day_kcal_TVV.text = it.kcalEaten.toString()
                    view.kcal_goal_TV.text = it.kcalGoal.toString()
                    view.day_index_TV.text = it.dayIndex.toString()
                    dayInfo = it
                }
            }
        }

        view.increment_date_btn.setOnClickListener {
            val result = mdVM.dataParserFromDate(view.date_TV.text.toString())
            val period = Period.of(0,0,1)
            val parsed = LocalDate.parse(result)
            val r = parsed.plus(period)
            val final = mdVM.dataParserToDate(r.toString())
            view.date_TV.text = final

                mdVM.dayInfoReader(view.date_TV.text.toString()) {
                if ( view.current_day_kcal_TVV.toString().toInt() != 9999 &&
                   view.kcal_goal_TV.toString().toInt() != 9999 &&
                   view.day_index_TV.text.toString().toInt() != 9999 ) {
                    view.current_day_kcal_TVV.text = it.kcalEaten.toString()
                    view.kcal_goal_TV.text = it.kcalGoal.toString()
                    view.day_index_TV.text = it.dayIndex.toString()
                    dayInfo = it
                    }
                }
        }

        view.date_TV.setOnClickListener {
                    val dpd = DatePickerDialog(requireContext(),
                        { _, mYear, mMonth, mDay ->
                            var mmDay = mDay.toString()
                            if(mDay < 10) mmDay = "0${mDay}"
                            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
                            if (LocalDate.parse("$mmDay.${mMonth+1}.$mYear", formatter).isBefore(LocalDate.parse(userInfo.startingDate, formatter)).not()) {
                                date_TV.text = "$mDay.${mMonth+1}.$mYear"
                            } else {
                                Toast.makeText(this@DayFragment.requireContext(),
                                "You can't search meals at date before your sign in date", Toast.LENGTH_LONG).show()
                            }
                        }, year, month, day)
                    dpd.show()
        }

        view.date_TV.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                mdVM.eventChangeListener(recyclerView, this@DayFragment, view.date_TV.text.toString())
                mealAdapter.notifyDataSetChanged()
            }

        })

        view.meal_gramss_ET.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if(mealList.map { it.Nazwa }.contains(jsonElement.Nazwa) && s.toString()!= "") {

                        view.wegle_in_meal_TV.text = mdVM.nutritionalValuesCalc(s.toString().toInt(), jsonElement.Weglowodany!!.toInt()).toString()
                        view.bialka_in_meal_TV.text = mdVM.nutritionalValuesCalc(s.toString().toInt(), jsonElement.Bialko!!.toInt()).toString()
                        view.tluszcz_in_meal_TV.text =  mdVM.nutritionalValuesCalc(s.toString().toInt(), jsonElement.Tluszcz!!.toInt()).toString()
                        view.kcal_in_meal_TV.text = mdVM.nutritionalValuesCalc(s.toString().toInt(), jsonElement.Kcal!!.toInt()).toString()
                    return
                    }else {
                    view.wegle_in_meal_TV.text = "0"
                        view.bialka_in_meal_TV.text = "0"
                        view.tluszcz_in_meal_TV.text = "0"
                    view.kcal_in_meal_TV.text = "0"
                    return
                }
            }
        })

        view.btn_add_meal.setOnClickListener {
            if (mealList.map { it.Nazwa }.contains(jsonElement.Nazwa.toString())
                && view.meal_gramss_ET.text.toString() != "grams"
                && view.meal_gramss_ET.text.toString().isEmpty().not()) {

                val caloriesPer100 = jsonElement.Kcal
                val caloriesPer1gram = caloriesPer100?.toDouble()!!.div(100)
                val grams = view.meal_gramss_ET.text.toString().toInt()
                val caloriesFromMeal = caloriesPer1gram.times(grams.toDouble()).toInt()

                val meal = Meal(
                    jsonElement.Nazwa.toString(),
                    view.date_TV.text.toString(),
                    grams,
                    caloriesFromMeal
                )
                CoroutineScope(Dispatchers.IO).launch {
                    val a = CoroutineScope(Dispatchers.IO).async {
                        mdVM.addMeal(meal, view.current_day_kcal_TVV.text.toString().toInt(), view.date_TV.text.toString())
                        mdVM.dayInfoReader(view.date_TV.text.toString()) {
                            if ( view.current_day_kcal_TVV.text.toString().toInt() != 9999 &&
                                view.kcal_goal_TV.text.toString().toInt() != 9999 &&
                                view.day_index_TV.text.toString().toInt() != 9999 ) {
                                view.current_day_kcal_TVV.text = it.kcalEaten.toString()
                                view.kcal_goal_TV.text = it.kcalGoal.toString()
                                view.day_index_TV.text = it.dayIndex.toString()
                                dayInfo = it
                            }
                        }
                        Log.i("added meal:", jsonElement.Nazwa.toString())
                        if (view.kcal_in_meal_TV.text == "0"){
                            view.current_day_kcal_TVV.text = caloriesFromMeal.toString()
                        } else {
                            view.current_day_kcal_TVV.text = (view.current_day_kcal_TVV.text.toString().toInt()
                                    + caloriesFromMeal).toString()
                        }
                    }
                    a.await()
                }
            }
            else {
                Toast.makeText(this@DayFragment.requireContext(),
                    "Please insert valid data", Toast.LENGTH_SHORT).show()
            }

        mealAdapter.notifyDataSetChanged()
        }

        return view
    }

    override fun onMealLongClick(meal: Meal, position: Int) {
        MaterialAlertDialogBuilder(requireContext()).setTitle(meal.name.toString()).setMessage("Are you sure you want to delete it?")
            .setNegativeButton("Keep it that way"){ _, _ -> }
            .setPositiveButton("Delete meal"){ _, _ ->
                mdVM.deleteMeal(meal.date!!, meal.name!!)
                (activity as HomeActivity).fragmentsReplacement(DayFragment())
            }.show()
        Toast.makeText(requireContext(), position.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun onMealClick(meal: Meal, position: Int) {
        val name = meal.name.toString()
        val date = meal.date.toString()
        val bundle = Bundle()
        val mealInfo = MealInfo()
        bundle.putString("MealName", name)
        bundle.putString("MealDate",date)
        mealInfo.arguments = bundle
        (activity as HomeActivity).fragmentsReplacement(mealInfo)
    }

    fun arrayAdapterFilter(list: List<ProductFromJSON>, view: View){
        arrayAdapter = ArrayAdapter(this@DayFragment.requireContext(), android.R.layout.simple_list_item_1, list.map { it.Nazwa })
        view.json_list_LV.adapter = arrayAdapter
    }

}