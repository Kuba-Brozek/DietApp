package com.example.dietapp2.adapters

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.dietapp2.DTO.Meal
import com.example.dietapp2.R

typealias dr = R.drawable

class MealAdapter(private val mealList: ArrayList<Meal>, private val listener: OnMealClickListener): RecyclerView.Adapter<MealAdapter.MealViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_event,
            parent, false)
        return MealViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        val meal: Meal = mealList[position]

        holder.name.text = meal.name
        holder.date.text = meal.date
        holder.kcal.text = meal.cals.toString()
        holder.grams.text = meal.grams.toString()

        val itV = holder.background
        try{
            when {
                meal.cals.toString().toInt() > 150 -> {
                    itV.setBackgroundColor(Color.parseColor("#E42020"))
                }
                meal.cals.toString().toInt() in 50 until 150 -> {
                    itV.setBackgroundColor(Color.parseColor("#ffff00"))
                }
                else -> {
                    itV.setBackgroundColor(Color.parseColor("#25C113"))
                }

            }
        }catch(e: Exception){
            Log.e("error","failed to load resources")
        }
    }

    override fun getItemCount(): Int {
        return mealList.size
    }


    inner class MealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val background: ConstraintLayout = itemView.findViewById(R.id.meal_bg)
        val name: TextView = itemView.findViewById(R.id.name_TV)
        val date: TextView = itemView.findViewById(R.id.date_CV)
        val kcal: TextView = itemView.findViewById(R.id.kcal_TV)
        val grams: TextView = itemView.findViewById(R.id.grams_TV)

        init{
            itemView.setOnLongClickListener{
                listener.onMealLongClick(mealList[absoluteAdapterPosition], absoluteAdapterPosition)
                true
            }
            itemView.setOnClickListener{
                listener.onMealClick(mealList[absoluteAdapterPosition], absoluteAdapterPosition)
            }
        }
    }

}

interface OnMealClickListener {
    fun onMealLongClick(meal: Meal, position: Int)
    fun onMealClick(meal: Meal, position: Int)
}