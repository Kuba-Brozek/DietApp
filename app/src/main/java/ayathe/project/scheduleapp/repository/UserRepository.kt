package ayathe.project.scheduleapp.repository

import android.util.Log
import android.widget.Toast
import ayathe.project.scheduleapp.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.DEBUG_PROPERTY_NAME

class UserRepository {

    private var auth = FirebaseAuth.getInstance()
    val user = Firebase.auth.currentUser
    val debug = "DEBUG"

    fun changePassword(password: String){
        user!!.updatePassword(password).addOnSuccessListener {
            Log.d(debug, "Udało się")
        }.addOnFailureListener {
            Log.d(debug, "Nie udało się")
        }
    }




}