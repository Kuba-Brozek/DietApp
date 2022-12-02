package ayathe.project.dietapp.home.secondfragment.eventinfo

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import ayathe.project.dietapp.R
import ayathe.project.dietapp.DTO.Meal
import ayathe.project.dietapp.home.homeactivity.HomeActivity
import ayathe.project.dietapp.home.secondfragment.DayFragment
import ayathe.project.dietapp.home.secondfragment.MealsDaysViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.current_day_fragment.*
import kotlinx.android.synthetic.main.fragment_event_info.*
import kotlinx.android.synthetic.main.fragment_event_info.view.*
import java.util.*


class MealInfo : Fragment() {
    private val secondVM by viewModels<MealsDaysViewModel>()
    private val calendar = Calendar.getInstance()
    private val year = calendar.get(Calendar.YEAR)
    private val month = calendar.get(Calendar.MONTH)
    private val day = calendar.get(Calendar.DAY_OF_MONTH)

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_event_info, container, false)
        val args = this.arguments
        var meal = Meal()
        val mealName = args?.getString("MealName")
        val mealDate = args?.getString("MealDate")
        Log.i("rty", mealName.toString())

        secondVM.showMealInfo(mealName.toString(), mealDate.toString()) {
            meal = it
            view.meal_name.setText(it.name.toString())
            view.meal_date.text = it.date.toString()
            view.meal_grams_ET.setText(it.grams.toString())
            view.meal_kcal.text = it.cals.toString()
        }

        val jsonString = secondVM.getJsonDataFromAsset(requireContext(), "json.json")
        val userList = secondVM.dataClassFromJsonString(jsonString!!)

        view.btn_exit.setOnClickListener {
            (activity as HomeActivity).fragmentsReplacement(DayFragment())
        }
        val jsonElement = userList.find { it.Nazwa == view.meal_name.text.toString() }
        val caloriesPer100 = jsonElement?.Kcal

        view.btn_calculate_kcal.setOnClickListener {
            val grams = view.meal_grams_ET.text.toString()
            val result = secondVM.nutritionalValuesCalc(caloriesPer100 ?: 100, grams.toInt())
            meal_kcal.text = result.toString()
        }

        view.btn_delete.setOnClickListener {

            MaterialAlertDialogBuilder(requireContext()).setTitle("Alert").setMessage("Are you sure you want to delete event: ${mealName.toString()}")
                .setNegativeButton("No, I am fine, thanks :D"){ _, _ -> }
                .setPositiveButton("Delete Event!"){ _, _ ->
                    secondVM.deleteMeal(meal) { }
                    (activity as HomeActivity).fragmentsReplacement(DayFragment())
                }.show()
        }

        view.btn_save.setOnClickListener {
            val finalElement = userList.find { it.Nazwa == view.meal_name.text.toString() }
            val kcalPer100gram = finalElement?.Kcal
            val grams = view.meal_grams_ET.text.toString().toInt()
            val caloriesFromMeal = secondVM.nutritionalValuesCalc(kcalPer100gram!!, grams)
            meal = Meal(
                view.meal_name.text.toString(),
                view.meal_date.text.toString(),
                grams,
                caloriesFromMeal
            )
            try {
                secondVM.dayInfoReader(date_TV.text.toString()) {
                    secondVM.addMeal(meal, it.kcalEaten!! , date_TV.text.toString())
                }
            } catch (e: Exception){

            }

        }

        return view
    }


}