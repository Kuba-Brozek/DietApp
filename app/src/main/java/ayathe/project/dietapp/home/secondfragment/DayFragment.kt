package ayathe.project.dietapp.home.secondfragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ayathe.project.dietapp.R
import ayathe.project.dietapp.adapter.MealAdapter
import ayathe.project.dietapp.adapter.onMealClickListener
import ayathe.project.dietapp.DTO.Meal
import ayathe.project.dietapp.home.homeactivity.HomeActivity
import ayathe.project.dietapp.home.secondfragment.eventinfo.MealInfo
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.current_day_fragment.*
import kotlinx.android.synthetic.main.current_day_fragment.view.*
import java.lang.NullPointerException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.util.*


class DayFragment : Fragment(), onMealClickListener {


    private lateinit var mealArrayList: ArrayList<Meal>
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
    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.current_day_fragment, container, false)
        val sdf = SimpleDateFormat("dd.MM.yyyy")
        val currentDate = sdf.format(Date())
        view.date_TV.text = currentDate
        view.date_TV.setOnClickListener {
            val dpd = DatePickerDialog(requireContext(),
                { _, mYear, mMonth, mDay -> date_TV.text = "$mDay.${mMonth+1}.$mYear" }, year, month, day)
            dpd.show()
        }

        recyclerView = view.findViewById(R.id.event_list_RV)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        recyclerView.setHasFixedSize(true)
        mealArrayList = arrayListOf()
        recyclerView.adapter = MealAdapter(mealArrayList, this@DayFragment)
        val jsonString = mdVM.getJsonDataFromAsset(requireContext(), "json.json")
        val userList = mdVM.dataClassFromJsonString(jsonString!!)
        mdVM.eventChangeListener(recyclerView, this, view.date_TV.text.toString())
        view.decrement_date_btn.setOnClickListener {
            val result = mdVM.dataParserFromDate(view.date_TV.text.toString())
            val period = Period.of(0,0,1)
            val parsed = LocalDate.parse(result)
            val r = parsed.minus(period)
            val final = mdVM.dataParserToDate(r.toString())
            view.date_TV.text = final
        }
        view.increment_date_btn.setOnClickListener {
            val result = mdVM.dataParserFromDate(view.date_TV.text.toString())
            val period = Period.of(0,0,1)
            val parsed = LocalDate.parse(result)
            val r = parsed.plus(period)
            val final = mdVM.dataParserToDate(r.toString())
            view.date_TV.text = final
        }

        view.btn_submit_event.setOnClickListener {
            try{
                val jsonElement = userList.find { it.Nazwa.toString() == view.meal_name_ET.text.toString() }
                Log.i("asdqwe", jsonElement?.Nazwa.toString())
                val caloriesPer100 = jsonElement?.Kcal
                val caloriesPer1gram = caloriesPer100?.toDouble()!!.div(100)
                val grams = view.meal_gramss_ET.text.toString().toInt()
                val caloriesFromMeal = caloriesPer1gram.times(grams.toDouble()).toInt()

                val meal = Meal(
                    view.meal_name_ET.text.toString(),
                    currentDate,
                    grams,
                    caloriesFromMeal
                )
                mdVM.addMeal(meal)
            }
            catch (e:NullPointerException){

            }

        }
        return view
    }

    override fun onMealLongClick(meal: Meal, position: Int) {
        MaterialAlertDialogBuilder(requireContext()).setTitle("Alert").setMessage("Are you sure you want to delete event: ${meal.name.toString()}")
            .setNegativeButton("No, I am fine, thanks :D"){ _, _ -> }
            .setPositiveButton("Delete Event!"){ _, _ ->
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


}