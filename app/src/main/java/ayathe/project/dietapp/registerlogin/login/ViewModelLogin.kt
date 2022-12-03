package ayathe.project.dietapp.registerlogin.login

import androidx.lifecycle.ViewModel
import ayathe.project.dietapp.repository.UserRepository

class ViewModelLogin: ViewModel() {
    private val repo = UserRepository()

    fun sendEmail(email: String){
        repo.sendEmail(email)
    }

}