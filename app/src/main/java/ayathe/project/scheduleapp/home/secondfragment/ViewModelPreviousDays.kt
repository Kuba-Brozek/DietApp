package ayathe.project.scheduleapp.home.secondfragment

import android.R
import android.content.Context
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import ayathe.project.scheduleapp.adapter.OnEventClickListener
import ayathe.project.scheduleapp.data.Event
import ayathe.project.scheduleapp.repository.UserRepository
import kotlinx.android.synthetic.main.fragment_second.view.*
import kotlinx.android.synthetic.main.fragment_third.view.*

class ViewModelPreviousDays: ViewModel() {
    private val repo = UserRepository()

    fun spinner(item: Spinner, context: Context, list: MutableList<String>){
        item.adapter = ArrayAdapter(context, R.layout.simple_spinner_item, list)
    }

    fun addEvent(event: Event){
        repo.addEvent(event)
    }

    fun eventChangeListener(recyclerView: RecyclerView, listener: OnEventClickListener){
        repo.eventChangeListener(recyclerView, listener)
    }

    fun deleteEvent(documentName: String){
        repo.deleteItem(documentName)
    }

    fun showAddWindow(view: View){
        view.btn_add_event.visibility = View.GONE
        view.background_event_adding.visibility = View.VISIBLE
        view.event_name_ET.visibility = View.VISIBLE
        view.btn_calendar.visibility = View.VISIBLE
        view.date_TV.visibility = View.VISIBLE
        view.category_spinner.visibility = View.VISIBLE
        view.event_description_ET.visibility = View.VISIBLE
        view.btn_submit_event.visibility = View.VISIBLE
        view.btn_no_add_event.visibility = View.VISIBLE
    }

    fun hideAddWindow(view: View){
        view.event_name_ET.visibility = View.GONE
        view.btn_calendar.visibility = View.GONE
        view.date_TV.visibility = View.GONE
        view.background_event_adding.visibility = View.GONE
        view.category_spinner.visibility = View.GONE
        view.event_description_ET.visibility = View.GONE
        view.btn_submit_event.visibility = View.GONE
        view.btn_no_add_event.visibility = View.GONE
        view.btn_add_event.visibility = View.VISIBLE
    }

    fun showEventInfo(view: View, context: Context, eventName: String){
        repo.showEventInfo(view, context, eventName)
    }
}