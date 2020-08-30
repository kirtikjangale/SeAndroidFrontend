package com.example.seandroidproject.activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.viewpager.widget.ViewPager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.seandroidproject.R
import com.example.seandroidproject.adapter.ViewPagerAdapter
import com.kirtik.foodrunner.util.ConnectionManager
import org.json.JSONObject

class DetailViewTextWasteActivity : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences
    lateinit var viewpager : ViewPager
    lateinit var toolbar : Toolbar
    lateinit var txtPrice : TextView
    lateinit var txtAge : TextView
    lateinit var txtSpecs : TextView
    lateinit var txthead : TextView
    lateinit var txtLocation : TextView
    lateinit var txtPincode : TextView
    lateinit var btnViewProfile : Button
    lateinit var txtAuthor : TextView
    lateinit var txtEdition : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_view_text_waste)


        sharedPreferences = getSharedPreferences(
            getString(R.string.preference_file_name),
            Context.MODE_PRIVATE
        )

        toolbar = findViewById(R.id.toolbar)
        setUpToolbar()

        txthead = findViewById(R.id.txthead)
        txtPrice = findViewById(R.id.txtPrice)
        txtAge = findViewById(R.id.txtAge)
        txtSpecs = findViewById(R.id.txtSpecs)
        txtLocation = findViewById(R.id.txtLocation)
        txtPincode = findViewById(R.id.txtPincode)
        btnViewProfile = findViewById(R.id.btnViewProfile)
        txtAuthor = findViewById(R.id.txtAuthor)
        txtEdition = findViewById(R.id.txtEdition)

        txtPrice.visibility = View.GONE
        txthead.visibility = View.GONE
        txtLocation.visibility = View.GONE
        txtPincode.visibility = View.GONE
        btnViewProfile.visibility = View.GONE
        txtEdition.visibility = View.GONE
        txtAuthor.visibility = View.GONE

        if(ConnectionManager().checkConnectivity(this@DetailViewTextWasteActivity)){
            val queue = Volley.newRequestQueue(this@DetailViewTextWasteActivity)
            val url = "https://se-course-app.herokuapp.com/textwaste/view/5f4bf9a4d7c47000176de337"

            val jsonParams = JSONObject()




            val jsonRequest = @SuppressLint("SetTextI18n")
            object : JsonObjectRequest(Request.Method.GET, url, null,
                Response.Listener {

                    try {
                        println(it)
                        val images = it.getJSONArray("photos")
                        val imageUrls = arrayListOf<String>()
                        for(i in 0 until images.length()){
                            imageUrls.add("https://se-course-app.herokuapp.com/images/"+images.getString(i))

                        }
                        println("urls $imageUrls")

                        val viewPager: ViewPager = findViewById(R.id.viewPager)
                        val adapter = ViewPagerAdapter(this@DetailViewTextWasteActivity, imageUrls)
                        viewPager.adapter = adapter

                        txtPrice.visibility = View.VISIBLE
                        txthead.visibility = View.VISIBLE
                        txtLocation.visibility = View.VISIBLE
                        txtPincode.visibility = View.VISIBLE
                        btnViewProfile.visibility = View.VISIBLE
                        txtAuthor.visibility = View.VISIBLE
                        txtEdition.visibility = View.VISIBLE

                        txtPrice.text = it.getInt("price").toString()
                        txtAge.text = "Used for: ${it.getString("used_for")}"
                        txtSpecs.text = it.getString("description")
                        txtLocation.text = it.getString("location")
                        txtPincode.text = it.getString("pincode")
                        txtAuthor.text = it.getString("author")
                        txtEdition.text = "${it.getString("edition")}th Edition"



                    } catch (e: Exception) {
                        println(e)
                        Toast.makeText(this@DetailViewTextWasteActivity, "Exception", Toast.LENGTH_SHORT)
                            .show()
                    }

                }, Response.ErrorListener {
                    Toast.makeText(this@DetailViewTextWasteActivity, "$it", Toast.LENGTH_SHORT).show()
                }){

                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["content-type"] = "application/json"
                    headers["Authorization"] = "Bearer "+ sharedPreferences.getString(
                        "userToken",
                        "-1"
                    ).toString()
                    return  headers
                }
            }


            queue.add(jsonRequest)
        }
        else{
            val dialog = AlertDialog.Builder(this@DetailViewTextWasteActivity)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection Not Found")
            dialog.setPositiveButton("Open Settings"){ _, _->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                finish()
            }
            dialog.setNegativeButton("Cancel"){ _, _->
                ActivityCompat.finishAffinity(this@DetailViewTextWasteActivity)
            }
            dialog.create()
            dialog.show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        if(id == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUpToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Item Name"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }
}