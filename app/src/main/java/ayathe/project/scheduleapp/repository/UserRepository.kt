package ayathe.project.scheduleapp.repository

import android.util.Log
import ayathe.project.scheduleapp.data.Event
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class UserRepository {

    private var auth = FirebaseAuth.getInstance()
    private val user = Firebase.auth.currentUser
    private val debug = "DEBUG"
    private val cloud = FirebaseFirestore.getInstance()

    fun changePassword(password: String){
        user!!.updatePassword(password).addOnSuccessListener {
            Log.d(debug, "Udało się.")
        }.addOnFailureListener {
            Log.d(debug, "Nie udało się.")
        }
    }

    fun changeEmail(email: String){
        user!!.updateEmail(email)
            .addOnSuccessListener {
                Log.d(debug, "User password updated.")
            }.addOnFailureListener {
                Log.d(debug, "Nie udało się.")
            }
    }

    fun addEvent(event: Event){
        val eventMap = hashMapOf(
            "date" to event.date,
            "description" to event.description
        )
        cloud.collection("events").document(auth.currentUser!!.uid).collection(event.category.toString()).document(event.name.toString())
            .set(eventMap).addOnSuccessListener {
                Log.d(debug, "Document added succesfully!")
            }.addOnFailureListener {
                Log.d(debug, "Document wasn't added!")
            }
    }




}