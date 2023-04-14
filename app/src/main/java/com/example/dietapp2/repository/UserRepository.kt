package com.example.dietapp2.repository

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.dietapp2.DTO.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers

class UserRepository {

    private var auth = FirebaseAuth.getInstance()
    private val user = Firebase.auth.currentUser
    private val debug = "DEBUG"
    private val doc = "DOC"
    private val userCreationTag = "UserCreation"
    private val cloud = FirebaseFirestore.getInstance()
    private val fbStorage = Firebase.storage
    private val storage = fbStorage.reference
    private val IODispatcher = Dispatchers.IO

    fun changePassword(password: String, context: Context) {
        val success = "Password change success"
        val failure = "Password change failure"
        user!!.updatePassword(password)
            .addOnSuccessListener {
                Log.i(doc, "Password change success.")
                toast(context, success)
            }.addOnFailureListener {
                Log.e(doc, "Password change failure.")
                toast(context, failure)
            }
    }


    fun readUserData(myCallback: (User) -> Unit) {
        cloud.collection(auth.currentUser!!.uid).document("userInfo").get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = task.result!!.toObject<User>()!!
                    myCallback(user)
                }
            }
    }


    @SuppressLint("SuspiciousIndentation")
    fun loadProfileImage(context: Context, imageView: ImageView) {
        val imageName = auth.currentUser!!.uid
        val storageReference = storage.child(imageName)
        if (storageReference.equals(null).not()) {
            try {
                val uri = storageReference.downloadUrl
                uri.addOnSuccessListener {
                    val imageURL = it.toString()
                    Glide.with(context).load(imageURL)
                        .circleCrop().into(imageView)
                }
                uri.addOnFailureListener {
                    Log.e("Image", "Internet access error")
                }
            } catch (e: Exception) {
                Log.e("Image", "Image loading error into ImageView: profile_picture.")
            }
        } else {
            Log.i("Image", "Current user doesn't have a profile picture.")
        }
    }

    fun uploadProfileImage(imageFileUri: Uri) {
        val uid = auth.currentUser!!.uid
        if (storage.child(uid).equals(null)) {
            Log.i("Auth error", " Current user authentification ended with error.")
        } else {
            storage.child(uid).putFile(imageFileUri)
                .addOnSuccessListener {
                    Log.i(
                        "Image",
                        "Profile Image succesfully uploaded to Cloud Storage."
                    )
                }.addOnFailureListener {
                    Log.e(
                        "Image",
                        "Profile Image upload Error."
                    )
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
            "destination" to user.destination,
            "startingDate" to user.startingDate
        )

        cloud.collection(auth.currentUser!!.uid)
            .document("userInfo")
            .set(userMap)
            .addOnSuccessListener {
                Log.i(userCreationTag, "User added to database")
                Firebase.auth.currentUser!!.updateEmail(user.email!!)
                    .addOnSuccessListener {
                        Log.i(debug, "Email change success.")
                    }.addOnFailureListener {
                        Log.e(debug, "Email change failure.")
                    }

            }.addOnFailureListener {
                Log.e(userCreationTag, "User NOT added to database")
            }
    }

    private fun toast(context: Context, string: String) { //working
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show()
    }

    fun sendEmail(email: String) {
        Firebase.auth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                Log.i("RESET", "Password reset email sent to the user.")
            }.addOnFailureListener {
                Log.i("RESET", "Password reset email not delivered to the user.")
            }
    }


}