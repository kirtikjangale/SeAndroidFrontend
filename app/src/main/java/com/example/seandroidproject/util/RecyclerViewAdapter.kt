package com.example.seandroidproject.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.seandroidproject.R

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
    }
}