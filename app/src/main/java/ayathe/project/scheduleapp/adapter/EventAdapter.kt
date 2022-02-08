package ayathe.project.scheduleapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ayathe.project.scheduleapp.R
import ayathe.project.scheduleapp.data.Event
import com.bumptech.glide.Glide

class EventAdapter(private val eventList: ArrayList<Event>, private val listener: OnEventClickListener): RecyclerView.Adapter<EventAdapter.EventViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_event, parent, false)

        return EventViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event: Event = eventList[position]
        holder.date.text = event.date
        holder.desc.text = event.name
        try{
            when {
                event.category.toString() ==  "biznes" -> {
                    Glide.with(holder.itemView).load(R.drawable.business).into(holder.itemView.findViewById(R.id.background_event))
                }
                event.category.toString() == "edukacja" -> {
                    Glide.with(holder.itemView).load(R.drawable.education).into(holder.itemView.findViewById(R.id.background_event))
                }
                event.category.toString() == "sprawy domowe" -> {
                    Glide.with(holder.itemView).load(R.drawable.home).into(holder.itemView.findViewById(R.id.background_event))
                }
                event.category.toString() == "trening" -> {
                    Glide.with(holder.itemView).load(R.drawable.work_out).into(holder.itemView.findViewById(R.id.background_event))
                }
                event.category.toString() == "inne" -> {
                    Glide.with(holder.itemView).load(R.drawable.other).into(holder.itemView.findViewById(R.id.background_event))
                }
            }
        }catch(e: Exception){
            Log.e("error","failed to load resources")
        }
    }

    override fun getItemCount(): Int {
        return eventList.size
    }


    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val date: TextView = itemView.findViewById(R.id.date_CV)
        val desc: TextView = itemView.findViewById(R.id.name_TV)

        init{
            itemView.setOnLongClickListener{
                listener.onEventLongClick(eventList[absoluteAdapterPosition], absoluteAdapterPosition)
                true
            }
            itemView.setOnClickListener{
                listener.onEventClick(eventList[absoluteAdapterPosition], absoluteAdapterPosition)
            }
        }
    }

}

interface OnEventClickListener {
    fun onEventLongClick(event: Event, position: Int)
    fun onEventClick(event: Event, position: Int)
}