package ayathe.project.dietapp.fragments.usersummary

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import ayathe.project.dietapp.R
import kotlinx.android.synthetic.main.activity_change_personal_info.view.*
import kotlinx.android.synthetic.main.fragment_home.view.*


class UserSummaryFragment : Fragment() {

    private val summaryVM by viewModels<UserSummaryViewModel>()


    @SuppressLint("Recycle")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view: View = inflater.inflate(R.layout.fragment_home, container, false)

        summaryVM.readUserData {
            view.starting_day_TV.text = it.startingDate.toString()
            view.s_weight_TV.text = it.weight.toString()
        }

        return view
    }
}