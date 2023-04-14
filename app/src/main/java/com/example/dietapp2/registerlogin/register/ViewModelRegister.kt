package com.example.dietapp2.registerlogin.register

import androidx.lifecycle.ViewModel
import com.example.dietapp2.DTO.User
import com.example.dietapp2.repository.UserRepository

class ViewModelRegister: ViewModel() {

    private val repo = UserRepository()

    fun addUserToDatabase(user: User){
        repo.addUserToDatabase(user)
    }

    fun readUserData(myCallback: (User) -> Unit){
        return repo.readUserData(myCallback)
    }

}