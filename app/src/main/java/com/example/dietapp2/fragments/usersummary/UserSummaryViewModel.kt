package com.example.dietapp2.fragments.usersummary

import android.animation.ValueAnimator
import android.widget.TextView
import androidx.lifecycle.ViewModel
import com.example.dietapp2.DTO.DayInfo
import com.example.dietapp2.DTO.User
import com.example.dietapp2.DTO.UserDetails
import com.example.dietapp2.repository.MealRepository
import com.example.dietapp2.repository.UserRepository
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class UserSummaryViewModel: ViewModel() {

    private val userRepo = UserRepository()
    private val mealRepo = MealRepository()

    fun animateTextView(initialValue: Int, finalValue: Int, textview: TextView) {
        val valueAnimator = ValueAnimator.ofInt(initialValue, finalValue)
        valueAnimator.duration = 1500
        valueAnimator.addUpdateListener {
            textview.text = valueAnimator.animatedValue.toString()
        }
        valueAnimator.start()
    }

    fun kcalGoalCalc(user: User): Int {
        return mealRepo.kcalGoalCalc(user.weight!!, user.height!!, user.age!!)
    }

    fun readUserData(callback: (User) -> Unit) {
        return userRepo.readUserData(callback)
    }

    fun readUserDetails(callback: (UserDetails) -> Unit) {
        return userRepo.readUserDetails(callback)
    }

    fun readDayInfo(date: String, callback: (DayInfo) -> Unit) {
        return mealRepo.dayInfoReader(date ,callback)
    }

    fun dayInfoExist(currentDayInfo: DayInfo): Boolean{
        return currentDayInfo.kcalEaten != null &&
                currentDayInfo.kcalGoal != null &&
                currentDayInfo.dayIndex != null
    }

    fun userAccountAgeInDays(startingDate: LocalDate, currentDate: LocalDate): String{
        return ChronoUnit.DAYS.between(startingDate, currentDate).toString()
    }

    fun addUserDetailsToDB(userDetails: UserDetails){
        return userRepo.addUserDetailsToDB(userDetails)
    }

    fun updateDayInfo(date: String, dayInfo: DayInfo){
        return mealRepo.updateDayInfo(date, dayInfo)
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
}