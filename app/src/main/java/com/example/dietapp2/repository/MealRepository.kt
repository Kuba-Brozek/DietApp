package com.example.dietapp2.repository

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.dietapp2.DTO.DayInfo
import com.example.dietapp2.DTO.Meal
import com.example.dietapp2.adapters.MealAdapter
import com.example.dietapp2.adapters.OnMealClickListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

class MealRepository {

    private var auth = FirebaseAuth.getInstance()
    private var userRepo = UserRepository()
    private val dayInfoLog = "dayInfoLog"
    private val mealLog = "MealLog"
    private val cloud = FirebaseFirestore.getInstance()
    private lateinit var mealArrayList: ArrayList<Meal>
    private lateinit var mealAdapter: MealAdapter


    @SuppressLint("SimpleDateFormat")
    fun modifyMeal(meal: Meal, mealModified: Meal, kcalEaten: Int) {
        val sdf = SimpleDateFormat("dd.MM.yyyy")
        val currentDate = sdf.format(Date())
        var dayInfo: DayInfo
        CoroutineScope(Dispatchers.IO).launch {
            userRepo.readUserData {
                var weight: Double
                dayInfoReader(meal.date!!) { dayInfoRead -> weight = dayInfoRead.weight ?: it.weight!!
                    dayInfo = dayInfoRead
                    dayInfo.dayIndex = dayIndexCalc(
                        getLocalDateFromString(it.startingDate!!, "dd.MM.yyyy"),
                        getLocalDateFromString(currentDate, "dd.MM.yyyy")
                    )
                    dayInfo.kcalGoal = if(dayInfo.kcalGoal != 9999) dayInfo.kcalGoal
                    else kcalGoalCalc(it.weight!!, it.height!!, it.age!!)
                    dayInfo.weight = weight
                    dayInfo.kcalBurnt = 0
                    dayInfo.kcalEaten = (kcalEaten - meal.cals!!) + mealModified.cals!!
                cloud.collection(auth.currentUser!!.uid).document("DaysInfo")
                    .collection(meal.date!!).document(meal.date!!).set(dayInfo)
                    .addOnSuccessListener {
                        Log.i(dayInfoLog, "Day info modified successfully")
                        cloud.collection(auth.currentUser!!.uid).document("Meals")
                            .collection(meal.date.toString()).document(meal.name.toString())
                            .set(mealModified)
                            .addOnSuccessListener {
                                Log.d(dayInfoLog, "Meal modified successfully!")
                            }.addOnFailureListener {
                                Log.d(dayInfoLog, "Meal modifying failure")
                            }
                    } .addOnFailureListener{
                        Log.i(dayInfoLog, "Day info modified successfully")
                    }
                }
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun addMeal(meal: Meal, kcalEaten: Int, date: String, callback: (DayInfo) -> Unit) {
        val sdf = SimpleDateFormat("dd.MM.yyyy")
        val currentDate = sdf.format(Date())
        val mealInfo = hashMapOf(
            "cals" to meal.cals,
            "name" to meal.name,
            "date" to meal.date,
            "grams" to meal.grams
        )
        var dayInfo: DayInfo
        CoroutineScope(Dispatchers.IO).launch {
            userRepo.readUserData {
                var weight: Double
                dayInfoReader(meal.date!!) { dayInfoRead -> weight = dayInfoRead.weight?.toDouble()
                    ?: it.weight!!
                    dayInfo = dayInfoRead
                    dayInfo.date = date
                    dayInfo.dayIndex = dayIndexCalc(
                        getLocalDateFromString(it.startingDate!!, "dd.MM.yyyy"),
                        getLocalDateFromString(currentDate, "dd.MM.yyyy")
                    )
                    dayInfo.kcalGoal = if(dayInfo.kcalGoal != 9999) dayInfo.kcalGoal
                    else kcalGoalCalc(it.weight!!, it.height!!, it.age!!)
                    dayInfo.weight = weight
                callback(dayInfo)
                cloud.collection(auth.currentUser!!.uid).document("DaysInfo")
                    .collection(date).document(date).set(dayInfo)
                    .addOnSuccessListener {

                        Log.i(dayInfoLog, "Day info added successfully")
                        cloud.collection(auth.currentUser!!.uid).document("Meals")
                            .collection(meal.date.toString()).document(meal.name.toString())
                            .set(mealInfo)
                            .addOnSuccessListener {
                                Log.d(dayInfoLog, "Meal added successfully!")
                            }.addOnFailureListener {
                                Log.d(dayInfoLog, "Meal adding failure")
                            }
                    } .addOnFailureListener{
                        Log.i(dayInfoLog, "Day info added successfully")
                    }
                }
            }
        }
    }

    fun dayInfoReader(date: String, Callback: (DayInfo) -> Unit) {

        try {
            val dayInfo = cloud.collection(auth.currentUser!!.uid).document("DaysInfo")
                .collection(date).document(date).get()
            dayInfo.addOnSuccessListener { task ->
                if (task.exists()) {
                    val day = task.toObject<DayInfo>()
                    Callback(day!!)
                    Log.i(dayInfoLog,"Document exist in DB ${day.date}")
                }
                else {
                    Callback(DayInfo("", 9999, 9999, 9999, listOf(), 9999, 9999.9))
                    Log.i(dayInfoLog,"Document doesn't exist in DB")
                }
            }
        } catch (e: Exception){
            Log.e("dayInfoError", "Internal Error")
        }
    }

    fun updateDayInfo(date: String, dayInfo: DayInfo) {
        try {
            cloud.collection(auth.currentUser!!.uid).document("DaysInfo")
                .collection(date).document(date).set(dayInfo)
        } catch (e: Exception){
            Log.e("dayInfoError", "Day Info not updated correctly")
        }
    }

    fun dayIndexCalc(startingDate: LocalDate, currentDate: LocalDate): Int {
        return ChronoUnit.DAYS.between(startingDate, currentDate).toInt() + 1
    }

    fun getLocalDateFromString(d: String, format: String): LocalDate {
        return LocalDate.parse(d, DateTimeFormatter.ofPattern(format))
    }

    fun kcalGoalCalc(weight: Double, height: Int, age: Int): Int {
        return (66 + (13.7 * weight) + (5 * height) - (6.8 * age)).toInt()
    }

    private fun activitiesMade(): List<String> {
        return emptyList()
    } // TODO()

    private fun kcalBurnt(): Int {
        return 0
    } //TODO()

    @SuppressLint("SetTextI18n")
    fun showMealInfo(mealName: String, mealDate: String, callback: (Meal) -> Unit) {

        val docRef = cloud.collection(auth.currentUser!!.uid).document("Meals")
            .collection(mealDate).document(mealName)

        docRef.get().addOnSuccessListener { docSnapshot ->
            val meal = docSnapshot.toObject<Meal>()
            callback(meal!!)
        }
    }


    fun deleteMeal(meal: Meal, callback: (DayInfo) -> Unit) {
        cloud.collection(auth.currentUser!!.uid).document("Meals")
            .collection(meal.date!!).document(meal.name!!).delete()
            .addOnSuccessListener {
                Log.i(mealLog, "Meal deleted successfully")
            }.addOnFailureListener {
                Log.e(mealLog, "Error deleting meal")
            }
        dayInfoReader(meal.date!!) {
            val dayInfo = it
            dayInfo.kcalEaten = it.kcalEaten!!.minus(meal.cals!!)
            cloud.collection(auth.currentUser!!.uid).document("DaysInfo")
                .collection(meal.date!!).document(meal.date!!).set(dayInfo)
                .addOnSuccessListener {
                    callback(dayInfo)
                    Log.i(dayInfoLog, "DayInfo corrected successfully")
                }.addOnFailureListener { _ ->
                    callback(it)
                    Log.e(dayInfoLog, "DayInfo correction failure")
                }

        }
    }

    @SuppressLint("SimpleDateFormat")
    fun eventChangeListener(recyclerView: RecyclerView, listener: OnMealClickListener, date: String) {
        mealArrayList = arrayListOf()
        mealAdapter = MealAdapter(mealArrayList, listener)
        recyclerView.adapter = mealAdapter
        cloud.collection(auth.currentUser!!.uid)
            .document("Meals").collection(date)
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onEvent(value: QuerySnapshot?,
                                     error: FirebaseFirestoreException?) {
                    if (error != null) {
                        Log.e("mealLog", error.message.toString()
                        )
                        return
                    }
                    for (doc in value?.documentChanges!!) {

                        if (doc.type == DocumentChange.Type.ADDED) {
                            mealArrayList.add(doc.document.toObject(Meal::class.java))
                        }
                    }
                    mealAdapter.notifyDataSetChanged()
                }
            })
    }

}