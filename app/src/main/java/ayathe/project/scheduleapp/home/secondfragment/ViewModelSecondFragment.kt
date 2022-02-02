package ayathe.project.scheduleapp.home.secondfragment

import android.app.DatePickerDialog
import android.view.View
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import ayathe.project.scheduleapp.repository.UserRepository
import kotlinx.android.synthetic.main.fragment_second.*
import kotlinx.android.synthetic.main.fragment_second.view.*

class ViewModelSecondFragment: ViewModel() {
    val repo = UserRepository()
    fun func(){
    }
}