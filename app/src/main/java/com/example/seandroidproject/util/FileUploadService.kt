package com.example.seandroidproject.util

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface FileUploadService {


  //  @Headers("content-type:multipart/form-data")
    @Multipart
    @POST("ewaste/create")
    open fun uploadewaste(
        @Header("Authorization") token : String,
        @Part("name") name: RequestBody?,
        @Part("price") price: RequestBody?,
        @Part("used_for") used_for: RequestBody?,
        @Part("specifications") specifications: RequestBody?,
        @Part("pincode") pincode: RequestBody?,
        @Part("location") location: RequestBody?,
        @Part gallery: List<MultipartBody.Part?>,
        @Part thumbnail : MultipartBody.Part?


    ): Call<ResponseBody?>?


    @Multipart
    @POST("textwaste/create")
    open fun uploadtextwaste(
        @Header("Authorization") token : String,
        @Part("name") name: RequestBody?,
        @Part("price") price: RequestBody?,
        @Part("used_for") used_for: RequestBody?,
        @Part("description") specifications: RequestBody?,
        @Part("pincode") pincode: RequestBody?,
        @Part("location") location: RequestBody?,
        @Part gallery: List<MultipartBody.Part?>,
        @Part("author") author: RequestBody?,
        @Part("edition") edition: RequestBody?,
        @Part thumbnail : MultipartBody.Part?

    ): Call<ResponseBody?>?

    @Multipart
    @POST("notewaste/create")
    open fun uploadnotewaste(
        @Header("Authorization") token : String,
        @Part("name") name: RequestBody?,
        @Part("price") price: RequestBody?,
        @Part thumbnail : MultipartBody.Part?,
        @Part("description") specifications: RequestBody?,
        @Part("pincode") pincode: RequestBody?,
        @Part("location") location: RequestBody?,
        @Part gallery: List<MultipartBody.Part?>


    ): Call<ResponseBody?>?

    @Multipart
    @PATCH("users/update")
    open fun uploadprofpic(
        @Header("Authorization") token : String,
        @Part prof_pic : MultipartBody.Part?
    ): Call<ResponseBody?>?
}