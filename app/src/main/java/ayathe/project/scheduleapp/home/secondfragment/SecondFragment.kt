package ayathe.project.scheduleapp.home.secondfragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ayathe.project.scheduleapp.R
import ayathe.project.scheduleapp.adapter.EventAdapter
import ayathe.project.scheduleapp.data.Event
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_second.*
import kotlinx.android.synthetic.main.fragment_second.view.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*


class SecondFragment : Fragment() {


    private lateinit var eventArrayList: ArrayList<Event>
    private lateinit var eventAdapter: EventAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var db: FirebaseFirestore
    private val secondVM by viewModels<ViewModelSecondFragment>()
    private val calendar = Calendar.getInstance()
    private val year = calendar.get(Calendar.YEAR)
    private val month = calendar.get(Calendar.MONTH)
    private val day = calendar.get(Calendar.DAY_OF_MONTH)
    private var categoryList = mutableListOf("kategoria 1", "kategoria 2", "kategoria 3", "kategoria 4", "inne")
    private var choice: String? = null

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_second, container, false)
        view.btn_calendar.setOnClickListener {
            val dpd = DatePickerDialog(requireContext(),
                { _, mYear, mMonth, mDay -> date_TV.text = "$mDay/${mMonth+1}/$mYear" }, year, month, day)
            dpd.show()
        }
        recyclerView = view.findViewById(R.id.event_list_RV)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        recyclerView.setHasFixedSize(true)
        eventArrayList = arrayListOf()
        eventAdapter = EventAdapter(eventArrayList)
        recyclerView.adapter = eventAdapter

        secondVM.eventChangeListener(recyclerView)

        secondVM.spinner(view.category_spinner, requireContext(), categoryList)
        view.category_spinner.onItemSelectedListener = object : AdapterView.OnItemClickListener,
            AdapterView.OnItemSelectedListener {
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                TODO("Not yet implemented") }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented") }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                choice = parent?.getItemAtPosition(position).toString()
            }
        }
        view.btn_submit_event.setOnClickListener {
            val event = Event(view.event_name_ET.text.toString(),
                view.date_TV.text.toString(),
                view.event_description_ET.text.toString(),
                view.category_spinner.selectedItem.toString())
        }
        view.event_name_ET.visibility = View.GONE
        view.btn_calendar.visibility = View.GONE
        view.date_TV.visibility = View.GONE
        view.category_spinner.visibility = View.GONE
        view.event_description_ET.visibility = View.GONE
        view.background_event_adding.visibility = View.GONE
        view.btn_submit_event.visibility = View.GONE
        view.btn_no_add_event.visibility = View.GONE

        view.btn_add_event.setOnClickListener {
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
        view.btn_no_add_event.setOnClickListener {
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

        return view
    }

    private fun load(recyclerView: RecyclerView) = runBlocking {
        launch {
            delay(1500)
            secondVM.eventChangeListener(recyclerView)
        }
    }


}