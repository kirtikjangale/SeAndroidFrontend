package com.example.seandroidproject.activity

//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
import com.example.seandroidproject.R

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.seandroidproject.util.FileUtils
import okhttp3.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


class SellActivity : AppCompatActivity() {

    lateinit var imageView: ImageView
    lateinit var imageButton: Button
    lateinit var sendButton: Button
    var imageData: ByteArray? = null
    lateinit var uri : Uri

    companion object {
        private const val IMAGE_PICK_CODE = 999
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sell)

        imageView = findViewById(R.id.imageView)

        imageButton = findViewById(R.id.imageButton)
        imageButton.setOnClickListener {
            launchGallery()
        }

        sendButton = findViewById(R.id.sendButton)
        sendButton.setOnClickListener {
            uploadFile(uri)
        }



    }

    private fun launchGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
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
            uri = data?.data!!
            imageView.setImageURI(uri)
            createImageData(uri)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun uploadFile(fileUri: Uri) {

        val BASE_URL = "https://se-course-app.herokuapp.com/"

        val builder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())


        val retrofit = builder.build()
        val httpClient = OkHttpClient.Builder()




        // create upload service client
        val service: FileUploadService = retrofit.create(FileUploadService::class.java)

        // use the FileUtils to get the actual file by uri
        val file = FileUtils.getFile(this, fileUri)

        // create RequestBody instance from file
        val requestFile = RequestBody.create(
            MediaType.parse(contentResolver.getType(fileUri).toString()),
            file
        )

        // MultipartBody.Part is used to send also the actual file name
        val gallery = MultipartBody.Part.createFormData("gallery", file.name, requestFile)

        val nameZ ="HC Verma"
        val priceZ = "45"
        val used_forZ="2 yrs"
        val specificationsZ ="4th edition"
        val pincodeZ ="425201"
        val locationZ = "bhusawal"


        val name = RequestBody.create(MultipartBody.FORM, nameZ)
        val price = RequestBody.create(MultipartBody.FORM, priceZ)
        val used_for = RequestBody.create(MultipartBody.FORM, used_forZ)
        val specifications = RequestBody.create(MultipartBody.FORM, specificationsZ)
        val pincode = RequestBody.create(MultipartBody.FORM, pincodeZ)
        val location = RequestBody.create(MultipartBody.FORM, locationZ)



        // finally, execute the request
        val call = service.upload(name,price,used_for,specifications,pincode,location,gallery)
        call!!.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(
                call: Call<ResponseBody?>,
                response: Response<ResponseBody?>
            ) {
                Toast.makeText(this@SellActivity,"Success",Toast.LENGTH_LONG).show()
                println(response.body())
                println(response)
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                Toast.makeText(this@SellActivity,"$t",Toast.LENGTH_LONG).show()
                t.message?.let { Log.e("Upload error:", it)
                }
            }
        })
    }

}