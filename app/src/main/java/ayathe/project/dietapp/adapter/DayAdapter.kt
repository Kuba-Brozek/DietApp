package ayathe.project.dietapp.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import ayathe.project.dietapp.R
import ayathe.project.dietapp.DTO.Meal

typealias dr = R.drawable

class DayAdapter(private val mealList: ArrayList<Meal>, private val listener: OnEventClickListener): RecyclerView.Adapter<DayAdapter.EventViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_event, parent, false)

        return EventViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val meal: Meal = mealList[position]
        holder.date.text = meal.date
        holder.desc.text = meal.name
        holder.kcal.text = meal.cals.toString()
        holder.grams.text = meal.grams.toString()
        val itV = holder.background
        try{
            when {
                meal.cals.toString().toInt() > 150 -> {
                    itV.setBackgroundColor(Color.parseColor("#FFCF6C37"))
                }
                meal.cals.toString().toInt() in 50 until 150 -> {
                    itV.setBackgroundColor(Color.parseColor("#FFCDDC46"))
                }
                else -> {
                    itV.setBackgroundColor(Color.parseColor("#FF6BDA4E"))
                }

            }
        }catch(e: Exception){
            Log.e("error","failed to load resources")
        }
    }

    override fun getItemCount(): Int {
        return mealList.size
    }


    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val background: ConstraintLayout = itemView.findViewById(R.id.meal_bg)
        val date: TextView = itemView.findViewById(R.id.date_CV)
        val desc: TextView = itemView.findViewById(R.id.name_TV)
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

interface OnEventClickListener {
    fun onMealLongClick(meal: Meal, position: Int)
    fun onMealClick(meal: Meal, position: Int)
}