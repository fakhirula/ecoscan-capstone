package com.example.ecoscan.ui.Splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.ecoscan.R
import com.example.ecoscan.ui.main.MainActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

         Handler().postDelayed({
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
         }, 2000) // SPLASH_DELAY_TIME adalah waktu delay dalam milidetik

    }
}