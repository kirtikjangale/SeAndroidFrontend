package com.example.seandroidproject.fragment

import android.app.Activity
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
import okhttp3.internal.wait
import java.io.IOException


class AllItemsFragment : Fragment() {

    lateinit var recyclerAllItems : RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_all_items, container, false)

        recyclerAllItems = view.findViewById(R.id.recycler_allitems)

        // dummy data
        fetchJson()
//        arrayList.add(ItemModel("OnePlus 1", "Nikhil1", 3.9, ""))
//        arrayList.add(ItemModel("OnePlus 2", "Nikhil2", 39.9, ""))
//        arrayList.add(ItemModel("OnePlus 3", "Nikhil3", 399.9, ""))
//        arrayList.add(ItemModel("OnePlus 4", "Nikhil4", 3999.9, ""))
//        arrayList.add(ItemModel("OnePlus 5", "Nikhil5", 39999.9, ""))
//        arrayList.add(ItemModel("OnePlus 6", "Nikhil6", 39999.9, ""))
//        arrayList.add(ItemModel("OnePlus 7", "Nikhil7", 39999.9, ""))

        recyclerAllItems.layoutManager = LinearLayoutManager(activity)
        // Inflate the layout for this fragment
        // just some basic data to display

        return view
    }

    fun fetchJson(){
        println("fetching JSON data from backend...")

        val url = "https://se-course-app.herokuapp.com/ewaste/all/517619"

        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
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
