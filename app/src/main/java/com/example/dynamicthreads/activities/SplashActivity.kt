package com.example.dynamicthreads.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.dynamicthreads.MainActivity
import com.example.dynamicthreads.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        
        val logo:TextView = findViewById(R.id.logo)
        val animation = AnimationUtils.loadAnimation(this, R.anim.logo_scale_animation)
        logo.startAnimation(animation)
        
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            // Replace MainActivity with your main activity
            startActivity(intent)
            finish()
        }, 2000)
        
    }
}