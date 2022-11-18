package ayathe.project.dietapp.home.homefragment

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import ayathe.project.dietapp.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_home.view.*


class HomeFragment : Fragment() {

    private val homeFrVM by viewModels<ViewModelHomeFragment>()
    private val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    @SuppressLint("Recycle")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)
        // Inflate the layout for this fragment
        view.plus_btn.setOnClickListener {
            val add10kcal = view.current_kcal_ET.text.toString().toInt().plus(10)
            view.current_kcal_ET.setText(add10kcal.toString(), TextView.BufferType.EDITABLE)
        }
        view.minus_btn.setOnClickListener {
            val remove10kcal = view.current_kcal_ET.text.toString().toInt().minus(10)
            view.current_kcal_ET.setText(remove10kcal.toString(), TextView.BufferType.EDITABLE)
        }

            view.add_btn.setOnClickListener{
                val a = view.over_all_kcal_TV.text.toString().toInt()
                val b = view.over_all_kcal_TV.text.toString().toInt().plus(view.current_kcal_ET.text.toString().toInt())
                animateTextView(a, b, view.over_all_kcal_TV)
        }

//        view.add_btn.setOnClickListener{
//            val overallValue = view.current_kcal_ET.text.toString()
//                .toInt().plus(view.over_all_kcal_TV.text.toString().toInt())
//            view.over_all_kcal_TV.text = overallValue.toString()
//        }

//        CoroutineScope(Dispatchers.IO).launch{
//            view.add_btn.setOnClickListener {
//
//                add(view)
//            }
//        }


        return view
    }
}

fun animateTextView(initialValue: Int, finalValue: Int, textview: TextView) {
    val valueAnimator = ValueAnimator.ofInt(initialValue, finalValue)
    valueAnimator.duration = 1500
    valueAnimator.addUpdateListener {
        textview.text = valueAnimator.animatedValue.toString()
    }
    valueAnimator.start()
}