package com.example.seandroidproject.adapter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.seandroidproject.R
import com.example.seandroidproject.activity.DetailViewEwasteActivity
import com.example.seandroidproject.activity.DetailViewNotewasteActivity
import com.example.seandroidproject.activity.DetailViewTextWasteActivity
import com.example.seandroidproject.model.ItemModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_wishlist_row.view.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException


class RecyclerViewAdapterWishlist(val items: MutableList<ItemModel>, val context: Context):RecyclerView.Adapter<RecyclerViewAdapterWishlist.ViewHolder>() {

    lateinit var sharedPreferences: SharedPreferences

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val name : TextView = view.findViewById(R.id.txtProductName)

        val price : TextView = view.findViewById(R.id.txtPrice)
        val imageview: ImageView = view.findViewById(R.id.itemImage)
        val pincode: TextView = view.findViewById(R.id.txtPinCode)
        val llContent : LinearLayout = view.findViewById(R.id.llContent)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        sharedPreferences = this.context.getSharedPreferences("ReuseNation Preferences", Context.MODE_PRIVATE)

        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_wishlist_row, parent, false)
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

        holder.pincode.text = item.pincode.toString()

        //Picasso.with(context).load("$baseUrl/${item.thumbnail}").into(holder.imageview)

        Picasso.get()
            .load("$baseUrl/${item.thumbnail}")
            .fit()
            .centerInside()
            .placeholder(R.drawable.loading)
            .into(holder.imageview)

        holder.itemView.btnRemoveFav.setOnClickListener {
            println("removing")
            println(item._id)
            val url = "https://se-course-app.herokuapp.com/users/remove/wishlist"

            val token = sharedPreferences.getString("userToken", "-1").toString()

            val client = OkHttpClient()

            val jsonObject: JSONObject = JSONObject()
            jsonObject.put("item_id", item._id)

            val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()

            val requestBody: RequestBody = jsonObject.toString().toRequestBody(JSON)

            // authentication is hardcoded
            val request = Request.Builder()
                .url("$url")
                .addHeader("Authorization", "Bearer $token")
                .delete(requestBody)
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
            items.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemCount)
            Toast.makeText(context, "removed from wishlist", Toast.LENGTH_SHORT).show()
        }


        holder.llContent.setOnClickListener{
            //Toast.makeText(context, "$category", Toast.LENGTH_SHORT).show()

            if(item.category=="ewaste"){
                val intent = Intent(context, DetailViewEwasteActivity::class.java)
                intent.putExtra("_id", item._id)
                ContextCompat.startActivity(context, intent, null)

            }
            else if(item.category=="twaste"){
                val intent = Intent(context, DetailViewTextWasteActivity::class.java)
                intent.putExtra("_id", item._id)
                ContextCompat.startActivity(context, intent, null)
            }
            else if(item.category=="nwaste"){
                val intent = Intent(context, DetailViewNotewasteActivity::class.java)
                intent.putExtra("_id", item._id)
                ContextCompat.startActivity(context, intent, null)
            }
        }

    }
}