package com.example.seandroidproject.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.example.seandroidproject.R
import java.io.IOException

class RegisterActivity : AppCompatActivity() {


    lateinit var etUserName : EditText
    lateinit var etUserEmail : EditText
    lateinit var etUserPhone : EditText
    lateinit var etUserAddress : EditText
    lateinit var etUserPassword : EditText
    lateinit var etUserPasswordConfirm : EditText
    lateinit var btnRegister : Button






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        supportActionBar?.hide()


        etUserName = findViewById(R.id.etUserName)
        etUserEmail = findViewById(R.id.etUserEmail)
        etUserPhone = findViewById(R.id.etUserPhone)
        etUserAddress = findViewById(R.id.etUserAddress)
        etUserPassword = findViewById(R.id.etUserPassword)
        etUserPasswordConfirm = findViewById(R.id.etUserPasswordConfirm)
        btnRegister = findViewById(R.id.btnRegister)

    }



}