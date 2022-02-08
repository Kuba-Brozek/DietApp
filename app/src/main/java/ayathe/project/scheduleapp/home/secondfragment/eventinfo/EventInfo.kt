package ayathe.project.scheduleapp.home.secondfragment.eventinfo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import ayathe.project.scheduleapp.R
import ayathe.project.scheduleapp.home.secondfragment.ViewModelSecondFragment
import ayathe.project.scheduleapp.home.thirdfragment.ViewModelThirdFragment
import kotlinx.android.synthetic.main.fragment_event_info.view.*


class EventInfo : Fragment() {
    private val secondVM by viewModels<ViewModelSecondFragment>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_event_info, container, false)
        // Inflate the layout for this fragment
        val args = this.arguments
        val inputData = args?.get("EventInfo")
        view.event_name.text = inputData.toString()


        return view
    }
}