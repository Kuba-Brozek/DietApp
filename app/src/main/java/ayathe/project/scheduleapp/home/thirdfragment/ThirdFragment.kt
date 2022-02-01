package ayathe.project.scheduleapp.home.thirdfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import ayathe.project.scheduleapp.R

class ThirdFragment : Fragment() {

    private val thirdVM by viewModels<ViewModelThirdFragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_third, container, false)
        // Inflate the layout for this fragment
        return view
    }
}