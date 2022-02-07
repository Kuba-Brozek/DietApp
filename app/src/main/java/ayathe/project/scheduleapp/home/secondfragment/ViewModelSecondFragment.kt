package ayathe.project.scheduleapp.home.secondfragment

import android.R
import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import ayathe.project.scheduleapp.adapter.OnEventClickListener
import ayathe.project.scheduleapp.data.Event
import ayathe.project.scheduleapp.repository.UserRepository

class ViewModelSecondFragment: ViewModel() {
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
}