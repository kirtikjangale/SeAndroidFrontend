package com.example.seandroidproject.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
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


        btnLogin.setOnClickListener {
            if(checkMobile(etMobileNumber.text.toString()) && checkPassword(etPasssword.text.toString())){

            }
        }

    }

    private fun checkMobile(number:String) : Boolean{
        val ans = number.length == 10
        if(!ans){
            Toast.makeText(this@LoginActivity,"Invalid Mobile Number", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun checkPassword(password:String) : Boolean{
        val ans = password.length >= 4
        if(!ans){
            Toast.makeText(this@LoginActivity,"Invalid Password", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}