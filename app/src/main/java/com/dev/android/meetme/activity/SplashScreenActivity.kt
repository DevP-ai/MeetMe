package com.dev.android.meetme.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.os.HandlerCompat.postDelayed
import com.dev.android.meetme.MainActivity
import com.dev.android.meetme.R
import com.dev.android.meetme.auth.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        supportActionBar?.hide()
        val user=FirebaseAuth.getInstance().currentUser

        Handler(Looper.getMainLooper()).postDelayed({
             if (user==null)
                 startActivity(Intent(this@SplashScreenActivity,LoginActivity::class.java))
             else
                 startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
            finish()
        },1000)

    }
}