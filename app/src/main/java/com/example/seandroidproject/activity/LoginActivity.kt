package com.example.seandroidproject.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.seandroidproject.R
import com.kirtik.foodrunner.util.ConnectionManager
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject
import java.lang.Exception

class LoginActivity : AppCompatActivity() {

    lateinit var etMobileNumber : EditText
    lateinit var etPasssword : EditText
    lateinit var btnLogin : Button
    lateinit var txtforgotPassword : TextView
    lateinit var txtRegister : TextView
    lateinit var txtContinueNoLogin: TextView

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()

        etMobileNumber = findViewById(R.id.etMobileNumber)
        etPasssword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        txtforgotPassword = findViewById(R.id.txtForgotPassword)
        txtRegister = findViewById(R.id.txtRegister)
        txtContinueNoLogin = findViewById(R.id.txtContinueNoLogin)

        sharedPreferences = getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)

        txtRegister.setOnClickListener{
            val intent = Intent(this@LoginActivity,RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        txtContinueNoLogin.setOnClickListener{
            val intent = Intent(this@LoginActivity,HomePageActivity::class.java)
            startActivity(intent)
            finish()
        }


        btnLogin.setOnClickListener {
            if(checkMobile(etMobileNumber.text.toString()) && checkPassword(etPasssword.text.toString())){

                if(ConnectionManager().checkConnectivity(this@LoginActivity)){
                    val queue = Volley.newRequestQueue(this@LoginActivity)
                    val url = "https://se-course-app.herokuapp.com/users/login"

                    val jsonParams = JSONObject()

                    jsonParams.put("phone",etMobileNumber.text.toString())
                    jsonParams.put("password",etPasssword.text.toString())

                    val jsonRequest = object : JsonObjectRequest(Request.Method.POST,url,jsonParams,
                        Response.Listener{
                            //println(it)
                            try {
                                val userId = it.getJSONObject("user").getString("_id")
                                val userName = it.getJSONObject("user").getString("name")
                                val userEmail = it.getJSONObject("user").getString("email")
                                val userPhone = it.getJSONObject("user").getString("phone")
                                val userLocation = it.getJSONObject("user").getString("location")
                                val userPinCode = it.getJSONObject("user").getString("pincode")

                                val userToken = it.getString("token")

                                //println("$userId \n $userName \n $userEmail \n $userPhone \n $userLocation \n $userPinCode \n $userToken")


                                savePreferences(userId,userName,userEmail,userPhone,userLocation,userPinCode,userToken)

                                val intent = Intent(this@LoginActivity,HomePageActivity::class.java)
                                startActivity(intent)
                                finish()

                            }
                            catch (e : Exception){
                                println(e)
                                Toast.makeText(this@LoginActivity,"Exception",Toast.LENGTH_SHORT).show()
                            }

                        },Response.ErrorListener {
                            Toast.makeText(this@LoginActivity,"$it",Toast.LENGTH_SHORT).show()
                        }){

                        override fun getHeaders(): MutableMap<String, String> {
                            val headers = HashMap<String,String>()
                            headers["content-type"] = "application/json"
                            headers["Authorization"] = "Bearer "+ sharedPreferences.getString("userToken","-1").toString()
                            return  headers
                        }
                    }


                    queue.add(jsonRequest)
                }
                else{
                    val dialog = AlertDialog.Builder(this@LoginActivity)
                    dialog.setTitle("Error")
                    dialog.setMessage("Internet Connection Not Found")
                    dialog.setPositiveButton("Open Settings"){_,_->
                        val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                        startActivity(settingsIntent)
                        finish()
                    }
                    dialog.setNegativeButton("Cancel"){ _,_->
                        ActivityCompat.finishAffinity(this@LoginActivity)
                    }
                    dialog.create()
                    dialog.show()
                }

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

    fun savePreferences(userId:String,userName:String,userEmail:String,userPhone:String,userLocation:String,userPinCode : String,userToken:String){
        sharedPreferences.edit().putBoolean("isLoggedIn",true).apply()
        sharedPreferences.edit().putString("userName",userName).apply()
        sharedPreferences.edit().putString("userEmail",userEmail).apply()
        sharedPreferences.edit().putString("userPhone",userPhone).apply()
        sharedPreferences.edit().putString("userLocation",userLocation).apply()
        sharedPreferences.edit().putString("userPinCode",userPinCode).apply()
        sharedPreferences.edit().putString("userToken",userToken).apply()
    }
}