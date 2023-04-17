package com.example.dietapp2.fragments.dayInfo.eventinfo

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
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.viewModels
import com.example.dietapp2.DTO.DayInfo
import com.example.dietapp2.DTO.Meal
import com.example.dietapp2.R
import com.example.dietapp2.fragments.dayInfo.DayFragment
import com.example.dietapp2.fragments.dayInfo.MealsDaysViewModel
import com.example.dietapp2.fragments.homeactivity.HomeActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class MealInfo : Fragment() {

    private lateinit var meal_name: EditText
    private lateinit var meal_date: TextView
    private lateinit var meal_grams_ET: EditText
    private lateinit var meal_kcal: TextView
    private lateinit var meal_carbs: TextView
    private lateinit var meal_proteins: TextView
    private lateinit var meal_fat: TextView
    private lateinit var btn_exit: AppCompatButton
    private lateinit var btn_save: AppCompatButton
    private lateinit var btn_delete: AppCompatButton
    private val secondVM by viewModels<MealsDaysViewModel>()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_event_info, container, false)
        val args = this.arguments

        meal_name = view.findViewById(R.id.meal_name)
        meal_date = view.findViewById(R.id.meal_date)
        meal_grams_ET = view.findViewById(R.id.meal_grams_ET)
        meal_kcal = view.findViewById(R.id.meal_kcal)
        meal_carbs = view.findViewById(R.id.meal_carbs)
        meal_proteins = view.findViewById(R.id.meal_proteins)
        meal_fat = view.findViewById(R.id.meal_fat)
        btn_exit = view.findViewById(R.id.btn_exit)
        btn_save = view.findViewById(R.id.btn_save)
        btn_delete = view.findViewById(R.id.btn_delete)

        var meal = Meal()
        val mealName = args?.getString("MealName")
        val mealDate = args?.getString("MealDate")
        var dayInfo = DayInfo()

        secondVM.dayInfoReader(mealDate.toString()) {
            dayInfo = it
        }
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        val jsonString = secondVM.getJsonDataFromAsset(requireContext(), "json.json")
        val mealList = secondVM.dataClassFromJsonString(jsonString!!)
        val jsonElement = mealList.find { it.name == mealName }

        secondVM.showMealInfo(mealName.toString(), mealDate.toString()) {
            meal = it
            meal_name.setText(it.name.toString())
            meal_date.text = it.date.toString()
            meal_grams_ET.setText(it.grams.toString())
            meal_kcal.text = it.cals.toString()
            meal_kcal.text = secondVM.nutritionalValuesCalc(jsonElement!!.kcal!!, it.grams!!.toInt()).toString()
            meal_carbs.text = secondVM.nutritionalValuesCalc(jsonElement.carbs!!.toInt(), it.grams!!.toInt()).toString()
            meal_proteins.text = secondVM.nutritionalValuesCalc(jsonElement.protein!!.toInt(), it.grams!!.toInt()).toString()
            meal_fat.text = secondVM.nutritionalValuesCalc(jsonElement.fat!!.toInt(), it.grams!!.toInt()).toString()
        }

        btn_exit.setOnClickListener {
            (activity as HomeActivity).fragmentsReplacement(DayFragment())
        }

        meal_grams_ET.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (s.toString()!= "") {
                    val grams = s.toString()
                    btn_save.isEnabled = true
                    meal_kcal.text = secondVM.nutritionalValuesCalc(jsonElement!!.kcal!!, grams.toInt()).toString()
                    meal_carbs.text = secondVM.nutritionalValuesCalc(jsonElement.carbs!!.toInt(), grams.toInt()).toString()
                    meal_proteins.text = secondVM.nutritionalValuesCalc(jsonElement.protein!!.toInt(), grams.toInt()).toString()
                    meal_fat.text = secondVM.nutritionalValuesCalc(jsonElement.fat!!.toInt(), grams.toInt()).toString()
                } else {
                    btn_save.isEnabled = false
                }
            }
        })

        btn_delete.setOnClickListener {

            MaterialAlertDialogBuilder(requireContext()).setTitle("Alert").setMessage("Are you sure you want to delete event: ${mealName.toString()}")
                .setNegativeButton("No, I am fine, thanks :D"){ _, _ -> }
                .setPositiveButton("Delete Event!"){ _, _ ->
                    secondVM.deleteMeal(meal) { }
                    (activity as HomeActivity).fragmentsReplacement(DayFragment())
                }.show()
        }

        btn_save.setOnClickListener {
            val grams = meal_grams_ET.text.toString().toInt()
            val caloriesFromMeal = secondVM.nutritionalValuesCalc(jsonElement!!.kcal!!, grams)
            val mealModified = Meal(
                meal_name.text.toString(),
                meal_date.text.toString(),
                grams,
                caloriesFromMeal
            )
            secondVM.modifyMeal(meal, mealModified, dayInfo.kcalEaten!!)
            secondVM.dayInfoReader(meal_date.text.toString()) {
                dayInfo = it
            }
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val dayFragment = DayFragment()
                (activity as HomeActivity).fragmentsReplacement(dayFragment)
            }
        })

        return view
    }




}