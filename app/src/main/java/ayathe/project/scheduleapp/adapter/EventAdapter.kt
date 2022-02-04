package ayathe.project.scheduleapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ayathe.project.scheduleapp.R
import ayathe.project.scheduleapp.data.EventCV

class EventAdapter(private val eventList: ArrayList<EventCV>): RecyclerView.Adapter<EventAdapter.EventViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EventAdapter.EventViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_event, parent, false)

        return EventViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EventAdapter.EventViewHolder, position: Int) {
        val event: EventCV = eventList[position]
        holder.date.text = event.date
        holder.desc.text = event.desc
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    public class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val date: TextView = itemView.findViewById(R.id.date_CV)
        val desc: TextView = itemView.findViewById(R.id.desc_TV)

    }

}