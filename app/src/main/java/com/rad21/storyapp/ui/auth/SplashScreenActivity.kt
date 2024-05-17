package com.rad21.storyapp.ui.auth

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.rad21.storyapp.R

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private val SPLASH_TIME_OUT: Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, SPLASH_TIME_OUT)

    }
}