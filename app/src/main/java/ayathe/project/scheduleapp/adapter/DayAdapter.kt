package ayathe.project.scheduleapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ayathe.project.scheduleapp.R
import ayathe.project.scheduleapp.DTO.Day
import com.bumptech.glide.Glide

typealias dr = R.drawable

class DayAdapter(private val dayList: ArrayList<Day>, private val listener: OnEventClickListener): RecyclerView.Adapter<DayAdapter.EventViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_event, parent, false)

        return EventViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val day: Day = dayList[position]
        holder.date.text = day.date
        holder.desc.text = day.name
        val itV = holder.itemView
        try{
            when {
                day.category.toString() ==  "biznes" -> {
                    Glide.with(itV).load(dr.business).into(itV.findViewById(R.id.background_event))
                }
                day.category.toString() == "edukacja" -> {
                    Glide.with(itV).load(dr.education).into(itV.findViewById(R.id.background_event))
                }
                day.category.toString() == "sprawy domowe" -> {
                    Glide.with(itV).load(dr.home).into(itV.findViewById(R.id.background_event))
                }
                day.category.toString() == "trening" -> {
                    Glide.with(itV).load(dr.work_out).into(itV.findViewById(R.id.background_event))
                }
                day.category.toString() == "inne" -> {
                    Glide.with(itV).load(dr.other).into(itV.findViewById(R.id.background_event))
                }
            }
        }catch(e: Exception){
            Log.e("error","failed to load resources")
        }
    }

    override fun getItemCount(): Int {
        return dayList.size
    }


    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val date: TextView = itemView.findViewById(R.id.date_CV)
        val desc: TextView = itemView.findViewById(R.id.name_TV)

        init{
            itemView.setOnLongClickListener{
                listener.onEventLongClick(dayList[absoluteAdapterPosition], absoluteAdapterPosition)
                true
            }
            itemView.setOnClickListener{
                listener.onEventClick(dayList[absoluteAdapterPosition], absoluteAdapterPosition)
            }
        }
    }

}

interface OnEventClickListener {
    fun onEventLongClick(day: Day, position: Int)
    fun onEventClick(day: Day, position: Int)
}