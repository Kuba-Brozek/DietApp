package ayathe.project.scheduleapp.home.thirdfragment

import androidx.lifecycle.ViewModel
import ayathe.project.scheduleapp.repository.UserRepository

class ViewModelThirdFragment: ViewModel() {
    private val repo = UserRepository()

    fun changePassword(password: String){
        repo.changePassword(password)
    }
    fun changeEmail(email: String){
        repo.changeEmail(email)
    }
}