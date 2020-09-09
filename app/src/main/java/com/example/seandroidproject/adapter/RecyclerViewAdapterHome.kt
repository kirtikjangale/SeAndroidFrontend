package com.example.seandroidproject.adapter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.seandroidproject.R
import com.example.seandroidproject.activity.DetailViewEwasteActivity
import com.example.seandroidproject.activity.DetailViewNotewasteActivity
import com.example.seandroidproject.activity.DetailViewTextWasteActivity
import com.example.seandroidproject.model.ItemModel
import com.squareup.picasso.Picasso
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException


class RecyclerViewAdapter(val items: List<ItemModel>, val wishlist: MutableList<String>, val context: Context, val category: String):RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    lateinit var sharedPreferences: SharedPreferences
    var responseCode:Int = 0


    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val name : TextView = view.findViewById(R.id.txtProductName)
        val usedFor : TextView = view.findViewById(R.id.txtUsedFor)
        val price : TextView = view.findViewById(R.id.txtPrice)
        val imageview: ImageView = view.findViewById(R.id.itemImage)
        val llContent : LinearLayout = view.findViewById(R.id.llContent)
        val card : CardView = view.findViewById(R.id.card)
        val btnFav: Button = view.findViewById(R.id.btnFavorite)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        sharedPreferences = this.context.getSharedPreferences(
            "ReuseNation Preferences",
            Context.MODE_PRIVATE
        )

        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.recycler_allitems_row,
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val item_id = item._id
        val token = sharedPreferences.getString("userToken", "-1").toString()

        val client = OkHttpClient()

            val baseUrl = "https://se-course-app.herokuapp.com/images/"

            holder.name.text = item.name
//        holder.sellerName.text = item.sellerName
            holder.price.text = item.price.toString()
            holder.usedFor.text = "Used For: "+item.used_for

            if(category == "notewaste")
                holder.usedFor.visibility = View.GONE

            Picasso.get()
                .load("$baseUrl/${item.thumbnail}")
                .fit()
                .centerInside()
                .placeholder(R.drawable.loading)
                .into(holder.imageview)

            val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

            holder.btnFav.setOnClickListener {
                val url = "https://se-course-app.herokuapp.com/users/add/wishlist"

                if(!isLoggedIn){
                    Toast.makeText(context, "Login to use wishlist", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if(item_id in wishlist){
                    return@setOnClickListener
                }
//                if(item.owner == sharedPreferences.getString("userId", "id")){
//                    Toast.makeText(context, "Owner cannot add to wishlist", Toast.LENGTH_SHORT).show()
//                    return@setOnClickListener
//                }

                println(item.owner)
                println(sharedPreferences.getString("userId", "id"))

                    val client = OkHttpClient()

                    val jsonObject: JSONObject = JSONObject()
                    jsonObject.put("item_id", item_id)

                    val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()
                    val requestBody: RequestBody = jsonObject.toString().toRequestBody(JSON)

                    // authentication is hardcoded
                    val request = Request.Builder()
                        .url(url)
                        .addHeader("Authorization", "Bearer $token")
                        .post(requestBody)
                        .build()


                    client.newCall(request).enqueue(object : Callback {
                        override fun onResponse(call: Call, response: Response) {
                            println("wihlisting ${item_id} in callback")
                            val resBody = response?.body?.string()
                            println("response:$resBody")
                            println(response.code)

                            if(response.code == 400){
                                Handler(Looper.getMainLooper()).post(Runnable {
                                    Toast.makeText(
                                        context,
                                        "owner cannot add to wishlist", Toast.LENGTH_SHORT
                                    ).show()
                                })
                            }

                            responseCode = response.code
                            if(response.code == 200){

                                wishlist.add(item_id)
                                holder.btnFav.text = "Wish Listed"
                                holder.btnFav.setBackgroundColor(
                                    getColor(
                                        context,
                                        R.color.colorLtGreen
                                    )
                                )
                                holder.btnFav.setOnClickListener {
                                    return@setOnClickListener
                                }
                            }
                            else{
                                holder.btnFav.text = "Add to wishlist"
                                holder.btnFav.setBackgroundColor(
                                    getColor(
                                        context,
                                        R.color.colorPrimaryDark
                                    )
                                )
//                                holder.btnFav.setOnClickListener {
//                                    return@setOnClickListener
//                                }

                            }


                        }

                        override fun onFailure(call: Call, e: IOException) {
                            println("Req. failed")
                        }
                    })

                    Toast.makeText(context, "adding to wishlist", Toast.LENGTH_SHORT).show()

            }



            holder.llContent.setOnClickListener{
                //Toast.makeText(context, "$category", Toast.LENGTH_SHORT).show()

                if(category=="ewaste"){
                    val intent = Intent(context, DetailViewEwasteActivity::class.java)
                    intent.putExtra("_id", item_id)
                    startActivity(context, intent, null)

                }
                else if(category=="textwaste"){
                    val intent = Intent(context, DetailViewTextWasteActivity::class.java)
                    intent.putExtra("_id", item_id)
                    startActivity(context, intent, null)
                }
                else if(category=="notewaste"){
                    val intent = Intent(context, DetailViewNotewasteActivity::class.java)
                    intent.putExtra("_id", item_id)
                    startActivity(context, intent, null)
                }
            }

        println(wishlist)
        if(item_id in wishlist){
            println("wishlisting ${item_id} in if")
            holder.btnFav.text = "Wish Listed"
            holder.btnFav.setBackgroundColor(getColor(context, R.color.colorLtGreen))
        }
        else{
            holder.btnFav.setBackgroundColor(getColor(context, R.color.colorPrimaryDark))
            if(!isLoggedIn){
                holder.btnFav.text = "Login to use wishlist"
            }
            else{
                holder.btnFav.text = "Add to Wishlist"
            }
        }

//            if(token != "-1"){
//                val wishlist_req = Request.Builder()
//                    .url("https://se-course-app.herokuapp.com/users/me")
//                    .header("Authorization", "Bearer $token")
//                    .build()
//
//                var wishlist: Array<String>? = null
//
//                client.newCall(wishlist_req).enqueue(object: Callback{
//                    override fun onFailure(call: Call, e: IOException) {
//                        println("req. failed")
//                    }
//
//                    override fun onResponse(call: Call, response: Response) {
//                        val resBody = response?.body?.string()
//
//                        val gson = GsonBuilder().create()
//                        val wishListData = gson.fromJson(resBody, WishList::class.java)
//                        wishlist = wishListData.wishlist
//                    }
//                })
//                for (w in wishlist!!){
//                    if (w == item_id){
//                        holder.btnFav.text = "Wishlisted"
//                        holder.btnFav.setBackgroundColor(R.color.colorLtGreen)
//                        holder.btnFav.setOnClickListener {
//                            Toast.makeText(context, "already in wishlist", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                }
//            }

        }




}

data class WishList(
    var wishlist: Array<String>
){}