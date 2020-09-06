package com.example.seandroidproject.fragment

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat
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
import java.lang.Exception

class WishlistFragment : Fragment() {

    lateinit var recyclerWishlistItems : RecyclerView
    lateinit var loader : RelativeLayout

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_wishlist, container, false)

        loader = view.findViewById(R.id.progressBar)

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

        try {
            client.newCall(request).enqueue(object: Callback {
                override fun onResponse(call: Call, response: Response) {
                    val resBody = response?.body?.string()
//                println(resBody)

                    val gson = GsonBuilder().create()
                    val itemListData =  gson.fromJson(resBody, Array<ItemModel>::class.java).toMutableList()
//                println(itemListData)

                    val itemsListAdapter = RecyclerViewAdapterWishlist(itemListData, activity as Context)

                    activity!!.runOnUiThread{
                        loader.visibility = View.GONE
                        recyclerWishlistItems.adapter = itemsListAdapter
                    }
                }
                override fun onFailure(call: Call, e: IOException) {
                    println("Req. failed")
                    activity!!.runOnUiThread{
                        loader.visibility = View.GONE
                        view?.findViewById<LinearLayout>(R.id.no_item_modal)?.visibility = View.VISIBLE
                        view?.findViewById<RecyclerView>(R.id.recycler_allitems)?.visibility = View.INVISIBLE
                        view?.findViewById<TextView>(R.id.error_text)?.text = "Something went wrong please try again later"

                        val dialog = AlertDialog.Builder(activity as Context)
                        dialog.setTitle("Wishlist Error")
                        dialog.setMessage("Internet Connection Not Found")
                        dialog.setPositiveButton("Open Settings"){ _, _->
                            val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                            startActivity(settingsIntent)
                            activity!!.finish()
                        }
                        dialog.setNegativeButton("Cancel"){ _, _->
                            ActivityCompat.finishAffinity(activity!!)
                        }
                        dialog.create()
                        dialog.show()
                    }
                }
            })
        }
        catch (err: Exception){
            loader.visibility = View.GONE
            view?.findViewById<LinearLayout>(R.id.no_item_modal)?.visibility = View.VISIBLE
            view?.findViewById<RecyclerView>(R.id.recycler_allitems)?.visibility = View.INVISIBLE
            view?.findViewById<TextView>(R.id.error_text)?.text = "Something went wrong please try again later"
        }

    }
}