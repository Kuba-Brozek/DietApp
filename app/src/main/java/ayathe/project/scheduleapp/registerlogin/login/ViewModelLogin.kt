package ayathe.project.scheduleapp.registerlogin.login

import androidx.lifecycle.ViewModel
import ayathe.project.scheduleapp.repository.UserRepository
import com.google.firebase.auth.FirebaseUser

class ViewModelLogin: ViewModel() {
    private val repo = UserRepository()

    fun updateUI(currentUser: FirebaseUser?){
        repo.updateUI(currentUser)
    }
}