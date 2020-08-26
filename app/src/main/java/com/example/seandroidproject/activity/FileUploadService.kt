package com.example.seandroidproject.activity

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface FileUploadService {


  //  @Headers("content-type:multipart/form-data")
    @Multipart
    @POST("ewaste/create")
    open fun upload(
        @Header("Authorization") token : String,
        @Part("name") name: RequestBody?,
        @Part("price") price: RequestBody?,
        @Part("used_for") used_for: RequestBody?,
        @Part("specifications") specifications: RequestBody?,
        @Part("pincode") pincode: RequestBody?,
        @Part("location") location: RequestBody?,
        @Part gallery: List<MultipartBody.Part?>

    ): Call<ResponseBody?>?
}