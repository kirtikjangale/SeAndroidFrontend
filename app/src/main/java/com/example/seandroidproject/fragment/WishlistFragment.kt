package com.example.seandroidproject.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.seandroidproject.R
import com.example.seandroidproject.util.ItemModel
import com.example.seandroidproject.util.RecyclerViewAdapter
import com.example.seandroidproject.util.RecyclerViewAdapterWishlist
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException

class WishlistFragment : Fragment() {

    lateinit var recyclerWishlistItems : RecyclerView

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_wishlist, container, false)

        recyclerWishlistItems = view.findViewById(R.id.recycler_wishlist)
        recyclerWishlistItems.layoutManager = GridLayoutManager(activity, 2)
        fetchJson()
        // Inflate the layout for this fragment
        // just some basic data to display

        return view
    }

    fun fetchJson(){
        println("fetching JSON data from backend...")
        sharedPreferences = this.activity!!.getSharedPreferences(getString(R.string.preference_file_name),Context.MODE_PRIVATE)

        val token = sharedPreferences.getString("userToken", "-1").toString()

        val url = "https://se-course-app.herokuapp.com/users/wishlist/me"

        val client = OkHttpClient()

        // authentication is hardcoded
        val request = Request.Builder().url(url).addHeader("Authorization", "Bearer $token").build()

        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val resBody = response?.body?.string()
//                println(resBody)

                val gson = GsonBuilder().create()
                val itemListData =  gson.fromJson(resBody, Array<ItemModel>::class.java).toMutableList()
//                println(itemListData)

                val itemsListAdapter = RecyclerViewAdapterWishlist(itemListData, activity as Context)

                activity!!.runOnUiThread{
                    recyclerWishlistItems.adapter = itemsListAdapter
                }
            }
            override fun onFailure(call: Call, e: IOException) {
                println("Req. failed")
            }
        })
    }
}