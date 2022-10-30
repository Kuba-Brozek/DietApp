package ayathe.project.scheduleapp.registerlogin.register.userDataInput.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ayathe.project.scheduleapp.R
import ayathe.project.scheduleapp.home.homeactivity.HomeActivity
import kotlinx.android.synthetic.main.fragment_user_info.*
import kotlinx.android.synthetic.main.fragment_user_info.view.*

class UserInfoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_info, container, false)

        view.finish_btn.setOnClickListener{
            val intent = Intent(requireContext(), HomeActivity::class.java)
            startActivity(intent)
        }

        return view
    }
}