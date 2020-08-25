package com.example.seandroidproject.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.seandroidproject.R
import kotlinx.android.synthetic.main.drawer_header.view.*
import kotlinx.android.synthetic.main.recycler_allitems_row.view.*

class RecyclerViewAdapter(val arrayList: ArrayList<ItemModel>, val context: Context):
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindItems(model: ItemModel) {
            itemView.txtName.text = model.name
            itemView.txtSellerName.text = model.sellerName
            itemView.txtPrice.text = model.price.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_allitems_row, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(arrayList[position])
    }
}