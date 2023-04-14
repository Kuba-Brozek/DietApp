package com.example.dietapp2.fragments.usersettings

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dietapp2.DTO.ProductFromJSON
import com.example.dietapp2.DTO.User
import com.example.dietapp2.repository.UserRepository
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import java.io.IOException


class UserSettingsViewModel: ViewModel() {
    private val repo = UserRepository()
    private val dispatcherIO: CoroutineDispatcher = Dispatchers.IO

    private fun changePassword(password: String, context: Context) {
        viewModelScope.launch {
            repo.changePassword(password, context)
        }
    }

    fun addUserToDatabase(user: User){
        repo.addUserToDatabase(user)
    }




    fun passwordChangeConfirmation(context: Context, password: String)= viewModelScope.launch{
        MaterialAlertDialogBuilder(context).setTitle("Alert").setMessage("Are you sure you want to change your password?")
            .setNegativeButton("I'll keep it that way"){ _, _ -> }
            .setPositiveButton("Change my password!"){ _, _ ->
                changePassword(password, context)
            }.show()
    }


    fun loadProfileImage(context: Context, view: ImageView){
        try {
                repo.loadProfileImage(context, view)
        } catch (ex: Exception){
        }
    }

    fun getJsonDataFromAsset(context: Context, filename: String): String? {
        var jsonString: String = ""
        try {
            jsonString = context.assets.open(filename).bufferedReader().use {
                it.readText()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return null
            }
        return jsonString
        }

    fun dataClassFromJsonString(jsonString: String): MutableMap<Int, ProductFromJSON> {
        val gson = Gson()
        val productList = mutableMapOf<Int, ProductFromJSON>()
        val listType = object : TypeToken<List<ProductFromJSON>>() {}.type
        val listOfProducts: List<ProductFromJSON> = gson.fromJson(jsonString, listType)
        listOfProducts.forEachIndexed { id, product ->
            productList[id] = product
        }
        return productList
    }

    fun readUserData(myCallback: (User) -> Unit){
        return repo.readUserData(myCallback)
    }

    fun uploadProfileImage(imageFileUri: Uri) {
        repo.uploadProfileImage(imageFileUri)
    }
}