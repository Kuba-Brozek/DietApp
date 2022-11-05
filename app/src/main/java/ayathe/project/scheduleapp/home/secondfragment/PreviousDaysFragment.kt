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
import ayathe.project.scheduleapp.adapter.DayAdapter
import ayathe.project.scheduleapp.adapter.OnEventClickListener
import ayathe.project.scheduleapp.DTO.Day
import ayathe.project.scheduleapp.home.homeactivity.HomeActivity
import ayathe.project.scheduleapp.home.secondfragment.eventinfo.PreviousDaysInfo
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_second.*
import kotlinx.android.synthetic.main.fragment_second.view.*
import java.util.*


class PreviousDaysFragment : Fragment(), OnEventClickListener {


    private lateinit var dayArrayList: ArrayList<Day>
    private lateinit var dayAdapter: DayAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var db: FirebaseFirestore
    private val secondVM by viewModels<ViewModelPreviousDays>()
    private val calendar = Calendar.getInstance()
    private val year = calendar.get(Calendar.YEAR)
    private val month = calendar.get(Calendar.MONTH)
    private val day = calendar.get(Calendar.DAY_OF_MONTH)
    private var categoryList = mutableListOf("biznes", "edukacja", "sprawy domowe", "trening", "inne")
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
        dayArrayList = arrayListOf()
        dayAdapter = DayAdapter(dayArrayList, this)
        recyclerView.adapter = dayAdapter

        secondVM.eventChangeListener(recyclerView, this)

        secondVM.spinner(view.category_spinner, requireContext(), categoryList)
        view.category_spinner.onItemSelectedListener = object : AdapterView.OnItemClickListener,
            AdapterView.OnItemSelectedListener {
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                choice = parent?.getItemAtPosition(position).toString()
            }
        }
        view.btn_submit_event.setOnClickListener {
            val event = Day(view.event_name_ET.text.toString(),
                view.date_TV.text.toString(),
                view.event_description_ET.text.toString(),
                view.category_spinner.selectedItem.toString())
            secondVM.addEvent(event)
            secondVM.hideAddWindow(view)
        }
        secondVM.hideAddWindow(view)

        view.btn_add_event.setOnClickListener {
            secondVM.showAddWindow(view)
        }
        view.btn_no_add_event.setOnClickListener {
            secondVM.hideAddWindow(view)
        }

        return view
    }

    override fun onEventLongClick(day: Day, position: Int) {
        MaterialAlertDialogBuilder(requireContext()).setTitle("Alert").setMessage("Are you sure you want to delete event: ${day.name.toString()}")
            .setNegativeButton("No, I am fine, thanks :D"){ _, _ -> }
            .setPositiveButton("Delete Event!"){ _, _ ->
                secondVM.deleteEvent(day.name.toString())
                (activity as HomeActivity).fragmentsReplacement(PreviousDaysFragment())
            }.show()
        Toast.makeText(requireContext(), position.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun onEventClick(day: Day, position: Int) {
        val name = day.name.toString()
        val category =day.category.toString()
        val bundle = Bundle()
        val eventInfo = PreviousDaysInfo()
        bundle.putString("EventName",name)
        bundle.putString("EventCategory",category)
        eventInfo.arguments = bundle
        (activity as HomeActivity).fragmentsReplacement(eventInfo)
    }
}