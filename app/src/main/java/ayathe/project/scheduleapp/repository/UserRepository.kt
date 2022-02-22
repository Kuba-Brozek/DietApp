package ayathe.project.scheduleapp.repository

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ayathe.project.scheduleapp.R
import ayathe.project.scheduleapp.adapter.EventAdapter
import ayathe.project.scheduleapp.adapter.OnEventClickListener
import ayathe.project.scheduleapp.data.Event
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.fragment_event_info.view.*
import kotlinx.android.synthetic.main.fragment_third.view.*

class UserRepository {

    private var auth = FirebaseAuth.getInstance()
    private val user = Firebase.auth.currentUser
    private val debug = "DEBUG"
    private val doc = "DOC"
    private val cloud = FirebaseFirestore.getInstance()
    private lateinit var eventArrayList: ArrayList<Event>
    private lateinit var eventAdapter: EventAdapter
    private val fbStorage = Firebase.storage
    private val storage = fbStorage.reference

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

    fun showEventInfo(view: View, context: Context, eventName: String){
        val docRef = cloud.collection(auth.currentUser!!.uid).document(eventName)

        docRef.get().addOnSuccessListener { docSnapshot ->
            val event = docSnapshot.toObject<Event>()
            view.event_name.setText(event?.name.toString())
            view.event_date.text = event?.date.toString()
            view.event_description.setText(event?.description.toString())
            try{
                when {
                    event?.category.toString() ==  "biznes" -> {
                        Glide.with(context).load(R.drawable.business).into(view.findViewById(
                            R.id.category_image))
                    }
                    event?.category.toString() == "edukacja" -> {
                        Glide.with(context).load(R.drawable.education).into(view.findViewById(
                            R.id.category_image))
                    }
                    event?.category.toString() == "sprawy domowe" -> {
                        Glide.with(context).load(R.drawable.home).into(view.findViewById(
                            R.id.category_image))
                    }
                    event?.category.toString() == "trening" -> {
                        Glide.with(context).load(R.drawable.work_out).into(view.findViewById(
                            R.id.category_image))
                    }
                    event?.category.toString() == "inne" -> {
                        Glide.with(context).load(R.drawable.other).into(view.findViewById(
                            R.id.category_image))
                    }
                }
            }catch(e: Exception){
                Log.e("error","failed to load image")
            }
        }
    }

    fun loadProfileImage(context: Context, view: View){
        val imageName = "${auth.currentUser!!.uid}.png"
        val uri = storage.child(imageName)
        try {
            uri.downloadUrl.addOnSuccessListener { Uri ->
                val imageURL = Uri.toString()
                Glide.with(context).load(imageURL).into(view.findViewById(R.id.profile_image))
            }
        } catch (e: java.lang.Exception){
           Log.e("Loading Error", "Image loading error into ImageView: profile_picture.")
        }
    }

    fun uploadProfileImage(imageFileUri: Uri){
        val uploadTask = storage.child(auth.currentUser!!.uid).putFile(imageFileUri)
            .addOnSuccessListener {
            Log.i("Upload Image Success", "Profile Image succesfully uploaded to Cloud Storage.")
        }.addOnFailureListener{
            Log.e("Upload Image Failure", "Profile Image upload Error.")
            }
    }

}