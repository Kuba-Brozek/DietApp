package com.example.dietapp2.fragments.usersummary

import android.animation.ValueAnimator
import android.widget.TextView
import androidx.lifecycle.ViewModel
import com.example.dietapp2.DTO.DayInfo
import com.example.dietapp2.DTO.User
import com.example.dietapp2.repository.MealRepository
import com.example.dietapp2.repository.UserRepository

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

    fun readUserData(callback: (User) -> Unit) {
        return userRepo.readUserData(callback)
    }

    fun readDayInfo(date: String, callback: (DayInfo) -> Unit) {
        return mealRepo.dayInfoReader(date ,callback)
    }
}