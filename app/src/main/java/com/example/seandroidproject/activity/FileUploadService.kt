package com.example.seandroidproject.activity

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part



interface FileUploadService {


    @Headers("content-type:multipart/form-data")
    @Multipart
    @POST("ewaste/create")
    open fun upload(
        @Part("name") name: RequestBody?,
        @Part("price") price: RequestBody?,
        @Part("used_for") used_for: RequestBody?,
        @Part("specifications") specifications: RequestBody?,
        @Part("pincode") pincode: RequestBody?,
        @Part("location") location: RequestBody?,
        @Part file: MultipartBody.Part?

    ): Call<ResponseBody?>?
}