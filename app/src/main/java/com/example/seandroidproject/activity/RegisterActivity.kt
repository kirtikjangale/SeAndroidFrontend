package com.example.seandroidproject.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.example.seandroidproject.R
import java.io.IOException

class RegisterActivity : AppCompatActivity() {

    lateinit var imgUserPhoto : ImageView
    lateinit var etUserName : EditText
    lateinit var etUserEmail : EditText
    lateinit var etUserPhone : EditText
    lateinit var etUserAddress : EditText
    lateinit var etUserPassword : EditText
    lateinit var etUserPasswordConfirm : EditText
    lateinit var btnRegister : Button

    private var imageData: ByteArray? = null


    companion object {
        private const val IMAGE_PICK_CODE = 999
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        supportActionBar?.hide()

        imgUserPhoto = findViewById(R.id.imgUserPhoto)
        etUserName = findViewById(R.id.etUserName)
        etUserEmail = findViewById(R.id.etUserEmail)
        etUserPhone = findViewById(R.id.etUserPhone)
        etUserAddress = findViewById(R.id.etUserAddress)
        etUserPassword = findViewById(R.id.etUserPassword)
        etUserPasswordConfirm = findViewById(R.id.etUserPasswordConfirm)
        btnRegister = findViewById(R.id.btnRegister)

        imgUserPhoto.setOnClickListener{
            launchGallery()
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
            val uri = data?.data
            if (uri != null) {
                imgUserPhoto.setImageURI(uri)
                createImageData(uri)
            }
            println(uri)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}