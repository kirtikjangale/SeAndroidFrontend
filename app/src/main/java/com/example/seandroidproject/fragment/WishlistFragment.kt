package com.example.seandroidproject.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.seandroidproject.R
import com.example.seandroidproject.util.ItemModel
import com.example.seandroidproject.util.RecyclerViewAdapter
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException

class WishlistFragment : Fragment() {

    lateinit var recyclerAllItems : RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_wishlist, container, false)

        recyclerAllItems = view.findViewById(R.id.recycler_allitems)
        recyclerAllItems.layoutManager = LinearLayoutManager(activity)
        fetchJson()
        // Inflate the layout for this fragment
        // just some basic data to display

        return view
    }

    fun fetchJson(){
        println("fetching JSON data from backend...")

        val url = "https://se-course-app.herokuapp.com/users/wishlist/me"

        val client = OkHttpClient()

        // authentication is hardcoded
        val request = Request.Builder().url(url).addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI1ZjRiYjc2Y2Q3YzQ3MDAwMTc2ZGUzMTUiLCJpYXQiOjE1OTg3OTgwMDQsImV4cCI6MTU5OTQ4OTIwNH0.V-yJS5TBzP2lm3EJWMBUbI7TStgxQWepHQYrfaXfFbs").build()

        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val resBody = response?.body?.string()
//                println(resBody)

                val gson = GsonBuilder().create()
                val itemListData =  gson.fromJson(resBody, Array<ItemModel>::class.java).toList()
//                println(itemListData)

                val itemsListAdapter = RecyclerViewAdapter(itemListData, activity as Context)

                activity!!.runOnUiThread{
                    recyclerAllItems.adapter = itemsListAdapter
                }
            }
            override fun onFailure(call: Call, e: IOException) {
                println("Req. failed")
            }
        })
    }
}