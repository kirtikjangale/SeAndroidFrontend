package com.example.seandroidproject.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.seandroidproject.R
import com.kirtik.foodrunner.util.ConnectionManager
import org.json.JSONObject
import java.io.IOException
import java.lang.Exception

class RegisterActivity : AppCompatActivity() {


    lateinit var etUserName : EditText
    lateinit var etUserEmail : EditText
    lateinit var etUserPhone : EditText
    lateinit var etUserAddress : EditText
    lateinit var etUserPinCode : EditText
    lateinit var etUserPassword : EditText
    lateinit var etUserPasswordConfirm : EditText
    lateinit var btnRegister : Button


    lateinit var sharedPreferences: SharedPreferences



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        supportActionBar?.hide()


        etUserName = findViewById(R.id.etUserName)
        etUserEmail = findViewById(R.id.etUserEmail)
        etUserPhone = findViewById(R.id.etUserPhone)
        etUserAddress = findViewById(R.id.etUserAddress)
        etUserPinCode = findViewById(R.id.etUserPinCode)
        etUserPassword = findViewById(R.id.etUserPassword)
        etUserPasswordConfirm = findViewById(R.id.etUserPasswordConfirm)
        btnRegister = findViewById(R.id.btnRegister)

        sharedPreferences = getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)


        btnRegister.setOnClickListener {
            if( checkName(etUserName.text.toString()) && checkEmail(etUserEmail.text.toString()) &&
                checkMobile(etUserPhone.text.toString()) && checkAddress(etUserAddress.text.toString()) &&
                checkPinCode(etUserPinCode.text.toString())
                && checkPassword(etUserPassword.text.toString())
                && confirmPassword(etUserPassword.text.toString(),etUserPasswordConfirm.text.toString())){

                if(ConnectionManager().checkConnectivity(this@RegisterActivity)){
                    val queue = Volley.newRequestQueue(this@RegisterActivity)
                    val url = "https://se-course-app.herokuapp.com/users/create"

                    val jsonParams = JSONObject()

                    jsonParams.put("name",etUserName.text.toString())
                    jsonParams.put("email",etUserEmail.text.toString())
                    jsonParams.put("password",etUserPassword.text.toString())
                    jsonParams.put("phone",etUserPhone.text.toString())
                    jsonParams.put("location",etUserAddress.text.toString())
                    jsonParams.put("pincode",etUserPinCode.text.toString())

                    //println(jsonParams)

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

                                val intent = Intent(this@RegisterActivity,HomePageActivity::class.java)
                                startActivity(intent)
                                finish()

                            }
                            catch (e : Exception){
                                println(e)
                                Toast.makeText(this@RegisterActivity,"Exception",Toast.LENGTH_SHORT).show()
                            }

                        },Response.ErrorListener {
                            Toast.makeText(this@RegisterActivity,"$it",Toast.LENGTH_SHORT).show()
                        }){

                        override fun getHeaders(): MutableMap<String, String> {
                            val headers = HashMap<String,String>()
                            headers["content-type"] = "application/json"
                            return  headers
                        }
                    }


                    queue.add(jsonRequest)
                }
                else{
                    val dialog = AlertDialog.Builder(this@RegisterActivity)
                    dialog.setTitle("Error")
                    dialog.setMessage("Internet Connection Not Found")
                    dialog.setPositiveButton("Open Settings"){_,_->
                        val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                        startActivity(settingsIntent)
                        finish()
                    }
                    dialog.setNegativeButton("Cancel"){ _,_->
                        ActivityCompat.finishAffinity(this@RegisterActivity)
                    }
                    dialog.create()
                    dialog.show()
                }

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

    private fun checkPinCode(number:String) : Boolean{
        val ans = number.isNotEmpty()
        if(!ans){
            Toast.makeText(this@RegisterActivity,"Invalid PinCode", Toast.LENGTH_SHORT).show()
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