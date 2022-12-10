package ayathe.project.dietapp.fragments.usersummary

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import ayathe.project.dietapp.R
import kotlinx.android.synthetic.main.fragment_home.view.*


class HomeFragment : Fragment() {

    private val homeVM by viewModels<ViewModelHomeFragment>()

    @SuppressLint("Recycle")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val homeVM = ViewModelHomeFragment()
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)

        view.plus_btn.setOnClickListener {
            val add10kcal = view.current_kcal_ET.text.toString().toInt().plus(10)
            view.current_kcal_ET.setText(add10kcal.toString(), TextView.BufferType.EDITABLE)
        }
        view.minus_btn.setOnClickListener {
            val remove10kcal = view.current_kcal_ET.text.toString().toInt().minus(10)
            view.current_kcal_ET.setText(remove10kcal.toString(), TextView.BufferType.EDITABLE)
        }

        view.add_btn.setOnClickListener{
            val a = view.over_all_kcal_TV.text.toString().toInt()
            val b = view.over_all_kcal_TV.text.toString().toInt().plus(view.current_kcal_ET.text.toString().toInt())
            homeVM.animateTextView(a, b, view.over_all_kcal_TV)
        }

        return view
    }
}