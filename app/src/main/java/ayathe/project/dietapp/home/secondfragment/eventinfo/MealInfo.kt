package ayathe.project.dietapp.home.secondfragment.eventinfo

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import ayathe.project.dietapp.R
import ayathe.project.dietapp.DTO.Meal
import ayathe.project.dietapp.home.homeactivity.HomeActivity
import ayathe.project.dietapp.home.secondfragment.DayFragment
import ayathe.project.dietapp.home.secondfragment.MealsDaysViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_event_info.*
import kotlinx.android.synthetic.main.fragment_event_info.view.*
import java.util.*


class MealInfo : Fragment() {
    private val secondVM by viewModels<MealsDaysViewModel>()
    private val calendar = Calendar.getInstance()
    private val year = calendar.get(Calendar.YEAR)
    private val month = calendar.get(Calendar.MONTH)
    private val day = calendar.get(Calendar.DAY_OF_MONTH)

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_event_info, container, false)
        val args = this.arguments
        val mealName = args?.getString("MealName")
        val mealDate = args?.getString("MealDate")
        Log.i("rty", mealName.toString())

        view.meal_name.setText(mealName.toString())
        secondVM.showEventInfo(view, requireContext(), mealName.toString(), mealDate.toString())

        val jsonString = secondVM.getJsonDataFromAsset(requireContext(), "json.json")
        val userList = secondVM.dataClassFromJsonString(jsonString!!)

        view.btn_exit.setOnClickListener {
            (activity as HomeActivity).fragmentsReplacement(DayFragment())
        }
        val jsonElement = userList.find { it.Nazwa == view.meal_name.text.toString() }
        val caloriesPer100 = jsonElement?.Kcal

        view.btn_calculate_kcal.setOnClickListener {
            val grams = view.meal_grams_ET.text.toString()
            val result = secondVM.kcalCalculator(caloriesPer100 ?: 100, grams.toInt())
            meal_kcal.text = result.toString()
        }

        view.btn_delete.setOnClickListener {

            MaterialAlertDialogBuilder(requireContext()).setTitle("Alert").setMessage("Are you sure you want to delete event: ${mealName.toString()}")
                .setNegativeButton("No, I am fine, thanks :D"){ _, _ -> }
                .setPositiveButton("Delete Event!"){ _, _ ->
                    secondVM.deleteMeal(mealName!!, meal_date.text.toString())
                    (activity as HomeActivity).fragmentsReplacement(DayFragment())
                }.show()
        }

        view.btn_save.setOnClickListener {
            val finalElement = userList.find { it.Nazwa == view.meal_name.text.toString() }
            val finalKcal = finalElement?.Kcal
            val grams = view.meal_grams_ET.text.toString().toInt()
            val caloriesFromMeal = secondVM.kcalCalculator(finalKcal!!, grams)
            val meal = Meal(
                view.meal_name.text.toString(),
                view.meal_date.text.toString(),
                grams,
                caloriesFromMeal
            )
            secondVM.addMeal(meal)
        }

        view.meal_date.setOnClickListener {
            val dpd = DatePickerDialog(requireContext(),
                { _, mYear, mMonth, mDay -> meal_date.text = "$mDay/${mMonth+1}/$mYear" }, year, month, day)
            dpd.show()
        }

        return view
    }


}