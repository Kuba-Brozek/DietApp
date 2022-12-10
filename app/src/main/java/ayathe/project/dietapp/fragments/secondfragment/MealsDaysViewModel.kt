package ayathe.project.dietapp.fragments.secondfragment

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import ayathe.project.dietapp.DTO.DayInfo
import ayathe.project.dietapp.adapters.OnMealClickListener
import ayathe.project.dietapp.DTO.Meal
import ayathe.project.dietapp.DTO.ProductFromJSON
import ayathe.project.dietapp.DTO.User
import ayathe.project.dietapp.repository.MealRepository
import ayathe.project.dietapp.repository.UserRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.time.LocalDate

class MealsDaysViewModel: ViewModel() {
    private val userRepo = UserRepository()
    private val repo = MealRepository()


    @RequiresApi(Build.VERSION_CODES.O)
    fun modifyMeal(meal: Meal, mealModified: Meal, kcalEaten: Int) {
        repo.modifyMeal(meal, mealModified, kcalEaten)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addMeal(meal: Meal, kcalEaten: Int, date: String, callback: (DayInfo) -> Unit) {
        repo.addMeal(meal, kcalEaten, date, callback)
    }

    fun readUserData(myCallback: (User) -> Unit){
        return userRepo.readUserData(myCallback)
    }

    fun eventChangeListener(recyclerView: RecyclerView, listener: OnMealClickListener, date: String){
        repo.eventChangeListener(recyclerView, listener, date)
    }


    fun deleteMeal(meal: Meal, callback: (DayInfo) -> Unit){
        repo.deleteMeal(meal, callback)
    }

    fun getJsonDataFromAsset(context: Context, filename: String): String? {
        var jsonString = ""
        try {
            jsonString = context.assets.open(filename).bufferedReader().use {
                it.readText()
            }
        } catch (e: IOException) {
            return jsonString
        }
        return jsonString
    }

    fun dataClassFromJsonString(jsonString: String): MutableList<ProductFromJSON> {
        val gson = Gson()
        val productList = mutableListOf<ProductFromJSON>()
        val listType = object : TypeToken<List<ProductFromJSON>>() {}.type
        val listOfProducts: List<ProductFromJSON> = gson.fromJson(jsonString, listType)
        listOfProducts.forEach {
            productList.add(it)
        }
        return productList
    }


    fun showMealInfo(mealName: String, mealDate: String, callback: (Meal) -> Unit) {
        repo.showMealInfo(mealName, mealDate, callback)
    }

    fun kcalCalculator(a: Int, b: Int): Int{
        val aDouble = a.toDouble()
        val bDouble = b.toDouble()
        val aD1 = aDouble/100
        val resultDouble = aD1*bDouble
        return resultDouble.toInt()
    }

    fun dataParserFromDate(string: String): String {
        val day = string.substring(0, 2)
        val month = string.substring(3, 5)
        val year = string.substring(6)
        return "$year-$month-$day"
    }

    fun dataParserToDate(string: String): String {
        val year = string.substring(0, 4)
        val month = string.substring(5, 7)
        val day = string.substring(8)
        return "$day.$month.$year"
    }


    fun nutritionalValuesCalc(grams: Int, value: Int): Int {
        val kcalIn1Gram = grams.toDouble() / 100
        val result = kcalIn1Gram * value
        return result.toInt()
    }

    fun dayInfoReader(date: String, callback: (DayInfo) -> Unit){
        return repo.dayInfoReader(date, callback)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun dayIndexCalc(startingDate: LocalDate, currentDate: LocalDate): Int {
        return repo.dayIndexCalc(startingDate, currentDate)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getLocalDateFromString(d: String, format: String): LocalDate {
        return repo.getLocalDateFromString(d, format)
    }
}