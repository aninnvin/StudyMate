package com.example.studymate

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val tvLogo = findViewById<TextView>(R.id.tvLogo)
        val tvAppName = findViewById<TextView>(R.id.tvAppName)
        val tvTagline = findViewById<TextView>(R.id.tvTagline)
        val progressBar = findViewById<android.widget.ProgressBar>(R.id.progressBar)

        // Animasi logo scale up
        val scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up)
        tvLogo.startAnimation(scaleUp)

        lifecycleScope.launch {
            delay(500)
            tvAppName.alpha = 1f
            tvAppName.animate().setDuration(800).alpha(1f).start()

            delay(300)
            tvTagline.alpha = 1f
            tvTagline.animate().setDuration(800).alpha(1f).start()

            delay(300)
            progressBar.alpha = 1f
            progressBar.animate().setDuration(500).alpha(1f).start()

            delay(1000)
            val intent = Intent(this@SplashActivity, LoginActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in, R.anim.slide_right)
            finish()
        }
    }
}