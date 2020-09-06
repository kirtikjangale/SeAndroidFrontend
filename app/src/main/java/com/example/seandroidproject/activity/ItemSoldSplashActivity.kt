package com.example.seandroidproject.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.seandroidproject.R

class ItemSoldSplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_sold)

        supportActionBar?.hide()
        Handler().postDelayed({
            val intent = Intent(this@ItemSoldSplashActivity,HomePageActivity::class.java)
            startActivity(intent)
            finish()
        },2000)
    }
}