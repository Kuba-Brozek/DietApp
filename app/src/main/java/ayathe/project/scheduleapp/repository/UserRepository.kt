package ayathe.project.scheduleapp.repository

import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ayathe.project.scheduleapp.R
import ayathe.project.scheduleapp.adapter.EventAdapter
import ayathe.project.scheduleapp.data.Event
import ayathe.project.scheduleapp.data.EventCV
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Document

class UserRepository {

    private var auth = FirebaseAuth.getInstance()
    private val user = Firebase.auth.currentUser
    private val debug = "DEBUG"
    private val cloud = FirebaseFirestore.getInstance()
    private lateinit var eventArrayList: ArrayList<EventCV>
    private lateinit var eventAdapter: EventAdapter

    fun changePassword(password: String){
        user!!.updatePassword(password).addOnSuccessListener {
            Log.d(debug, "Password change success.")
        }.addOnFailureListener {
            Log.d(debug, "Password change failure.")
        }
    }

    fun changeEmail(email: String){
        user!!.updateEmail(email)
            .addOnSuccessListener {
                Log.d(debug, "Email change success.")
            }.addOnFailureListener {
                Log.d(debug, "Email change failure.")
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

    fun eventChangeListener(recyclerView: RecyclerView){


        eventArrayList = arrayListOf()
        eventAdapter = EventAdapter(eventArrayList)
        recyclerView.adapter = eventAdapter

        cloud.collection("events").addSnapshotListener(object: EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null){
                    Log.e("Error loading Events.",
                        error.message.toString())
                    return
                }
                for(dc: DocumentChange in value?.documentChanges!!){

                    if(dc.type == DocumentChange.Type.ADDED){
                        eventArrayList.add(dc.document.toObject(EventCV::class.java))
                    }
                }
            }

        })
    }




}