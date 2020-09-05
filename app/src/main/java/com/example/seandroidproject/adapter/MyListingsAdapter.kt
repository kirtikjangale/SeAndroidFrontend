package com.example.seandroidproject.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.seandroidproject.R
import com.example.seandroidproject.activity.HomePageActivity
import com.example.seandroidproject.activity.ItemSoldSplashActivity
import com.example.seandroidproject.model.ItemMyListing
import com.kirtik.foodrunner.util.ConnectionManager
import com.squareup.picasso.Picasso

class MyListingsAdapter(val context: Context,val itemList : ArrayList<ItemMyListing>,val authToken:String): RecyclerView.Adapter<MyListingsAdapter.MyListingViewHolder>() {


    class MyListingViewHolder(view:View) : RecyclerView.ViewHolder(view){
        val imgItem : ImageView = view.findViewById(R.id.imgItem)
        val txtItemTitle : TextView = view.findViewById(R.id.txtItemTitle)
        val txtItemPrice : TextView = view.findViewById(R.id.txtItemPrice)
        val btnSold : Button = view.findViewById(R.id.btnSold)
        val llContent : LinearLayout = view.findViewById(R.id.llContent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyListingsAdapter.MyListingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_mylistings_row,parent,false)
        return MyListingViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyListingsAdapter.MyListingViewHolder, position: Int) {
        val item = itemList[position]

        Picasso.get().load("https://se-course-app.herokuapp.com/images/${item.thumbnail}").fit()
            .centerInside()
            .placeholder(R.drawable.loading)
            .into(holder.imgItem)

        holder.txtItemTitle.text = item.name
       holder.txtItemPrice.text = "Rs ${item.price}"


        holder.btnSold.setOnClickListener {
            deleteItem(item._id,item.category)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    private fun deleteItem(id : String,category:String){

            val queue = Volley.newRequestQueue(context)
            val url = "https://se-course-app.herokuapp.com/$category/delete/$id"

            val jsonRequest = @SuppressLint("SetTextI18n")
            object : JsonObjectRequest(Request.Method.POST, url, null,
                Response.Listener {
                    try {
                        //Toast.makeText(context,"Item removed from app",Toast.LENGTH_SHORT).show()
                        val intent = Intent(context,ItemSoldSplashActivity::class.java)
                        startActivity(context,intent,null)


                    } catch (e: Exception) {
                        println(e)
                        Toast.makeText(context, "Exception", Toast.LENGTH_SHORT)
                            .show()
                    }

                }, Response.ErrorListener {
                    Toast.makeText(context, "$it", Toast.LENGTH_SHORT).show()
                }){

                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["content-type"] = "application/json"
                    headers["Authorization"] = authToken
                    return  headers
                }
            }


            queue.add(jsonRequest)



    }
}