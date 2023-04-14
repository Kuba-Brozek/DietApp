package com.example.dietapp2.registerlogin.login

import androidx.lifecycle.ViewModel
import com.example.dietapp2.repository.UserRepository

class ViewModelLogin: ViewModel() {
    private val repo = UserRepository()

    fun sendEmail(email: String){
        repo.sendEmail(email)
    }

}