package ayathe.project.scheduleapp.home.secondfragment

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import ayathe.project.scheduleapp.R
import kotlinx.android.synthetic.main.fragment_second.*
import kotlinx.android.synthetic.main.fragment_second.view.*
import java.util.*


class SecondFragment : Fragment() {

    private val secondVM by viewModels<ViewModelSecondFragment>()
    private val calendar = Calendar.getInstance()
    private val year = calendar.get(Calendar.YEAR)
    private val month = calendar.get(Calendar.MONTH)
    private val day = calendar.get(Calendar.DAY_OF_MONTH)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val xd = null
        val view: View = inflater.inflate(R.layout.fragment_second, container, false)
        view.btn_calendar.setOnClickListener {
            val dpd = DatePickerDialog(requireContext(),
                { _, mYear, mMonth, mDay -> TVTV.text = "$mDay/$mMonth/$mYear" }, year, month, day)
            dpd.show()
        }
        return view
    }

}