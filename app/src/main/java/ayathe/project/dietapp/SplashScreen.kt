package ayathe.project.dietapp

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import ayathe.project.dietapp.registerlogin.activityreglog.LogRegActivity
import com.mikhaellopez.circularprogressbar.CircularProgressBar

@SuppressLint("CustomSplashScreen")
@Suppress("DEPRECATION")
class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        findViewById<ImageView>(R.id.imageView_splashScreen).clipToOutline = true

        val circularProgressBar = findViewById<CircularProgressBar>(R.id.circularProgressBar)
        circularProgressBar.apply {

            setProgressWithAnimation(200f, 3000)
            progressMax = 200f
            progressBarColor = Color.WHITE
            progressBarColorDirection = CircularProgressBar.GradientDirection.TOP_TO_BOTTOM

            backgroundProgressBarColor = Color.GRAY
            backgroundProgressBarColorDirection = CircularProgressBar.GradientDirection.TOP_TO_BOTTOM
            progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT
        }

        Handler().postDelayed({
            val intent = Intent(this, LogRegActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}