package com.example.seandroidproject.util

import okhttp3.OkHttpClient

import retrofit2.Retrofit

import retrofit2.converter.gson.GsonConverterFactory




class ServiceGenerator {
    private val BASE_URL = "https://se-course-app.herokuapp.com/"

    private val builder = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())

    private val retrofit = builder.build()

    private val httpClient = OkHttpClient.Builder()

    fun <S> createService(
        serviceClass: Class<S>?
    ): S {
        return retrofit.create(serviceClass)
    }
}