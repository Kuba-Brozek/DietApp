package ayathe.project.scheduleapp.home.usersettings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import ayathe.project.scheduleapp.R

class ChangePasswordFragment : Fragment() {

    private val userSettingsVM by viewModels<UserSettingsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.activity_settings, container, false)
        lifecycleScope.launchWhenStarted {

        }
        return view
    }
}

