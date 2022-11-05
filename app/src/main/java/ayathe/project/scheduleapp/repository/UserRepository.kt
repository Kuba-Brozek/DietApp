package ayathe.project.scheduleapp.repository

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import ayathe.project.scheduleapp.R
import ayathe.project.scheduleapp.adapter.DayAdapter
import ayathe.project.scheduleapp.adapter.OnEventClickListener
import ayathe.project.scheduleapp.DTO.Day
import ayathe.project.scheduleapp.DTO.User
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.fragment_event_info.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class UserRepository {

    private var auth = FirebaseAuth.getInstance()
    private val user = Firebase.auth.currentUser
    private val debug = "DEBUG"
    private val doc = "DOC"
    private val userCreationTag = "UserCreation"
    private val cloud = FirebaseFirestore.getInstance()
    private lateinit var dayArrayList: ArrayList<Day>
    private lateinit var dayAdapter: DayAdapter
    private val fbStorage = Firebase.storage
    private val storage = fbStorage.reference

    fun changePassword(password: String, context: Context) {
        val success = "Udało się zmienić hasło"
        val failure = "Nie udało się zmienić hasła"
        user!!.updatePassword(password)
            .addOnSuccessListener {
                Log.i(doc, "Password change success.")
                toast(context, success)
            }.addOnFailureListener {
                Log.e(doc, "Password change failure.")
                toast(context, failure)
            }
    }

    fun changeEmail(email: String, context: Context) {
        val success = "Udało się zmienić email"
        val failure = "Nie udało się zmienić adresu email"
        user!!.updateEmail(email)
            .addOnSuccessListener {
                toast(context, success)
                Log.i(debug, "Email change success.")
            }.addOnFailureListener {
                toast(context, failure)
                Log.e(debug, "Email change failure.")
            }
    }

    fun addEvent(day: Day) {
        val eventMap = hashMapOf(
            "category" to day.category,
            "name" to day.name,
            "date" to day.date,
            "description" to day.description
        )
        cloud.collection(auth.currentUser!!.uid).document(day.name.toString())
            .set(eventMap)
            .addOnSuccessListener {
                Log.d(debug, "Event added successfully!")

            }.addOnFailureListener {
                Log.d(debug, "Event adding failure")
            }
    }

    fun eventChangeListener(recyclerView: RecyclerView, listener: OnEventClickListener) {

        dayArrayList = arrayListOf()
        dayAdapter = DayAdapter(dayArrayList, listener)
        recyclerView.adapter = dayAdapter
        cloud.collection(auth.currentUser!!.uid)
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null) {
                        Log.e(
                            "Error loading Events.",
                            error.message.toString()
                        )
                        return
                    }
                    for (doc in value?.documentChanges!!) {

                        if (doc.type == DocumentChange.Type.ADDED) {
                            dayArrayList.add(doc.document.toObject(Day::class.java))
                        }
                    }
                    dayAdapter.notifyDataSetChanged()
                }
            })
    }

    fun deleteItem(documentName: String) {
        cloud.collection(auth.currentUser!!.uid)
            .document(documentName).delete()
            .addOnSuccessListener {
                Log.i(doc, "Event deleted succesfully")
            }.addOnFailureListener {
                Log.e(doc, "Error deleting event")
            }
    }

    fun showUserInfo(): String {
        return auth.currentUser!!.email.toString()
    }


    fun readUserData(myCallback: (User) -> Unit) {
        cloud.collection(auth.currentUser!!.uid).document("userInfo").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = task.result!!.toObject<User>()!!
                    myCallback(user)
                }
            }
        }




    fun showEventInfo(view: View, context: Context, eventName: String) {
        val docRef = cloud.collection(auth.currentUser!!.uid).document(eventName)

        docRef.get().addOnSuccessListener { docSnapshot ->
            val event = docSnapshot.toObject<Day>()
            view.event_name.setText(event?.name.toString())
            view.event_date.text = event?.date.toString()
            view.event_description.setText(event?.description.toString())
            try {
                when {
                    event?.category.toString() == "biznes" -> {
                        Glide.with(context).load(R.drawable.business).into(
                            view.findViewById(
                                R.id.category_image
                            )
                        )
                    }
                    event?.category.toString() == "edukacja" -> {
                        Glide.with(context).load(R.drawable.education).into(
                            view.findViewById(
                                R.id.category_image
                            )
                        )
                    }
                    event?.category.toString() == "sprawy domowe" -> {
                        Glide.with(context).load(R.drawable.home).into(
                            view.findViewById(
                                R.id.category_image
                            )
                        )
                    }
                    event?.category.toString() == "trening" -> {
                        Glide.with(context).load(R.drawable.work_out).into(
                            view.findViewById(
                                R.id.category_image
                            )
                        )
                    }
                    event?.category.toString() == "inne" -> {
                        Glide.with(context).load(R.drawable.other).into(
                            view.findViewById(
                                R.id.category_image
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("error", "failed to load image")
            }
        }
    }

    fun loadProfileImage(context: Context, imageView: ImageView) {
        val imageName = auth.currentUser!!.uid
        val uri = storage.child(imageName)
        if (uri.equals(null).not()) {
            try {
                uri.downloadUrl.addOnSuccessListener { Uri ->
                    val imageURL = Uri.toString()
                    Glide.with(context).load(imageURL)
                        .circleCrop().into(imageView)
                }
            } catch (e: Exception) {
                Log.e("Loading Error", "Image loading error into ImageView: profile_picture.")
            }
        } else {
            Log.i("Image Error", "Current user doesn't have a profile picture.")
        }
    }

    fun uploadProfileImage(imageFileUri: Uri) {
        val uid = auth.currentUser!!.uid
        if (storage.child(uid).equals(null)) {
            Log.i("Auth error", " Current user authentification ended with error.")
        } else {
            storage.child(uid).putFile(imageFileUri)
                .addOnSuccessListener {
                    Log.i("Upload Image Success",
                        "Profile Image succesfully uploaded to Cloud Storage."
                    )
                }.addOnFailureListener {
                    Log.e("Upload Image Failure",
                        "Profile Image upload Error.")
                }

        }
    }

    fun addUserToDatabase(user: User) { //working
        val userMap = hashMapOf(
            "userId" to user.userId,
            "username" to user.username,
            "email" to user.email,
            "image" to user.image,
            "age" to user.age,
            "weight" to user.weight,
            "height" to user.height,
            "destination" to user.destination
        )

        cloud.collection(auth.currentUser!!.uid)
            .document("userInfo")
            .set(userMap)
            .addOnSuccessListener {
                Log.i(userCreationTag,
                    "User added to database")
            }.addOnFailureListener {
                Log.e(userCreationTag,
                    "User NOT added to database")
            }
    }

    private fun toast(context: Context, string: String) { //working
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show()
    }


}