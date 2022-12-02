package ayathe.project.dietapp.fragments.homefragment

import android.animation.ValueAnimator
import android.widget.TextView
import androidx.lifecycle.ViewModel

class ViewModelHomeFragment: ViewModel() {

    fun animateTextView(initialValue: Int, finalValue: Int, textview: TextView) {
        val valueAnimator = ValueAnimator.ofInt(initialValue, finalValue)
        valueAnimator.duration = 1500
        valueAnimator.addUpdateListener {
            textview.text = valueAnimator.animatedValue.toString()
        }
        valueAnimator.start()
    }
}