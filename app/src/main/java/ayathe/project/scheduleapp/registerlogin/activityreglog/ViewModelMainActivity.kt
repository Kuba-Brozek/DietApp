package ayathe.project.scheduleapp.registerlogin.activityreglog
import androidx.lifecycle.ViewModel
import ayathe.project.scheduleapp.DTO.User
import ayathe.project.scheduleapp.repository.UserRepository

class ViewModelMainActivity: ViewModel() {
    private val repo = UserRepository()

    fun addUserToDatabase(user: User){
        repo.addUserToDatabase(user)
    }

}
