package ayathe.project.dietapp.fragments.secondfragment

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
import ayathe.project.dietapp.fragments.homeactivity.HomeActivity
import ayathe.project.dietapp.fragments.secondfragment.eventinfo.MealInfo
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
        val jsonString = mdVM.getJsonDataFromAsset(this@DayFragment.requireContext(), "json.json")
        val mealList = mdVM.dataClassFromJsonString(jsonString!!)

        arrayAdapterFilter(mealList, view)
        view.json_list_LV.onItemClickListener = AdapterView.OnItemClickListener { parent, v, position, id ->
            jsonElement = mealList.find { it.name.toString() == parent?.getItemAtPosition(position).toString() }!!
            Log.i("Clicked LV Item", jsonElement.name.toString())
            view.meal_gramss_ET.inputType = InputType.TYPE_CLASS_NUMBER

            view.current_meal_name_TV.text = jsonElement.name
            view.wegle_in_meal_TV.text = mdVM.nutritionalValuesCalc(100, jsonElement.carbs!!.toInt()).toString()
            view.bialka_in_meal_TV.text = mdVM.nutritionalValuesCalc(100, jsonElement.protein!!.toInt()).toString()
            view.tluszcz_in_meal_TV.text =  mdVM.nutritionalValuesCalc(100, jsonElement.fat!!.toInt()).toString()
            view.kcal_in_meal_TV.text = mdVM.nutritionalValuesCalc(100, jsonElement.kcal!!.toInt()).toString()
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
                    arrayAdapterFilter(mealList.filter { it.name!!.lowercase().contains(queryText.toString().lowercase()) }, view)
                } else {
                    arrayAdapterFilter(mealList, view)
                }
            }
        })

        mdVM.readUserData {
            userInfo = it
        }

        mdVM.dayInfoReader(view.date_TV.text.toString()) {
            if ( it.kcalEaten != 9999 &&
                it.kcalGoal != 9999 &&
                it.dayIndex != 9999 ) {
                view.current_day_kcal_TVV.text = it.kcalEaten.toString()
                view.kcal_goal_TV.text = it.kcalGoal.toString()
                view.day_index_TV.text = it.dayIndex.toString()
                dayInfo = it
            } else {
                view.current_day_kcal_TVV.text = "KCAL"
                view.kcal_goal_TV.text = "GOAL"
                view.day_index_TV.text = mdVM.dayIndexCalc(
                    mdVM.getLocalDateFromString(userInfo.startingDate!!, "dd.MM.yyyy"),
                    mdVM.getLocalDateFromString(view.date_TV.text.toString(), "dd.MM.yyyy")
                ).toString()
            }
        }

        mdVM.eventChangeListener(recyclerView, this, view.date_TV.text.toString())

        view.decrement_date_btn.setOnClickListener {
            val result = mdVM.dataParserFromDate(view.date_TV.text.toString())
            val period = Period.of(0,0,1)
            val parsed = LocalDate.parse(result)
            val r = parsed.minus(period)
            val final = mdVM.dataParserToDate(r.toString())

            date_TV.text = final
        }

        view.increment_date_btn.setOnClickListener {
            val result = mdVM.dataParserFromDate(view.date_TV.text.toString())
            val period = Period.of(0,0,1)
            val parsed = LocalDate.parse(result)
            val r = parsed.plus(period)
            val final = mdVM.dataParserToDate(r.toString())
            view.date_TV.text = final
        }

        view.date_TV.setOnClickListener {
            val dpd = DatePickerDialog(this@DayFragment.requireContext(),
                { _, mYear, mMonth, mDay ->
                    var mmMonth = mMonth.toString()
                    var mmDay = mDay.toString()
                    if (mmMonth.toInt() < 10){
                        mmMonth = "0$mmMonth"
                    }
                    if (mmDay.toInt() < 10){
                        mmDay = "0$mmDay"
                    }
                    date_TV.text = "${mmDay.toInt()}.${mmMonth.toInt()+1}.$mYear"
                }, year, month, day)
            dpd.show()
        }

        view.date_TV.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                mdVM.eventChangeListener(recyclerView, this@DayFragment, view.date_TV.text.toString())
                mealAdapter.notifyDataSetChanged()

                mdVM.dayInfoReader(view.date_TV.text.toString()) {

                    if ( it.kcalEaten != 9999 &&
                        it.kcalGoal != 9999 &&
                        it.dayIndex != 9999 ) {
                        view.current_day_kcal_TVV.text = it.kcalEaten.toString()
                        view.kcal_goal_TV.text = it.kcalGoal.toString()
                        view.day_index_TV.text = mdVM.dayIndexCalc(
                            mdVM.getLocalDateFromString(userInfo.startingDate!!, "dd.MM.yyyy"),
                            mdVM.getLocalDateFromString(view.date_TV.text.toString(), "dd.MM.yyyy")
                        ).toString()
                        dayInfo = it
                    } else {
                        view.current_day_kcal_TVV.text = "KCAL"
                        view.kcal_goal_TV.text = "GOAL"
                        view.day_index_TV.text = mdVM.dayIndexCalc(
                            mdVM.getLocalDateFromString(userInfo.startingDate!!, "dd.MM.yyyy"),
                            mdVM.getLocalDateFromString(view.date_TV.text.toString(), "dd.MM.yyyy")
                        ).toString()
                    }
                }
            }

        })

        view.meal_gramss_ET.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(grams: Editable?) {
                if(mealList.map { it.name }.contains(jsonElement.name) && grams.toString()!= "") {

                    view.wegle_in_meal_TV.text = mdVM.nutritionalValuesCalc(grams.toString().toInt(), jsonElement.carbs!!.toInt()).toString()
                    view.bialka_in_meal_TV.text = mdVM.nutritionalValuesCalc(grams.toString().toInt(), jsonElement.protein!!.toInt()).toString()
                    view.tluszcz_in_meal_TV.text =  mdVM.nutritionalValuesCalc(grams.toString().toInt(), jsonElement.fat!!.toInt()).toString()
                    view.kcal_in_meal_TV.text = mdVM.nutritionalValuesCalc(grams.toString().toInt(), jsonElement.kcal!!.toInt()).toString()
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
            if (view.meal_gramss_ET.text.isNotEmpty()){
                if (mealList.map { it.name }.contains(jsonElement.name.toString())
                    && view.meal_gramss_ET.text.toString() != "grams"
                    && view.meal_gramss_ET.text.toString().isEmpty().not()) {

                    val grams = view.meal_gramss_ET.text.toString().toInt()
                    val caloriesFromMeal = mdVM.nutritionalValuesCalc(grams, jsonElement.kcal!!.toInt())

                    val meal = Meal(
                        jsonElement.name.toString(),
                        view.date_TV.text.toString(),
                        grams,
                        caloriesFromMeal
                    )
                    CoroutineScope(Dispatchers.IO).launch {
                        val a = CoroutineScope(Dispatchers.IO).async {
                            val kcalEaten = if (view.current_day_kcal_TVV.text.toString() == "KCAL") 0 else view.current_day_kcal_TVV.text.toString().toInt()
                            mdVM.addMeal(meal, kcalEaten, view.date_TV.text.toString())
                            val date = view.date_TV.text.toString()
                            mdVM.dayInfoReader(date) {
                                if ( it.kcalEaten != 9999 &&
                                    it.kcalGoal != 9999 &&
                                    it.dayIndex != 9999 ) {
                                    view.current_day_kcal_TVV.text = (it.kcalEaten?.plus(
                                        caloriesFromMeal
                                    )).toString()
                                    view.kcal_goal_TV.text = it.kcalGoal.toString()
                                    view.day_index_TV.text = mdVM.dayIndexCalc(
                                        mdVM.getLocalDateFromString(userInfo.startingDate!!, "dd.MM.yyyy"),
                                        mdVM.getLocalDateFromString(view.date_TV.text.toString(), "dd.MM.yyyy")
                                    ).toString()
                                    dayInfo = it
                                    Log.i("added meal:", jsonElement.name.toString())
                                } else {
                                    view.date_TV.text = date
                                }
                            }
                        }
                        a.await()
                    }
                }
                else {
                    Toast.makeText(this@DayFragment.requireContext(),
                        "Please insert valid data", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this@DayFragment.requireContext(), "Please enter grams or choose meal type", Toast.LENGTH_LONG).show()
            }
            mealAdapter.notifyDataSetChanged()
        }

        return view
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onMealLongClick(meal: Meal, position: Int) {
        MaterialAlertDialogBuilder(requireContext()).setTitle(meal.name.toString()).setMessage("Are you sure you want to delete meal?")
            .setNegativeButton("Keep it that way"){ _, _ -> }
            .setPositiveButton("Delete meal"){ _, _ ->
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
        bundle.putString("MealDate",date)
        mealInfo.arguments = bundle
        (activity as HomeActivity).fragmentsReplacement(mealInfo)
    }

    fun arrayAdapterFilter(list: List<ProductFromJSON>, view: View){
        arrayAdapter = ArrayAdapter(this@DayFragment.requireContext(), android.R.layout.simple_list_item_1, list.map { it.name })
        view.json_list_LV.adapter = arrayAdapter
    }

}