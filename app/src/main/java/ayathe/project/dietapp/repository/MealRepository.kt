package ayathe.project.dietapp.repository

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ayathe.project.dietapp.DTO.Meal
import ayathe.project.dietapp.R
import ayathe.project.dietapp.adapter.MealAdapter
import ayathe.project.dietapp.adapter.OnMealClickListener
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.fragment_event_info.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MealRepository {

    private var auth = FirebaseAuth.getInstance()
    private val user = Firebase.auth.currentUser
    private val debug = "DEBUG"
    private val doc = "DOC"
    private val userCreationTag = "UserCreation"
    private val cloud = FirebaseFirestore.getInstance()
    private lateinit var mealArrayList: ArrayList<Meal>
    private lateinit var mealAdapter: MealAdapter
    private val fbStorage = Firebase.storage
    private val storage = fbStorage.reference


    fun getMeals(date: String): ArrayList<Meal> {
        val list = arrayListOf<Meal>()
        val a = cloud.collection(auth.currentUser!!.uid)
            .document("Meals").collection(date)
            .get()
            .addOnSuccessListener { doc ->
                for ( document in doc){
                    val documento = document.toObject(Meal::class.java)
                    list.add(documento)
                }
            }
        return list
    }

    @SuppressLint("SimpleDateFormat")
    fun addMeal(meal: Meal) {
        val sdf = SimpleDateFormat("dd.MM.yyyy")
        val currentDate = sdf.format(Date())
        val mealInfo = hashMapOf(
            "cals" to meal.cals,
            "name" to meal.name,
            "date" to meal.date,
            "grams" to meal.grams
        )
        cloud.collection(auth.currentUser!!.uid).document("Meals")
            .collection(currentDate).document(meal.name.toString())
            .set(mealInfo)
            .addOnSuccessListener {
                Log.d(debug, "Meal added successfully!")

            }.addOnFailureListener {
                Log.d(debug, "Meal adding failure")
            }
    }

    @SuppressLint("SetTextI18n")
    fun showMealInfo(view: View, context: Context, mealName: String, mealDate: String) {

        val docRef = cloud.collection(auth.currentUser!!.uid).document("Meals")
            .collection(mealDate).document(mealName)

        docRef.get().addOnSuccessListener { docSnapshot ->
            val meal = docSnapshot.toObject<Meal>()
            view.meal_name.setText(meal?.name.toString())
//            view.meal_name.setText("${meal?.name.toString().substring(0, 7)}..")
            view.meal_date.text = meal?.date.toString()
            view.meal_grams_ET.setText(meal?.grams.toString())
            view.meal_kcal.text = meal?.cals.toString()
            try {
                when {
                    meal?.cals.toString() == "biznes" -> {
                        Glide.with(context).load(R.drawable.business).into(
                            view.findViewById(
                                R.id.category_image
                            )
                        )
                    }
                    meal?.cals.toString() == "edukacja" -> {
                        Glide.with(context).load(R.drawable.education).into(
                            view.findViewById(
                                R.id.category_image
                            )
                        )
                    }
                    meal?.cals.toString() == "sprawy domowe" -> {
                        Glide.with(context).load(R.drawable.home).into(
                            view.findViewById(
                                R.id.category_image
                            )
                        )
                    }
                    meal?.cals.toString() == "trening" -> {
                        Glide.with(context).load(R.drawable.work_out).into(
                            view.findViewById(
                                R.id.category_image
                            )
                        )
                    }
                    meal?.cals.toString() == "inne" -> {
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


    fun deleteMeal(mealDate: String, mealName: String) {
        cloud.collection(auth.currentUser!!.uid).document("Meals")
            .collection(mealDate).document(mealName).delete()
            .addOnSuccessListener {
                Log.i(doc, "Meal deleted succesfully")
            }.addOnFailureListener {
                Log.e(doc, "Error deleting meal")
            }
    }

    @SuppressLint("SimpleDateFormat")
    fun eventChangeListener(recyclerView: RecyclerView, listener: OnMealClickListener, date: String) {
        mealArrayList = arrayListOf()
        mealAdapter = MealAdapter(mealArrayList, listener)
        recyclerView.adapter = mealAdapter
        cloud.collection(auth.currentUser!!.uid)
            .document("Meals").collection(date)
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onEvent(value: QuerySnapshot?,
                                     error: FirebaseFirestoreException?) {
                    if (error != null) {
                        Log.e(
                            "Error loading meals.",
                            error.message.toString()
                        )
                        return
                    }
                    for (doc in value?.documentChanges!!) {

                        if (doc.type == DocumentChange.Type.ADDED) {
                            mealArrayList.add(doc.document.toObject(Meal::class.java))
                        }
                    }
                    mealAdapter.notifyDataSetChanged()
                }
            })
    }

}