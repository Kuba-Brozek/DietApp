package ayathe.project.scheduleapp.repository

import android.annotation.SuppressLint
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import ayathe.project.scheduleapp.adapter.EventAdapter
import ayathe.project.scheduleapp.data.Event
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.ktx.Firebase

class UserRepository {

    private var auth = FirebaseAuth.getInstance()
    private val user = Firebase.auth.currentUser
    private val debug = "DEBUG"
    private val cloud = FirebaseFirestore.getInstance()
    private lateinit var eventArrayList: ArrayList<Event>
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
            "category" to event.category,
            "name" to event.name,
            "date" to event.date,
            "description" to event.description
        )
        cloud.collection(auth.currentUser!!.uid).document(event.name.toString())
            .set(eventMap).addOnSuccessListener {
                Log.d(debug, "Document added successfully!")
            }.addOnFailureListener {
                Log.d(debug, "Document wasn't added!")
            }
    }

    fun eventChangeListener(recyclerView: RecyclerView){

        eventArrayList = arrayListOf()
        eventAdapter = EventAdapter(eventArrayList)
        recyclerView.adapter = eventAdapter
        cloud.collection(auth.currentUser!!.uid).addSnapshotListener(object: EventListener<QuerySnapshot> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null){
                    Log.e("Error loading Events.",
                        error.message.toString())
                    return
                }
                for(dc: DocumentChange in value?.documentChanges!!){

                    if(dc.type == DocumentChange.Type.ADDED){
                        eventArrayList.add(dc.document.toObject(Event::class.java))
                    }
                }
                eventAdapter.notifyDataSetChanged()
            }

        })
    }




}