package com.example.dietapp2.fragments.dayInfo.eventinfo

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.viewModels
import com.example.dietapp2.DTO.DayInfo
import com.example.dietapp2.DTO.Meal
import com.example.dietapp2.R
import com.example.dietapp2.fragments.dayInfo.FoodFragment
import com.example.dietapp2.fragments.dayInfo.FoodFragmentViewModel
import com.example.dietapp2.fragments.homeactivity.HomeActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class MealInfoFragment : Fragment() {

    private lateinit var meal_name: TextView
    private lateinit var meal_date: TextView
    private lateinit var kcal_100g_TV: TextView
    private lateinit var carbs_100g_TV: TextView
    private lateinit var prot_100g_TV: TextView
    private lateinit var fat_100g_TV: TextView
    private lateinit var xxg_TV: TextView
    private lateinit var meal_grams_ET: EditText
    private lateinit var meal_kcal: TextView
    private lateinit var meal_carbs: TextView
    private lateinit var meal_proteins: TextView
    private lateinit var meal_fat: TextView
    private lateinit var btn_exit: AppCompatButton
    private lateinit var btn_save: AppCompatButton
    private lateinit var btn_delete: AppCompatButton
    private val foodFragmentViewModel by viewModels<FoodFragmentViewModel>()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_meal_info, container, false)
        val args = arguments

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
        kcal_100g_TV = view.findViewById(R.id.kcal_100g_TV)
        carbs_100g_TV = view.findViewById(R.id.carbs_100g_TV)
        prot_100g_TV = view.findViewById(R.id.prot_100g_TV)
        fat_100g_TV = view.findViewById(R.id.fat_100g_TV)
        xxg_TV = view.findViewById(R.id.xxg_TV)

        var meal = Meal()
        val mealName = args?.getString("MealName")
        val mealDate = args?.getString("MealDate")
        var dayInfo = DayInfo()

        foodFragmentViewModel.dayInfoReader(mealDate.toString()) {
            dayInfo = it
        }
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        val jsonString = foodFragmentViewModel.getJsonDataFromAsset(requireContext(), "json.json")
        val mealList = foodFragmentViewModel.dataClassFromJsonString(jsonString!!)
        val jsonElement = mealList.find { it.name == mealName }!!

        kcal_100g_TV.text = jsonElement.kcal.toString()
        carbs_100g_TV.text = jsonElement.carbs.toString()
        prot_100g_TV.text = jsonElement.protein.toString()
        fat_100g_TV.text = jsonElement.fat.toString()


        foodFragmentViewModel.showMealInfo(mealName.toString(), mealDate.toString()) {
            meal = it
            val mealGrams = "${it.grams.toString()}g"
            xxg_TV.text = mealGrams
            meal_name.text = it.name.toString()
            meal_date.text = it.date.toString()
            meal_grams_ET.setText(it.grams.toString())
            meal_kcal.text = foodFragmentViewModel.nutritionalValuesCalc(jsonElement.kcal!!, it.grams!!.toInt()).toString()
            meal_carbs.text = foodFragmentViewModel.nutritionalValuesCalc(jsonElement.carbs!!.toInt(), it.grams!!.toInt()).toString()
            meal_proteins.text = foodFragmentViewModel.nutritionalValuesCalc(jsonElement.protein!!.toInt(), it.grams!!.toInt()).toString()
            meal_fat.text = foodFragmentViewModel.nutritionalValuesCalc(jsonElement.fat!!.toInt(), it.grams!!.toInt()).toString()
        }

        meal_grams_ET.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (s.toString()!= "") {
                    val mealGrams = "${s.toString()}g"
                    xxg_TV.text = mealGrams
                    val grams = s.toString()
                    btn_save.isEnabled = true
                    meal_kcal.text = foodFragmentViewModel.nutritionalValuesCalc(jsonElement.kcal!!, grams.toInt()).toString()
                    meal_carbs.text = foodFragmentViewModel.nutritionalValuesCalc(jsonElement.carbs!!.toInt(), grams.toInt()).toString()
                    meal_proteins.text = foodFragmentViewModel.nutritionalValuesCalc(jsonElement.protein!!.toInt(), grams.toInt()).toString()
                    meal_fat.text = foodFragmentViewModel.nutritionalValuesCalc(jsonElement.fat!!.toInt(), grams.toInt()).toString()
                } else {
                    btn_save.isEnabled = false
                }
            }
        })

        btn_delete.setOnClickListener {

            MaterialAlertDialogBuilder(requireContext()).setTitle(mealName!!)
                .setMessage("Are you sure you want to delete?")
                .setNegativeButton("No, I am fine"){ _, _ -> }
                .setPositiveButton("Delete Meal!"){ _, _ ->
                    foodFragmentViewModel.deleteMeal(meal) { }
                    (activity as HomeActivity).fragmentsReplacement(FoodFragment(meal.date!!))
                }.show()
        }

        btn_exit.setOnClickListener {
            (activity as HomeActivity).fragmentsReplacement(FoodFragment())
        }

        btn_save.setOnClickListener {
            val grams = meal_grams_ET.text.toString().toInt()
            val caloriesFromMeal = foodFragmentViewModel.nutritionalValuesCalc(jsonElement.kcal!!, grams)
            val mealModified = Meal(
                meal_name.text.toString(),
                meal_date.text.toString(),
                grams,
                caloriesFromMeal
            )
            foodFragmentViewModel.modifyMeal(meal, mealModified, dayInfo.kcalEaten!!)
            foodFragmentViewModel.dayInfoReader(meal_date.text.toString()) {
                dayInfo = it
            }
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val foodFragment = FoodFragment()
                (activity as HomeActivity).fragmentsReplacement(foodFragment)
            }
        })

        return view
    }




}