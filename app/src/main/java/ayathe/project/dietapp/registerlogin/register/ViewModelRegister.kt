package ayathe.project.dietapp.registerlogin.register

import androidx.lifecycle.ViewModel
import ayathe.project.dietapp.DTO.User
import ayathe.project.dietapp.repository.UserRepository

class ViewModelRegister: ViewModel() {

    private val repo = UserRepository()

    fun addUserToDatabase(user: User){
        repo.addUserToDatabase(user)
    }

    fun readUserData(myCallback: (User) -> Unit){
        return repo.readUserData(myCallback)
    }

}