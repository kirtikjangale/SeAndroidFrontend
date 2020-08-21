package com.example.seandroidproject.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.seandroidproject.R

class LoginActivity : AppCompatActivity() {

    lateinit var etMobileNumber : EditText
    lateinit var etPasssword : EditText
    lateinit var btnLogin : Button
    lateinit var txtforgotPassword : TextView
    lateinit var txtRegister : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()

        etMobileNumber = findViewById(R.id.etMobileNumber)
        etPasssword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        txtforgotPassword = findViewById(R.id.txtForgotPassword)
        txtRegister = findViewById(R.id.txtRegister)


        txtRegister.setOnClickListener{
            val intent = Intent(this@LoginActivity,RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }


    }
}