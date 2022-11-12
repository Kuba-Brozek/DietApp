package ayathe.project.dietapp.registerlogin.activityreglog
import androidx.lifecycle.ViewModel
import ayathe.project.dietapp.DTO.User
import ayathe.project.dietapp.repository.UserRepository

class ViewModelMainActivity: ViewModel() {
    private val repo = UserRepository()

    fun addUserToDatabase(user: User){
        repo.addUserToDatabase(user)
    }

}
