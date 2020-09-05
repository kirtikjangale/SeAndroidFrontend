package com.example.seandroidproject.activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
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
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.kirtik.foodrunner.util.ConnectionManager
import kotlinx.android.synthetic.main.activity_detail_view_notewaste.*
import org.json.JSONObject

class DetailViewNotewasteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_view_notewaste)

        lateinit var sharedPreferences: SharedPreferences
        lateinit var viewpager : ViewPager
        lateinit var toolbar : Toolbar
        lateinit var txtPrice : TextView
        lateinit var txtSpecs : TextView
        lateinit var txthead : TextView
        lateinit var txtLocation : TextView
        lateinit var txtPincode : TextView
        lateinit var btnViewProfile : Button

        //id
        var id : String? = null
        //

        lateinit var sellerName : TextView
        lateinit var sellerPhone : TextView
        lateinit var sellerEmail : TextView
        lateinit var sellerPic : ImageView

        sharedPreferences = getSharedPreferences(
            getString(R.string.preference_file_name),
            Context.MODE_PRIVATE
        )

        //BottomSheet view
        val view = layoutInflater.inflate(R.layout.bottom_sheet_userinfo, null)
        val bottomSheetDialog = BottomSheetDialog(this@DetailViewNotewasteActivity)
        bottomSheetDialog.setContentView(view)

        sellerName = view.findViewById(R.id.txtSellerName)
        sellerPhone = view.findViewById(R.id.txtSellerPhone)
        sellerEmail = view.findViewById(R.id.txtSellerEmail)

        var sellerId : String? = null
        //


        toolbar = findViewById(R.id.toolbar)

        txthead = findViewById(R.id.txthead)
        txtPrice = findViewById(R.id.txtPrice)
        txtSpecs = findViewById(R.id.txtSpecs)
        txtLocation = findViewById(R.id.txtLocation)
        txtPincode = findViewById(R.id.txtPincode)
        btnViewProfile = findViewById(R.id.btnViewProfile)

        txtPrice.visibility = View.GONE
        txthead.visibility = View.GONE
        txtLocation.visibility = View.GONE
        txtPincode.visibility = View.GONE
        btnViewProfile.visibility = View.GONE

        if(intent != null){
            id = intent.getStringExtra("_id")

        }
        else {
            Toast.makeText(this@DetailViewNotewasteActivity, "Intent is null", Toast.LENGTH_SHORT).show()
            finish()
        }



        if(ConnectionManager().checkConnectivity(this@DetailViewNotewasteActivity)){
            val queue = Volley.newRequestQueue(this@DetailViewNotewasteActivity)
            val url = "https://se-course-app.herokuapp.com/notewaste/view/$id"


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
                        val adapter = ViewPagerAdapter(this@DetailViewNotewasteActivity, imageUrls)
                        viewPager.adapter = adapter

                        setUpToolbar(it.getString("name"))

                        txtPrice.visibility = View.VISIBLE
                        txthead.visibility = View.VISIBLE
                        txtLocation.visibility = View.VISIBLE
                        txtPincode.visibility = View.VISIBLE
                        btnViewProfile.visibility = View.VISIBLE

                        txtPrice.text = it.getInt("price").toString()
                        txtSpecs.text = it.getString("description")
                        txtLocation.text = it.getString("location")
                        txtPincode.text = it.getString("pincode")

                        sellerId = it.getString("owner")

                        val url2 = "https://se-course-app.herokuapp.com/users/other/$sellerId"
                        val accRequest = @SuppressLint("SetTextI18n")
                        object : JsonObjectRequest(Request.Method.GET, url2, null,
                            Response.Listener {

                                try {
                                    sellerName.text = it.getString("name")
                                    sellerPhone.text = it.getString("phone")
                                    sellerEmail.text = it.getString("email")

                                } catch (e: Exception) {
                                    println(e)
                                    Toast.makeText(
                                        this@DetailViewNotewasteActivity,
                                        "Exception",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }

                            }, Response.ErrorListener {
                                Toast.makeText(this@DetailViewNotewasteActivity, "$it", Toast.LENGTH_SHORT).show()
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

                        queue.add(accRequest)




                    } catch (e: Exception) {
                        println(e)
                        Toast.makeText(this@DetailViewNotewasteActivity, "Exception", Toast.LENGTH_SHORT)
                            .show()
                    }

                }, Response.ErrorListener {
                    Toast.makeText(this@DetailViewNotewasteActivity, "$it", Toast.LENGTH_SHORT).show()
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
            val dialog = AlertDialog.Builder(this@DetailViewNotewasteActivity)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection Not Found")
            dialog.setPositiveButton("Open Settings"){ _, _->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                finish()
            }
            dialog.setNegativeButton("Cancel"){ _, _->
                ActivityCompat.finishAffinity(this@DetailViewNotewasteActivity)
            }
            dialog.create()
            dialog.show()
        }

        btnViewProfile.setOnClickListener {
            bottomSheetDialog.show()

            sellerPhone.setOnClickListener {
                val callIntent = Intent(Intent.ACTION_DIAL)
                callIntent.data = Uri.parse("tel:"+sellerPhone.text.toString())
                startActivity(callIntent)
            }

        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        if(id == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUpToolbar(name:String){
        setSupportActionBar(toolbar)
        supportActionBar?.title = name
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }
}