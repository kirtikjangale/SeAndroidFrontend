package com.example.seandroidproject.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.seandroidproject.R
import kotlinx.android.synthetic.main.recycler_allitems_row.view.*
import okhttp3.*
import okhttp3.MultipartBody
import java.io.IOException


class RecyclerViewAdapter(val items: List<ItemModel>, val context: Context):RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {


    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val name : TextView = view.findViewById(R.id.txtProductName)
        val usedFor : TextView = view.findViewById(R.id.txtUsedFor)
        val price : TextView = view.findViewById(R.id.txtPrice)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_allitems_row, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = items[position]

        holder.name.text = item.name
//        holder.sellerName.text = item.sellerName
        holder.price.text = item.price.toString()
        holder.usedFor.text = item.used_for

        holder.itemView.btnFavorite.setOnClickListener {
            val url = "https://se-course-app.herokuapp.com/users/wishlist"

            val client = OkHttpClient()

            val requestBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("item_id", "${item._id}")
                .build()

            // authentication is hardcoded
            val request = Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI1ZjQ1ZmYxYmU0YTdmNjAwMTcyNWQwZmUiLCJpYXQiOjE1OTg0NjU2MDMsImV4cCI6MTU5OTE1NjgwM30.n5HaZnLmVfTjXJrgAap4RDW331WNPcqow2KRyIxbl0w")
                .post(requestBody)
                .build()


            client.newCall(request).enqueue(object: Callback {
                override fun onResponse(call: Call, response: Response) {
                    val resBody = response?.body?.string()
                    println(resBody)
                }
                override fun onFailure(call: Call, e: IOException) {
                    println("Req. failed")
                }
            })

        }

    }
}