package com.example.seandroidproject.activity

//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import com.example.seandroidproject.R
import com.example.seandroidproject.util.FileUtils
import okhttp3.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


class SellActivity : AppCompatActivity() {


    lateinit var imageButton: Button
    lateinit var sendButton: Button
    var imageData: ByteArray? = null
    var uri = arrayListOf<Uri>()
    lateinit var dpImage : Uri

    lateinit var spinner: Spinner
    lateinit var sharedPreferences : SharedPreferences

    lateinit var imgDp : ImageView
    lateinit var itemName : EditText
    lateinit var itemPrice : EditText
    lateinit var itemAge : EditText
    lateinit var itemSpecs : EditText
    lateinit var txtImgSelected : TextView
    lateinit var itemAuthor : EditText
    lateinit var itemEdition : EditText

    lateinit var toolbar : Toolbar
    lateinit var progressBar : RelativeLayout

     var flag : Boolean = false
     var dpfilter = false
     var proceed  = false


    var waste = "ewaste"

    companion object {
        private const val IMAGE_PICK_CODE = 999
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_sell)



        sharedPreferences = getSharedPreferences(
            getString(R.string.preference_file_name),
            Context.MODE_PRIVATE
        )

        itemName = findViewById(R.id.itemName)
        itemPrice = findViewById(R.id.itemPrice)
        itemAge = findViewById(R.id.itemAge)
        itemSpecs = findViewById(R.id.itemSpecs)
        imgDp = findViewById(R.id.imgDp)
        txtImgSelected = findViewById(R.id.txtImgSelected)

        itemAuthor = findViewById(R.id.itemAuthor)
        itemEdition = findViewById(R.id.itemEdition)

        toolbar = findViewById(R.id.toolbar)
        progressBar = findViewById(R.id.indeterminateBar)
        spinner = findViewById(R.id.spinner)

        progressBar.visibility = View.GONE

