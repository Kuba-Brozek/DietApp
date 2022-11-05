package ayathe.project.scheduleapp.home.usersettings

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ayathe.project.scheduleapp.DTO.ProductFromJSON
import ayathe.project.scheduleapp.DTO.User
import ayathe.project.scheduleapp.repository.UserRepository
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import okhttp3.internal.wait
import java.io.IOException


class UserSettingsViewModel: ViewModel() {
    private val repo = UserRepository()
    private val dispatcherIO: CoroutineDispatcher = Dispatchers.IO

    private fun changePassword(password: String, context: Context) {
        viewModelScope.launch {
            repo.changePassword(password, context)
        }
    }

    private fun changeEmail(email: String, context: Context) {
        viewModelScope.launch {
            repo.changeEmail(email, context)
        }
    }

    fun emailChangeConfirmation(context: Context, textView: TextView) = viewModelScope.launch {
        MaterialAlertDialogBuilder(context).setTitle("Alert")
            .setMessage("Are you sure you want to change your email?")
            .setNegativeButton("I'll keep it that way") { _, _ -> }
            .setPositiveButton("Change my email!") { _, _ ->
                changeEmail(textView.text.toString(), context)
            }.show()
    }


    fun passwordChangeConfirmation(context: Context, password: String)= viewModelScope.launch{
        MaterialAlertDialogBuilder(context).setTitle("Alert").setMessage("Are you sure you want to change your password?")
            .setNegativeButton("I'll keep it that way"){ _, _ -> }
            .setPositiveButton("Change my password!"){ _, _ ->
                changePassword(password, context)
            }.show()
    }


    fun showUserInfo(): String{
        return repo.showUserInfo()
    }

    suspend fun loadProfileImage(context: Context, view: ImageView){
        try {
            viewModelScope.launch {
                repo.loadProfileImage(context, view)
            }.join()
        } catch (ex: Exception){

        }
    }

    open fun getJsonDataFromAsset(context: Context, filename: String): String? {
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

    open fun dataClassFromJsonString(jsonString: String){
        val gson = Gson()

        val listType = object : TypeToken<List<ProductFromJSON>>() {}.type
        val listOfProducts: List<ProductFromJSON> = gson.fromJson(jsonString, listType)
        listOfProducts.forEachIndexed { id, product ->
            Log.i("", "Product$id, $product") }
    }


    fun readUserData(myCallback: (User) -> Unit){
        return repo.readUserData(myCallback)
    }

    suspend fun uploadProfileImage(imageFileUri: Uri) {
        viewModelScope.launch {
            repo.uploadProfileImage(imageFileUri)
        }.join()
    }
}