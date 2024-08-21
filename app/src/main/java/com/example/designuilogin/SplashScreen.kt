package com.example.designuilogin

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val iv_note = findViewById<ImageView>(R.id.iv_note)
        iv_note.alpha = 0f
        iv_note.animate().alpha(1f).setDuration(2500).withEndAction {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            finish()
        }, 3000)
    }
}
