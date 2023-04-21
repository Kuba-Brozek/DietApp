package com.example.dietapp2.registerlogin.activityreglog

import androidx.lifecycle.ViewModel
import com.example.dietapp2.DTO.User
import com.example.dietapp2.repository.UserRepository

class LogRegActivityViewModel: ViewModel() {
    private val repo = UserRepository()

    fun addUserToDatabase(user: User){
        repo.addUserToDatabase(user)
    }

}
