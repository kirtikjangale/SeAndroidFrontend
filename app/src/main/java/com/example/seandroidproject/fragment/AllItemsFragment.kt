package com.example.seandroidproject.fragment

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
import kotlinx.android.synthetic.main.fragment_all_items.*


class AllItemsFragment : Fragment() {

    lateinit var recyclerAllItems : RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_all_items, container, false)

        recyclerAllItems = view.findViewById(R.id.recycler_allitems)

        // dummy data
        val arrayList = ArrayList<ItemModel>()
        arrayList.add(ItemModel("OnePlus 1", "Nikhil1", 3.9, ""))
        arrayList.add(ItemModel("OnePlus 2", "Nikhil2", 39.9, ""))
        arrayList.add(ItemModel("OnePlus 3", "Nikhil3", 399.9, ""))
        arrayList.add(ItemModel("OnePlus 4", "Nikhil4", 3999.9, ""))
        arrayList.add(ItemModel("OnePlus 5", "Nikhil5", 39999.9, ""))
        arrayList.add(ItemModel("OnePlus 6", "Nikhil6", 39999.9, ""))
        arrayList.add(ItemModel("OnePlus 7", "Nikhil7", 39999.9, ""))

        val itemsListAdapter = RecyclerViewAdapter(arrayList, activity as Context)
        recyclerAllItems.layoutManager = LinearLayoutManager(activity)
        recyclerAllItems.adapter = itemsListAdapter

        // Inflate the layout for this fragment
        // just some basic data to display
        return view
    }

}
