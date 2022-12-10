package ayathe.project.dietapp.fragments.dayInfo.eventinfo

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import ayathe.project.dietapp.DTO.DayInfo
import ayathe.project.dietapp.R
import ayathe.project.dietapp.DTO.Meal
import ayathe.project.dietapp.fragments.homeactivity.HomeActivity
import ayathe.project.dietapp.fragments.dayInfo.DayFragment
import ayathe.project.dietapp.fragments.dayInfo.MealsDaysViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_event_info.*
import kotlinx.android.synthetic.main.fragment_event_info.view.*


class MealInfo : Fragment() {
    private val secondVM by viewModels<MealsDaysViewModel>()

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
        var dayInfo = DayInfo()

        secondVM.dayInfoReader(mealDate.toString()) {
            dayInfo = it
        }

        val jsonString = secondVM.getJsonDataFromAsset(requireContext(), "json.json")
        val mealList = secondVM.dataClassFromJsonString(jsonString!!)
        val jsonElement = mealList.find { it.name == mealName }

        secondVM.showMealInfo(mealName.toString(), mealDate.toString()) {
            meal = it
            view.meal_name.setText(it.name.toString())
            view.meal_date.text = it.date.toString()
            view.meal_grams_ET.setText(it.grams.toString())
            view.meal_kcal.text = it.cals.toString()
            view.meal_kcal.text = secondVM.nutritionalValuesCalc(jsonElement!!.kcal!!, it.grams!!.toInt()).toString()
            view.meal_carbs.text = secondVM.nutritionalValuesCalc(jsonElement.carbs!!.toInt(), it.grams!!.toInt()).toString()
            view.meal_proteins.text = secondVM.nutritionalValuesCalc(jsonElement.protein!!.toInt(), it.grams!!.toInt()).toString()
            view.meal_fat.text = secondVM.nutritionalValuesCalc(jsonElement.fat!!.toInt(), it.grams!!.toInt()).toString()
        }

        view.btn_exit.setOnClickListener {
            (activity as HomeActivity).fragmentsReplacement(DayFragment())
        }

        view.meal_grams_ET.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (s.toString()!= "") {
                    val grams = s.toString()
                    view.btn_save.isEnabled = true
                    meal_kcal.text = secondVM.nutritionalValuesCalc(jsonElement!!.kcal!!, grams.toInt()).toString()
                    meal_carbs.text = secondVM.nutritionalValuesCalc(jsonElement.carbs!!.toInt(), grams.toInt()).toString()
                    meal_proteins.text = secondVM.nutritionalValuesCalc(jsonElement.protein!!.toInt(), grams.toInt()).toString()
                    meal_fat.text = secondVM.nutritionalValuesCalc(jsonElement.fat!!.toInt(), grams.toInt()).toString()
                } else {
                    view.btn_save.isEnabled = false
                }
            }
        })

        view.btn_delete.setOnClickListener {

            MaterialAlertDialogBuilder(requireContext()).setTitle("Alert").setMessage("Are you sure you want to delete event: ${mealName.toString()}")
                .setNegativeButton("No, I am fine, thanks :D"){ _, _ -> }
                .setPositiveButton("Delete Event!"){ _, _ ->
                    secondVM.deleteMeal(meal) { }
                    (activity as HomeActivity).fragmentsReplacement(DayFragment())
                }.show()
        }

        view.btn_save.setOnClickListener {
            val grams = view.meal_grams_ET.text.toString().toInt()
            val caloriesFromMeal = secondVM.nutritionalValuesCalc(jsonElement!!.kcal!!, grams)
            val mealModified = Meal(
                view.meal_name.text.toString(),
                view.meal_date.text.toString(),
                grams,
                caloriesFromMeal
            )
            secondVM.modifyMeal(meal, mealModified, dayInfo.kcalEaten!!)
            secondVM.dayInfoReader(meal_date.text.toString()) {
                dayInfo = it
            }
        }

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val dayFragment = DayFragment()
                (activity as HomeActivity).fragmentsReplacement(dayFragment)
            }
        })

        return view
    }




}