        val category = arrayOf("ewaste", "Notebook", "Textbook")

        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this@SellActivity,
            android.R.layout.simple_spinner_item,
            category
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {

                itemName.text.clear()
                itemPrice.text.clear()
                itemAge.text.clear()
                itemSpecs.text.clear()
                itemAuthor.text.clear()
                itemEdition.text.clear()
                txtImgSelected.text = "0 Images Selected"
                uri.clear()
                imgDp.setImageResource(R.drawable.defphoto)
                proceed=false

                val item = parent.getItemAtPosition(pos)
                Toast.makeText(this@SellActivity,"$item",Toast.LENGTH_SHORT).show()
                waste = item.toString()
                if(item.toString() != "Textbook"){
                    itemAuthor.visibility = View.GONE
                    itemEdition.visibility = View.GONE

                }
                else{
                    itemAuthor.visibility = View.VISIBLE
                    itemEdition.visibility = View.VISIBLE

                }

                if(item.toString()=="Notebook"){
                    itemAge.visibility = View.GONE
                }
                else{
                    itemAge.visibility = View.VISIBLE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        setUpToolbar()

        imgDp.setOnClickListener {
            dpfilter = true
            launchGallery()
        }

        imageButton = findViewById(R.id.imageButton)
        imageButton.setOnClickListener {
            txtImgSelected.text = "0 Images Selected"
            uri.clear()
            dpfilter=false
            launchGallery()


        }




        sendButton = findViewById(R.id.sendButton)
        sendButton.setOnClickListener {

            flag = (checkDp(proceed) && checkName(itemName.text.toString())
                    && checkPrice(itemPrice.text.toString())
                    && checkSpecs(itemSpecs.text.toString()) && checkImages(uri))
            if(flag) {
                println(waste)
                if(waste == "Textbook"){
                    if(itemAuthor.text.toString().isEmpty() || itemEdition.text.toString().isEmpty()){
                        Toast.makeText(this@SellActivity,"Please fill all details",Toast.LENGTH_SHORT).show()
                    }
                    else
                        uploadFile(uri)
                }

                else if(waste != "Notebook"){
                    if(itemAge.text.toString().isEmpty())
                        Toast.makeText(this@SellActivity,"Please fill all details",Toast.LENGTH_SHORT).show()
                    else
                        uploadFile(uri)
                }
                else
                    uploadFile(uri)

            }

        }



    }

    private fun launchGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        //intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    @Throws(IOException::class)
    private fun createImageData(uri: Uri) {
        val inputStream = contentResolver.openInputStream(uri)
        inputStream?.buffered()?.use {
            imageData = it.readBytes()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
//            val temp = data?.data!!
//            //imageView.setImageURI(temp)
//            createImageData(temp)
//
//            uri.add(temp)

            if (data?.getClipData() != null) {
                var count = data.clipData!!.itemCount

                for (i in 0..count - 1) {
                    var imageUri: Uri = data.clipData!!.getItemAt(i).uri
                    //     iv_image.setImageURI(imageUri) Here you can assign your Image URI to the ImageViews
                    if(dpfilter){
                        if (imageUri != null) {
                            dpImage = imageUri
                            imgDp.setImageURI(dpImage)
                            proceed = true
                        }
                    }
                    else{
                        if (imageUri != null) {
                            uri.add(imageUri)
                        }
                    }
                }

            } else if (data?.getData() != null) {
                // if single image is selected

                var imageUri: Uri? = data.data
                //   iv_image.setImageURI(imageUri) Here you can assign the picked image uri to your imageview

                if(dpfilter){
                    if (imageUri != null) {
                        dpImage = imageUri
                        imgDp.setImageURI(dpImage)
                        proceed = true
                    }
                }
                else{
                    if (imageUri != null) {
                        uri.add(imageUri)
                    }
                }
            }
        }


        if(uri.size==1)
            txtImgSelected.text = "1 Image Selected"
        if(uri.size>0)
            txtImgSelected.text = "${uri.size} Image Selected"

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun uploadFile(fileUri: List<Uri>) {

        progressBar.visibility = View.VISIBLE

        println(fileUri)

        val BASE_URL = "https://se-course-app.herokuapp.com/"

        val builder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())


        val retrofit = builder.build()
        val httpClient = OkHttpClient.Builder()




        // create upload service client
        val service: FileUploadService = retrofit.create(FileUploadService::class.java)

        // use the FileUtils to get the actual file by uri

        val gallery= arrayListOf<MultipartBody.Part> ()

        for(uri in fileUri){
            val file = FileUtils.getFile(this, uri)

            // create RequestBody instance from file
            val requestFile = RequestBody.create(
                MediaType.parse(contentResolver.getType(uri).toString()),
                file
            )
            gallery.add(MultipartBody.Part.createFormData("gallery", file.name, requestFile))
        }

        val file = FileUtils.getFile(this, dpImage)
        val requestFile = RequestBody.create(
            MediaType.parse(contentResolver.getType(dpImage).toString()),
            file
        )
        val thumbnail = MultipartBody.Part.createFormData("thumbnail", file.name, requestFile)


        // MultipartBody.Part is used to send also the actual file name
       // val gallery = MultipartBody.Part.createFormData("gallery", file.name, requestFile)




        val name = RequestBody.create(MultipartBody.FORM, itemName.text.toString())
        val price = RequestBody.create(MultipartBody.FORM, itemPrice.text.toString())
        val used_for = RequestBody.create(MultipartBody.FORM, itemAge.text.toString())
        val specifications = RequestBody.create(MultipartBody.FORM, itemSpecs.text.toString())
        val pincode = RequestBody.create(
            MultipartBody.FORM, sharedPreferences.getString(
                "userPinCode",
                "0"
            ).toString()
        )
        val location = RequestBody.create(
            MultipartBody.FORM, sharedPreferences.getString(
                "userLocation",
                "Unspecified"
            ).toString()
        )


        println("Bearer " + sharedPreferences.getString("userToken", "-1").toString())

        // finally, execute the request
        var call : Call<ResponseBody?>? = null
        if(waste =="ewaste") {
            call = service.uploadewaste(
                "Bearer " + sharedPreferences.getString("userToken", "-1").toString(),
                name,
                price,
                used_for,
                specifications,
                pincode,
                location,
                gallery,
                thumbnail
            )
            //Toast.makeText(this@SellActivity,"${waste} ewaste",Toast.LENGTH_SHORT).show()
        }
        else if(waste =="Textbook"){
            val author = RequestBody.create(MultipartBody.FORM, itemAuthor.text.toString())
            val edition = RequestBody.create(MultipartBody.FORM, itemEdition.text.toString())

            call = service.uploadtextwaste(
                "Bearer " + sharedPreferences.getString("userToken", "-1").toString(),
                name,
                price,
                used_for,
                specifications,
                pincode,
                location,
                gallery,
                author,
                edition,
                thumbnail
            )
            //Toast.makeText(this@SellActivity,"${waste} Textbook",Toast.LENGTH_SHORT).show()
        }
        else{
            call = service.uploadnotewaste(
                "Bearer " + sharedPreferences.getString("userToken", "-1").toString(),
                name,
                price,
                thumbnail,
                specifications,
                pincode,
                location,
                gallery

            )

            //Toast.makeText(this@SellActivity,"$waste notewaste",Toast.LENGTH_SHORT).show()
        }
        call!!.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(
                call: Call<ResponseBody?>,
                response: Response<ResponseBody?>
            ) {

                progressBar.visibility = View.GONE
                if(response.body()!=null) {
                    Toast.makeText(this@SellActivity, "Item listed for selling", Toast.LENGTH_LONG).show()
                    println(response.body()!!.byteStream())
                }
                else{
                    Toast.makeText(this@SellActivity, "Please try again", Toast.LENGTH_LONG).show()
                }
                println(response.body().toString())
                println(response)
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                Toast.makeText(this@SellActivity, "$t", Toast.LENGTH_LONG).show()
                t.message?.let {
                    Log.e("Upload error:", it)
                }
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        if(id == android.R.id.home) {
            val intent = Intent(this@SellActivity, HomePageActivity::class.java)
            startActivity(intent)
            finish()

        }
        return super.onOptionsItemSelected(item)
    }
    private fun setUpToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Sell Item"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    private  fun checkDp(ans: Boolean):Boolean{
        if(!ans)
            Toast.makeText(this@SellActivity, "Choose Dp for product", Toast.LENGTH_SHORT).show()

        return ans
    }
    private fun checkImages(uri: ArrayList<Uri>):Boolean{
        if(uri.isEmpty()){
            Toast.makeText(this@SellActivity, "Choose images to display", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
    private fun checkName(name: String):Boolean{
        if(name.isEmpty()){
            Toast.makeText(this@SellActivity, "Specify name of product", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun checkPrice(price: String):Boolean{
        if(price.isEmpty()){
            Toast.makeText(this@SellActivity, "Specify price of product", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
    private fun checkAge(age: String):Boolean{
        if(age.isEmpty()){
            Toast.makeText(this@SellActivity, "Specify item used since", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun checkSpecs(specs: String):Boolean{
        if(specs.isEmpty()){
            Toast.makeText(this@SellActivity, "Specifications missing", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }





}