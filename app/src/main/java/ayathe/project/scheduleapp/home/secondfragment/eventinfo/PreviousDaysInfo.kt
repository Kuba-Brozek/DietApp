package ayathe.project.scheduleapp.home.secondfragment.eventinfo

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import ayathe.project.scheduleapp.R
import ayathe.project.scheduleapp.DTO.Event
import ayathe.project.scheduleapp.home.homeactivity.HomeActivity
import ayathe.project.scheduleapp.home.secondfragment.PreviousDaysFragment
import ayathe.project.scheduleapp.home.secondfragment.ViewModelPreviousDays
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_event_info.*
import kotlinx.android.synthetic.main.fragment_event_info.view.*
import java.util.*


class PreviousDaysInfo : Fragment() {
    private val secondVM by viewModels<ViewModelPreviousDays>()
    private val calendar = Calendar.getInstance()
    private val year = calendar.get(Calendar.YEAR)
    private val month = calendar.get(Calendar.MONTH)
    private val day = calendar.get(Calendar.DAY_OF_MONTH)

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_event_info, container, false)
        // Inflate the layout for this fragment
        val args = this.arguments
        val eventName = args?.get("EventName")
        val eventCategory = args?.get("EventCategory")

        view.event_name.setText(eventName.toString())
        secondVM.showEventInfo(view, requireContext(), eventName.toString())

        view.btn_exit.setOnClickListener {
            (activity as HomeActivity).fragmentsReplacement(PreviousDaysFragment())
        }

        view.btn_delete.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext()).setTitle("Alert").setMessage("Are you sure you want to delete event: ${eventName.toString()}")
                .setNegativeButton("No, I am fine, thanks :D"){ _, _ -> }
                .setPositiveButton("Delete Event!"){ _, _ ->
                    secondVM.deleteEvent(eventName.toString())
                    (activity as HomeActivity).fragmentsReplacement(PreviousDaysFragment())
                }.show()
        }

        view.btn_save.setOnClickListener {
            val event = Event(view.event_name.text.toString(),
                view.event_date.text.toString(),
                view.event_description.text.toString(),
                eventCategory.toString())
            secondVM.addEvent(event)
        }

        view.event_date.setOnClickListener {
            val dpd = DatePickerDialog(requireContext(),
                { _, mYear, mMonth, mDay -> event_date.text = "$mDay/${mMonth+1}/$mYear" }, year, month, day)
            dpd.show()
        }

        return view
    }
}