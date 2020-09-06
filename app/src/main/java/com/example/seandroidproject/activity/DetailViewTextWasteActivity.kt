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
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.seandroidproject.R
import com.example.seandroidproject.adapter.ViewPagerAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.kirtik.foodrunner.util.ConnectionManager
import com.squareup.picasso.Picasso
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
    lateinit var imgNavigate : ImageView

    //id
    var id : String? = null
    //

    var sellerDpUrl : String? = null

    lateinit var loader : RelativeLayout

    lateinit var sellerName : TextView
    lateinit var sellerPhone : TextView
    lateinit var sellerEmail : TextView
    lateinit var sellerPic : ImageView


    var sliderDotspanel: LinearLayout? = null
    private var dotscount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_view_text_waste)


        sharedPreferences = getSharedPreferences(
            getString(R.string.preference_file_name),
            Context.MODE_PRIVATE
        )

        loader = findViewById(R.id.progressBar)
        sliderDotspanel = findViewById(R.id.SliderDots)

        //BottomSheet view
        val view = layoutInflater.inflate(R.layout.bottom_sheet_userinfo, null)
        val bottomSheetDialog = BottomSheetDialog(this@DetailViewTextWasteActivity)
        bottomSheetDialog.setContentView(view)

        sellerName = view.findViewById(R.id.txtSellerName)
        sellerPhone = view.findViewById(R.id.txtSellerPhone)
        sellerEmail = view.findViewById(R.id.txtSellerEmail)
        sellerPic = view.findViewById(R.id.imgSellerPic)


        var sellerId : String? = null
        //

        toolbar = findViewById(R.id.toolbar)


        txthead = findViewById(R.id.txthead)
        txtPrice = findViewById(R.id.txtPrice)
        txtAge = findViewById(R.id.txtAge)
        txtSpecs = findViewById(R.id.txtSpecs)
        txtLocation = findViewById(R.id.txtLocation)
        txtPincode = findViewById(R.id.txtPincode)
        btnViewProfile = findViewById(R.id.btnViewProfile)
        txtAuthor = findViewById(R.id.txtAuthor)
        txtEdition = findViewById(R.id.txtEdition)
        imgNavigate = findViewById(R.id.imgNavigate)

        txtPrice.visibility = View.GONE
        txthead.visibility = View.GONE
        txtLocation.visibility = View.GONE
        txtPincode.visibility = View.GONE
        btnViewProfile.visibility = View.GONE
        txtEdition.visibility = View.GONE
        txtAuthor.visibility = View.GONE


        if(intent != null){
            id = intent.getStringExtra("_id")

        }
        else {
            Toast.makeText(this@DetailViewTextWasteActivity, "Intent is null", Toast.LENGTH_SHORT).show()
            finish()
        }

        imgNavigate.setOnClickListener {

            var query = "${txtLocation.text}+${txtPincode.text}"
//            var gmmIntentUri = Uri.parse("geo:0,0?q=$query")
//            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
//            mapIntent.setPackage("com.google.android.apps.maps")
//            startActivity(mapIntent)

            val gmmIntentUri =
                Uri.parse("google.navigation:q=$query")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)



        }
        if(ConnectionManager().checkConnectivity(this@DetailViewTextWasteActivity)){
            val queue = Volley.newRequestQueue(this@DetailViewTextWasteActivity)
            var url : String = ""

            if(!sharedPreferences.getBoolean("isLoggedIn",false))
                url = "https://se-course-app.herokuapp.com/textwaste/view_noauth/$id"
            else
                url = "https://se-course-app.herokuapp.com/textwaste/view/$id"

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

                        //////////////////////////////////////////////////////////////////////////////////////
                        dotscount = adapter.getCount()


                        val dots = arrayOfNulls<ImageView>(dotscount)

                        for (i in 0 until dotscount) {
                            dots[i] = ImageView(this@DetailViewTextWasteActivity)
                            dots[i]?.setImageDrawable(
                                ContextCompat.getDrawable(
                                    applicationContext,
                                    R.drawable.non_active_dot
                                )
                            )
                            val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                            params.setMargins(8, 0, 8, 0)
                            sliderDotspanel!!.addView(dots[i], params)
                        }

                        dots[0]?.setImageDrawable(
                            ContextCompat.getDrawable(
                                applicationContext,
                                R.drawable.active_dot
                            )
                        )
                        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                            override fun onPageScrolled(
                                position: Int,
                                positionOffset: Float,
                                positionOffsetPixels: Int
                            ) {
                            }

                            override fun onPageSelected(position: Int) {
                                for (i in 0 until dotscount) {
                                    dots[i]?.setImageDrawable(
                                        ContextCompat.getDrawable(
                                            applicationContext,
                                            R.drawable.non_active_dot
                                        )
                                    )
                                }
                                dots[position]?.setImageDrawable(
                                    ContextCompat.getDrawable(
                                        applicationContext,
                                        R.drawable.active_dot
                                    )
                                )
                            }

                            override fun onPageScrollStateChanged(state: Int) {}
                        })


                        ////////////////////////////////////////////////////////////////////////////////////////

                        setUpToolbar(it.getString("name"))

                        txtPrice.visibility = View.VISIBLE
                        txthead.visibility = View.VISIBLE
                        txtLocation.visibility = View.VISIBLE
                        txtPincode.visibility = View.VISIBLE
                        btnViewProfile.visibility = View.VISIBLE
                        txtAuthor.visibility = View.VISIBLE
                        txtEdition.visibility = View.VISIBLE
                        loader.visibility = View.GONE

                        txtPrice.text = it.getInt("price").toString()
                        txtAge.text = "Used for: ${it.getString("used_for")}"
                        txtSpecs.text = it.getString("description")
                        txtLocation.text = it.getString("location")
                        txtPincode.text = it.getString("pincode")
                        txtAuthor.text = it.getString("author")
                        txtEdition.text = "${it.getString("edition")}th Edition"


                        ///////////////////////////////////////////////////////////////
                        if(sharedPreferences.getBoolean("isLoggedIn",false)) {

                            sellerId = it.getString("owner")

                            val url2 = "https://se-course-app.herokuapp.com/users/other/$sellerId"
                            val accRequest = @SuppressLint("SetTextI18n")
                            object : JsonObjectRequest(Request.Method.GET, url2, null,
                                Response.Listener {

                                    try {
                                        sellerName.text = it.getString("name")
                                        sellerPhone.text = it.getString("phone")
                                        sellerEmail.text = it.getString("email")
                                        try {
                                            sellerDpUrl = it.getString("dp_url")
                                        } catch (e: Exception) {

                                        }

                                    } catch (e: Exception) {
                                        println(e)
                                        Toast.makeText(
                                            this@DetailViewTextWasteActivity,
                                            "Exception",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }

                                }, Response.ErrorListener {
                                    Toast.makeText(
                                        this@DetailViewTextWasteActivity,
                                        "$it",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }) {

                                override fun getHeaders(): MutableMap<String, String> {
                                    val headers = HashMap<String, String>()
                                    headers["content-type"] = "application/json"
                                    headers["Authorization"] =
                                        "Bearer " + sharedPreferences.getString(
                                            "userToken",
                                            "-1"
                                        ).toString()
                                    return headers
                                }
                            }

                            queue.add(accRequest)
                        }
                        /////////////////////////////////////////////////


                    } catch (e: Exception) {
                        loader.visibility = View.GONE
                        println(e)
                        Toast.makeText(this@DetailViewTextWasteActivity, "$e", Toast.LENGTH_SHORT)
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
            loader.visibility = View.GONE
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

        btnViewProfile.setOnClickListener {
            if(sharedPreferences.getBoolean("isLoggedIn",false)){
                bottomSheetDialog.show()

                Picasso.get().load("https://se-course-app.herokuapp.com/images/${sellerDpUrl}").fit()
                    .centerCrop()
                    .error(R.drawable.addphotodark)
                    .placeholder(R.drawable.loading)
                    .into(sellerPic)

                sellerPhone.setOnClickListener {
                    val callIntent = Intent(Intent.ACTION_DIAL)
                    callIntent.data = Uri.parse("tel:"+sellerPhone.text.toString())
                    startActivity(callIntent)
                }
            }
            else{
                Toast.makeText(this@DetailViewTextWasteActivity,"Login to know owner",Toast.LENGTH_SHORT).show()
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