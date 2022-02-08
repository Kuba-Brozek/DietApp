package ayathe.project.scheduleapp.repository

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ayathe.project.scheduleapp.adapter.EventAdapter
import ayathe.project.scheduleapp.adapter.OnEventClickListener
import ayathe.project.scheduleapp.data.Event
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.core.Tag
import com.google.firebase.firestore.*
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_third.view.*

class UserRepository {

    private var auth = FirebaseAuth.getInstance()
    private val user = Firebase.auth.currentUser
    private val debug = "DEBUG"
    private val doc = "DOC"
    private val cloud = FirebaseFirestore.getInstance()
    private lateinit var eventArrayList: ArrayList<Event>
    private lateinit var eventAdapter: EventAdapter

    fun changePassword(password: String){
        user!!.updatePassword(password).addOnSuccessListener {
            Log.i(doc, "Password change success.")
        }.addOnFailureListener {
            Log.e(doc, "Password change failure.")
        }
    }

    fun changeEmail(email: String){
        user!!.updateEmail(email)
            .addOnSuccessListener {
                Log.i(debug, "Email change success.")
            }.addOnFailureListener {
                Log.e(debug, "Email change failure.")
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
                Log.d(debug, "Event added successfully!")
            }.addOnFailureListener {
                Log.d(debug, "Event adding failure")
            }
    }

    fun eventChangeListener(recyclerView: RecyclerView, listener: OnEventClickListener){

        eventArrayList = arrayListOf()
        eventAdapter = EventAdapter(eventArrayList, listener)
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

    fun deleteItem(documentName: String){
        cloud.collection(auth.currentUser!!.uid)
            .document(documentName).delete()
            .addOnSuccessListener {
                Log.i(doc, "Event deleted succesfully")
            }.addOnFailureListener {
                Log.e(doc, "Error deleting event")
            }
    }

    fun showUserInfo(view: View){
        val email = auth.currentUser!!.email.toString()
        view.email_displayTV.text = email
    }


}