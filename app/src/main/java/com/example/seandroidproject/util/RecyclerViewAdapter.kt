package com.example.seandroidproject.util

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.seandroidproject.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_allitems_row.view.*
import okhttp3.*
import okhttp3.MultipartBody
import java.io.IOException


class RecyclerViewAdapter(val items: List<ItemModel>, val context: Context):RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    lateinit var sharedPreferences: SharedPreferences

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val name : TextView = view.findViewById(R.id.txtProductName)
        val usedFor : TextView = view.findViewById(R.id.txtUsedFor)
        val price : TextView = view.findViewById(R.id.txtPrice)
        val imageview: ImageView = view.findViewById(R.id.itemImage)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        sharedPreferences = this.context.getSharedPreferences("ReuseNation Preferences", Context.MODE_PRIVATE)

        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_allitems_row, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = items[position]
        val baseUrl = "https://se-course-app.herokuapp.com/images/"

        holder.name.text = item.name
//        holder.sellerName.text = item.sellerName
        holder.price.text = item.price.toString()
        holder.usedFor.text = item.used_for

        Picasso.with(context).load("$baseUrl/${item.thumbnail}").into(holder.imageview)

        holder.itemView.btnFavorite.setOnClickListener {
            val url = "https://se-course-app.herokuapp.com/users/add/wishlist"
            val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

            if(!isLoggedIn){
                Toast.makeText(context, "Login to add to wishlist", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val token = sharedPreferences.getString("userToken", "-1").toString()

            val client = OkHttpClient()

            val requestBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("item_id", "${item._id}")
                .build()

            // authentication is hardcoded
            val request = Request.Builder()
                .url("$url/${item._id}")
                .addHeader("Authorization", "Bearer $token")
                .post(requestBody)
                .build()


            client.newCall(request).enqueue(object: Callback {
                override fun onResponse(call: Call, response: Response) {
                    val resBody = response?.body?.string()
//                    println(resBody)
                    Toast.makeText(context, "added to wishlist", Toast.LENGTH_SHORT).show()
                }
                override fun onFailure(call: Call, e: IOException) {
                    println("Req. failed")
                }
            })

        }

    }
}