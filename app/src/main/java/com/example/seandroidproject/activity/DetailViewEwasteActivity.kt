package com.example.seandroidproject.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.marginBottom
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.seandroidproject.R
import com.example.seandroidproject.adapter.ViewPagerAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.JsonArray
import com.kirtik.foodrunner.util.ConnectionManager
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions
import com.squareup.picasso.Picasso
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException


class DetailViewEwasteActivity : AppCompatActivity() {

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
    lateinit var txtHelper : TextView
    lateinit var faqView: LinearLayout
    lateinit var faqAsk: Button

    lateinit var sellerName : TextView
    lateinit var sellerPhone : TextView
    lateinit var sellerEmail : TextView
    lateinit var sellerPic : ImageView
    lateinit var imgNavigate : ImageView
    lateinit var loader : RelativeLayout
    //id
        var id : String? = null

    //

    var sellerDpUrl : String? = null

    var sliderDotspanel: LinearLayout? = null
    private var dotscount = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_view_ewaste)

        sharedPreferences = getSharedPreferences(
            getString(R.string.preference_file_name),
            Context.MODE_PRIVATE
        )

        loader = findViewById(R.id.progressBar)

        //BottomSheet view
        val view = layoutInflater.inflate(R.layout.bottom_sheet_userinfo, null)
        val bottomSheetDialog = BottomSheetDialog(this@DetailViewEwasteActivity)
        bottomSheetDialog.setContentView(view)

        sellerName = view.findViewById(R.id.txtSellerName)
        sellerPhone = view.findViewById(R.id.txtSellerPhone)
        sellerEmail = view.findViewById(R.id.txtSellerEmail)
        sellerPic = view.findViewById(R.id.imgSellerPic)

        var sellerId : String? = null
        //

        toolbar = findViewById(R.id.toolbar)
        sliderDotspanel = findViewById(R.id.SliderDots)


        txthead = findViewById(R.id.txthead)
        txtPrice = findViewById(R.id.txtPrice)
        txtAge = findViewById(R.id.txtAge)
        txtSpecs = findViewById(R.id.txtSpecs)
        txtLocation = findViewById(R.id.txtLocation)
        txtPincode = findViewById(R.id.txtPincode)
        btnViewProfile = findViewById(R.id.btnViewProfile)
        faqView = findViewById(R.id.faq_section)
        faqAsk = findViewById(R.id.btnFaqAsk)
        imgNavigate = findViewById(R.id.imgNavigate)
        txtHelper = findViewById(R.id.txtHelper)

        txtPrice.visibility = View.GONE
        txthead.visibility = View.GONE
        txtLocation.visibility = View.GONE
        txtPincode.visibility = View.GONE
        btnViewProfile.visibility = View.GONE


        if(intent != null){
            id = intent.getStringExtra("_id")

        }
        else {
            Toast.makeText(this@DetailViewEwasteActivity, "Intent is null", Toast.LENGTH_SHORT).show()
            finish()
        }



        //viewpager = findViewById(R.id.viewPager)

        if(!sharedPreferences.getBoolean("isLoggedIn",false)){
            imgNavigate.visibility = View.GONE
            txtHelper.visibility = View.GONE

        }
        else {
            imgNavigate.visibility = View.VISIBLE
            txtHelper.visibility = View.VISIBLE
        }
        imgNavigate.setOnClickListener {

            val permissions =
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION

                )
            Permissions.check(
                this@DetailViewEwasteActivity /*context*/,
                permissions,
                null /*rationale*/,
                null /*options*/,
                object : PermissionHandler() {
                    override fun onGranted() {
                        Toast.makeText(this@DetailViewEwasteActivity, "PERMISSION_GRANTED", Toast.LENGTH_SHORT).show()
                        var query = "${txtLocation.text}+${txtPincode.text}"

                        val gmmIntentUri =
                            Uri.parse("google.navigation:q=$query")
                        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                        mapIntent.setPackage("com.google.android.apps.maps")
                        startActivity(mapIntent)
                    }

                    override fun onDenied(context: Context?, deniedPermissions: ArrayList<String?>?) {
                        Toast.makeText(this@DetailViewEwasteActivity, "Can't Call until permission is granted", Toast.LENGTH_SHORT).show()

                    }
                })





        }

        if(ConnectionManager().checkConnectivity(this@DetailViewEwasteActivity)){
            val queue = Volley.newRequestQueue(this@DetailViewEwasteActivity)
            var url : String = ""

            if(!sharedPreferences.getBoolean("isLoggedIn", false))
                url = "https://se-course-app.herokuapp.com/ewaste/view_noauth/$id"
            else
                url = "https://se-course-app.herokuapp.com/ewaste/view/$id"

            println("Url:$url")

            val jsonRequest = @SuppressLint("SetTextI18n")
            object : JsonObjectRequest(Request.Method.GET, url, null,
                Response.Listener {
                    println("Url:$url")
                    try {
                        println(it)



                        val faqs: JSONArray = it.getJSONArray("faqs")
                        println("faqs")
                        println(faqs)

                        val images = it.getJSONArray("photos")
                        val imageUrls = arrayListOf<String>()
                        for (i in 0 until images.length()) {
                            imageUrls.add(
                                "https://se-course-app.herokuapp.com/images/" + images.getString(
                                    i
                                )
                            )

                        }
                        println("urls $imageUrls")

                        val viewPager: ViewPager = findViewById(R.id.viewPager)
                        val adapter = ViewPagerAdapter(this@DetailViewEwasteActivity, imageUrls)
                        viewPager.adapter = adapter

                        //////////////////////////////////////////////////////////////////////////////////////
                        dotscount = adapter.getCount()


                        val dots = arrayOfNulls<ImageView>(dotscount)

                        for (i in 0 until dotscount) {
                            dots[i] = ImageView(this@DetailViewEwasteActivity)
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
                        viewPager.addOnPageChangeListener(object : OnPageChangeListener {
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
                        loader.visibility = View.GONE

                        txtPrice.text = it.getInt("price").toString()
                        txtAge.text = "Used for: ${it.getString("used_for")}"
                        txtSpecs.text = it.getString("specifications")


                        txtLocation.text = it.getString("location")




                        txtPincode.text = it.getString("pincode")
                        val item_id = it.getString("_id")

                        var item_owner: String = ""
                        if(it.has("owner")){
                            item_owner = it.getString("owner")
                        }
                        println("item_owner")
                        println(item_owner)

                        for (i in 0 until faqs.length()) {
                            val faq = faqs.getJSONObject(i)
                            println(faq)

                            val faqViewHolder = layoutInflater.inflate(R.layout.faqs_layout, null)
                            faqViewHolder.findViewById<TextView>(R.id.faq_question).text ="Q: ${faq.getString("question")}"
                            if(faq.has("answer")){
                                faqViewHolder.findViewById<TextView>(R.id.faq_answer).text = "A: ${faq.getString("answer")}"
                            }
                            if(faq.has("name")){
                                faqViewHolder.findViewById<TextView>(R.id.faq_user).text = faq.getString("name")
                            }

                            // add answering functionality
                            if(sharedPreferences.getBoolean("isLoggedIn", false) and (sharedPreferences.getString("userId", "-1") == item_owner)){
                                faqViewHolder.findViewById<Button>(R.id.btnFaqAns).visibility = View.VISIBLE

                                faqViewHolder.findViewById<Button>(R.id.btnFaqAns).setOnClickListener {
                                    val dialog = BottomSheetDialog(this)
                                    val dialog_view = layoutInflater.inflate(R.layout.bottom_pincode_dialog, null)
                                    dialog_view.findViewById<TextView>(R.id.txtChangePinCode).text = "Answer the Question"

                                    val answerQuestion: Button = dialog_view.findViewById(R.id.btn_change_pincode)
                                    answerQuestion.text = "Submit"

                                    val editAnswer: EditText = dialog_view.findViewById(R.id.editPinCode)

                                    val cancelPinCodeBtn: Button = dialog_view.findViewById(R.id.btn_cancel_pincode)
                                    cancelPinCodeBtn.setOnClickListener{
                                        dialog.dismiss()
                                    }


                                    answerQuestion.setOnClickListener{

                                        val client = OkHttpClient()

                                        val jsonObject: JSONObject = JSONObject()
                                        jsonObject.put("answer", editAnswer.text)
                                        jsonObject.put("id",faq.getString("_id"))

                                        val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()
                                        val requestBody: RequestBody = jsonObject.toString().toRequestBody(JSON)

                                        // authentication is hardcoded
                                        val request = okhttp3.Request.Builder()
                                            .url("https://se-course-app.herokuapp.com/ewaste/answer/${item_id}")
                                            .addHeader("Authorization", "Bearer ${sharedPreferences.getString("userToken", "-1")}")
                                            .post(requestBody)
                                            .build()

                                        try {
                                            client.newCall(request).enqueue(object : Callback {
                                                override fun onResponse(call: Call, response: okhttp3.Response) {
                                                    runOnUiThread {

                                                        println("Answer ")
                                                        faqViewHolder.findViewById<TextView>(R.id.faq_answer).text = "A: ${editAnswer.text}"
                                                        dialog.dismiss()
                                                    }
                                                }

                                                override fun onFailure(call: Call, e: IOException) {
                                                    println("req. failed")
                                                    dialog.dismiss()
                                                    Toast.makeText(this@DetailViewEwasteActivity, "failed to submit answer, try again later", Toast.LENGTH_SHORT).show()
                                                }
                                            })
                                        }
                                        catch (err: Exception){

                                            val dialog = AlertDialog.Builder(this)
                                            dialog.setTitle("FAQ Error")
                                            dialog.setMessage("Internet Connection Not Found")
                                            dialog.setPositiveButton("Open Settings"){ _, _->
                                                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                                                startActivity(settingsIntent)
                                                this.finish()
                                            }
                                            dialog.setNegativeButton("Cancel"){ _, _->
                                                ActivityCompat.finishAffinity(this)
                                            }
                                            dialog.create()
                                            dialog.show()
                                        }

                                    }

                                    dialog.setContentView(dialog_view)
                                    dialog.show()
                                }

                            }

                            faqView.addView(faqViewHolder)
                        }


                        ////////////////////////////////////////////////
                        if(sharedPreferences.getBoolean("isLoggedIn",false)){
                            faqAsk.setOnClickListener {
                                val dialog = BottomSheetDialog(this)
                                val dialog_view = layoutInflater.inflate(R.layout.bottom_pincode_dialog, null)
                                dialog_view.findViewById<TextView>(R.id.txtChangePinCode).text = "Ask a Question"

                                val askQuestion: Button = dialog_view.findViewById(R.id.btn_change_pincode)
                                askQuestion.text = "Submit"

                                val editQuestion: EditText = dialog_view.findViewById(R.id.editPinCode)

                                val cancelPinCodeBtn: Button = dialog_view.findViewById(R.id.btn_cancel_pincode)
                                cancelPinCodeBtn.setOnClickListener{
                                    dialog.dismiss()
                                }


                                askQuestion.setOnClickListener{
                                    println(editQuestion.text)

                                    val client = OkHttpClient()

                                    val jsonObject: JSONObject = JSONObject()
                                    jsonObject.put("question", editQuestion.text)

                                    val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()
                                    val requestBody: RequestBody = jsonObject.toString().toRequestBody(JSON)

                                    // authentication is hardcoded
                                    val request = okhttp3.Request.Builder()
                                        .url("https://se-course-app.herokuapp.com/ewaste/ask/${item_id}")
                                        .addHeader("Authorization", "Bearer ${sharedPreferences.getString("userToken", "-1")}")
                                        .post(requestBody)
                                        .build()

                                    try {
                                        client.newCall(request).enqueue(object : Callback {
                                            override fun onResponse(call: Call, response: okhttp3.Response) {
                                                runOnUiThread {
                                                    val faqViewHolder = layoutInflater.inflate(R.layout.faqs_layout, null)
                                                    faqViewHolder.findViewById<TextView>(R.id.faq_question).text ="Q: ${editQuestion.text}"
                                                    faqViewHolder.findViewById<TextView>(R.id.faq_user).text = sharedPreferences.getString("userName", "user")
                                                    faqView.addView(faqViewHolder)
                                                    dialog.dismiss()
                                                }
                                            }

                                            override fun onFailure(call: Call, e: IOException) {
                                                println("req. failed")
                                                dialog.dismiss()
                                                Toast.makeText(this@DetailViewEwasteActivity, "failed to submit question, try again later", Toast.LENGTH_SHORT).show()
                                            }
                                        })
                                    }
                                    catch (err: Exception){
                                        val dialog = AlertDialog.Builder(this)
                                        dialog.setTitle("FAQ Error")
                                        dialog.setMessage("Internet Connection Not Found")
                                        dialog.setPositiveButton("Open Settings"){ _, _->
                                            val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                                            startActivity(settingsIntent)
                                            this.finish()
                                        }
                                        dialog.setNegativeButton("Cancel"){ _, _->
                                            ActivityCompat.finishAffinity(this)
                                        }
                                        dialog.create()
                                        dialog.show()
                                    }

                                }

                                dialog.setContentView(dialog_view)
                                dialog.show()
                            }
                        }
                        else{
                            faqAsk.setOnClickListener {
                                Toast.makeText(this, "login to ask questions", Toast.LENGTH_SHORT).show()
                            }
                        }

                        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
                            sellerId = it.getString("owner")

                            val url2 = "https://se-course-app.herokuapp.com/users/other/$sellerId"
                            val accRequest = @SuppressLint("SetTextI18n")
                            object : JsonObjectRequest(
                                Method.GET, url2, null,
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
                                            this@DetailViewEwasteActivity,
                                            "Exception",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }

                                }, Response.ErrorListener {
                                    Toast.makeText(
                                        this@DetailViewEwasteActivity,
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
                        ///////////////////////////////////////////////////////////////


                    } catch (e: Exception) {
                        println(e)


                    }

                }, Response.ErrorListener {
                    loader.visibility = View.GONE
                    Toast.makeText(this@DetailViewEwasteActivity, "$it", Toast.LENGTH_SHORT).show()
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
            val dialog = AlertDialog.Builder(this@DetailViewEwasteActivity)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection Not Found")
            dialog.setPositiveButton("Open Settings"){ _, _->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                finish()
            }
            dialog.setNegativeButton("Cancel"){ _, _->
                ActivityCompat.finishAffinity(this@DetailViewEwasteActivity)
            }
            dialog.create()
            dialog.show()
        }

        btnViewProfile.setOnClickListener {

            if(sharedPreferences.getBoolean("isLoggedIn", false)){
                bottomSheetDialog.show()

                Picasso.get().load("https://se-course-app.herokuapp.com/images/${sellerDpUrl}").fit()
                    .centerCrop()
                    .error(R.drawable.addphotodark)
                    .placeholder(R.drawable.loading)
                    .into(sellerPic)

                sellerPhone.setOnClickListener {

                    val permissions =
                        arrayOf(
                            Manifest.permission.CALL_PHONE
                        )
                    Permissions.check(
                        this@DetailViewEwasteActivity /*context*/,
                        permissions,
                        null /*rationale*/,
                        null /*options*/,
                        object : PermissionHandler() {
                            override fun onGranted() {
                                Toast.makeText(this@DetailViewEwasteActivity, "PERMISSION_GRANTED", Toast.LENGTH_SHORT).show()
                                val callIntent = Intent(Intent.ACTION_DIAL)
                                callIntent.data = Uri.parse("tel:" + sellerPhone.text.toString())
                                startActivity(callIntent)
                            }

                            override fun onDenied(context: Context?, deniedPermissions: ArrayList<String?>?) {
                                Toast.makeText(this@DetailViewEwasteActivity, "Can't Call until permission is granted", Toast.LENGTH_SHORT).show()

                            }
                        })


                }
            }
            else{
                Toast.makeText(
                    this@DetailViewEwasteActivity,
                    "Login to know owner",
                    Toast.LENGTH_SHORT
                ).show()
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
    private fun setUpToolbar(name: String){
        setSupportActionBar(toolbar)
        supportActionBar?.title = name
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }




}