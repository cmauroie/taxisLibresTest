package col.rgm.taxislibrestest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.RelativeLayout



class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        this.supportActionBar?.hide()
        iniciarAnimacion()
    }

    fun iniciarAnimacion(){
        val contenedor = findViewById(R.id.splash_container) as RelativeLayout
        val animation = AnimationUtils.loadAnimation(this, R.anim.slide_up)
        animation.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationStart(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                var startIntent: Intent? = null
                startIntent = Intent(applicationContext, MainActivity::class.java)
                startActivity((startIntent))
            }
        })
        //animation.startNow()
        contenedor.startAnimation(animation)
    }


}
