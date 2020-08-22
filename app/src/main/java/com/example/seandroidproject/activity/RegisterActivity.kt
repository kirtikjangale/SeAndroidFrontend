package com.example.seandroidproject.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
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


        btnRegister.setOnClickListener {
            if( checkName(etUserName.text.toString()) && checkEmail(etUserEmail.text.toString()) &&
                checkMobile(etUserPhone.text.toString()) && checkAddress(etUserAddress.text.toString()) && checkPassword(etUserPassword.text.toString())
                && confirmPassword(etUserPassword.text.toString(),etUserPasswordConfirm.text.toString())){

            }

        }

    }

    private fun checkName(name:String) : Boolean{
        if(name.length < 3){
            Toast.makeText(this@RegisterActivity,"Invalid Name. Length must be greater than 2",Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun checkEmail(email:String) :Boolean{
        val ans = Patterns.EMAIL_ADDRESS.toRegex().matches(email)
        if (!ans){
            Toast.makeText(this@RegisterActivity, "Invalid Email", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }



    private fun checkMobile(number:String) : Boolean{
        val ans = number.length == 10
        if(!ans){
            Toast.makeText(this@RegisterActivity,"Invalid Mobile Number", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun checkPassword(password:String) : Boolean{
        val ans = password.length >= 4
        if(!ans){
            Toast.makeText(this@RegisterActivity,"Password length should be greater than 3",Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun checkAddress(address:String) : Boolean{
        val ans = address.isNotEmpty()
        if(!ans){
            Toast.makeText(this@RegisterActivity,"Invalid Address",Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun confirmPassword(password: String, confirm_password:String) : Boolean{
        val ans = password == confirm_password
        if(!ans){
            Toast.makeText(this@RegisterActivity,"Password and Confirm Password does not match",Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }




}