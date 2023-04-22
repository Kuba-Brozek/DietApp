package com.example.dietapp2.adapters

import com.example.dietapp2.DTO.Meal

interface OnMealClickListener {
    fun onMealLongClick(meal: Meal, position: Int)
    fun onMealClick(meal: Meal, position: Int)
}