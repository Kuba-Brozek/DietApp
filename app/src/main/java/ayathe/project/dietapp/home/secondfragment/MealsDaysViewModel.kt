package ayathe.project.dietapp.home.secondfragment

import android.content.Context
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import ayathe.project.dietapp.adapters.OnMealClickListener
import ayathe.project.dietapp.DTO.Meal
import ayathe.project.dietapp.DTO.ProductFromJSON
import ayathe.project.dietapp.repository.MealRepository
import ayathe.project.dietapp.repository.UserRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

class MealsDaysViewModel: ViewModel() {
    private val userRepo = UserRepository()
    private val repo = MealRepository()


    fun addMeal(meal: Meal){
        repo.addMeal(meal)
    }

    fun eventChangeListener(recyclerView: RecyclerView, listener: OnMealClickListener, date: String){
        repo.eventChangeListener(recyclerView, listener, date)
    }


    fun deleteMeal(mealDate: String, mealName: String){
        repo.deleteMeal(mealDate, mealName)
    }

    fun getJsonDataFromAsset(context: Context, filename: String): String? {
        var jsonString = ""
        try {
            jsonString = context.assets.open(filename).bufferedReader().use {
                it.readText()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
        return jsonString
    }

    fun dataClassFromJsonString(jsonString: String): MutableList<ProductFromJSON> {
        val gson = Gson()
        val productList = mutableListOf<ProductFromJSON>()
        val listType = object : TypeToken<List<ProductFromJSON>>() {}.type
        val listOfProducts: List<ProductFromJSON> = gson.fromJson(jsonString, listType)
        listOfProducts.forEachIndexed { _, product ->
            productList.add(product)
        }
        return productList
    }


    fun showEventInfo(view: View, context: Context, mealName: String, mealDate: String) {
        repo.showMealInfo(view, context, mealName, mealDate)
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
        return (grams / 100 * value.toInt())
    }
}
//yyyy-MM-dd
//0123 4 56 7 89