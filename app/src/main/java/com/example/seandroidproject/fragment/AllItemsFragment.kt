package com.example.seandroidproject.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.seandroidproject.R
import com.example.seandroidproject.model.ItemModel
import com.example.seandroidproject.adapter.RecyclerViewAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException


class AllItemsFragment(default_pincode: String, default_category: String) : Fragment() {

    lateinit var recyclerAllItems : RecyclerView

    lateinit var loader : RelativeLayout

    lateinit var sharedPreferences: SharedPreferences

    var pincode_root = default_pincode
    var category_root = default_category


    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_all_items, container, false)
        sharedPreferences = activity!!.getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)

        loader = view.findViewById(R.id.progressBar)

        sharedPreferences = activity!!.getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)

        // initial fetch
        recyclerAllItems = view.findViewById(R.id.recycler_allitems)
        recyclerAllItems.layoutManager = GridLayoutManager(activity, 2)

        pincode_root = sharedPreferences.getString("pincodeFilter","517619").toString()
        category_root = sharedPreferences.getString("categoryFilter","ewaste").toString()

        fetchJson(pincode_root, category_root)

        // add event listener to pin_code
        val btn_pin_code: Button = view.findViewById(R.id.btn_pin_code)
        btn_pin_code.text = pincode_root.toString()

        btn_pin_code.setOnClickListener {
//            println("pin_code clicked")
            val dialog = BottomSheetDialog(activity as Context)
            val dialog_view = layoutInflater.inflate(R.layout.bottom_pincode_dialog, null)

            val editPinCodeView: EditText = dialog_view.findViewById(R.id.editPinCode)
            editPinCodeView.setText(pincode_root)

            val cancelPinCodeBtn: Button = dialog_view.findViewById(R.id.btn_cancel_pincode)
            cancelPinCodeBtn.setOnClickListener{
                dialog.dismiss()
            }

            val changePinCodeBtn: Button = dialog_view.findViewById(R.id.btn_change_pincode)
            changePinCodeBtn.setOnClickListener{
                pincode_root = editPinCodeView.text.toString()
                sharedPreferences.edit().putString("pincodeFilter",editPinCodeView.text.toString()).apply()
                dialog.dismiss()
                btn_pin_code.text = pincode_root
                loader.visibility = View.VISIBLE
                fetchJson(pincode_root, category_root)
            }

            dialog.setContentView(dialog_view)
            dialog.show()

        }

        // add event listener to filter category
        val btn_filter: TextView = view.findViewById(R.id.btn_filter)
        when (category_root) {
            "ewaste" -> {
                btn_filter.text = "Electronics"
            }
            "textwaste" -> {
                btn_filter.text = "Text Books"
            }
            "notewaste" -> {
                btn_filter.text = "Note Books"
            }
        }

        btn_filter.setOnClickListener {
//            println("pin_code clicked")
            val dialog = BottomSheetDialog(activity as Context)
            val dialog_view = layoutInflater.inflate(R.layout.bottom_category_dialog, null)

            var editCategoryView: RadioGroup = dialog_view.findViewById(R.id.category_group)
            when (category_root) {
                "ewaste" -> {
                    editCategoryView.check(R.id.category_ewaste)
                }
                "textwaste" -> {
                    editCategoryView.check(R.id.category_textwaste)
                }
                "notewaste" -> {
                    editCategoryView.check(R.id.category_notewaste)
                }
            }

            val cancelPinCodeBtn: Button = dialog_view.findViewById(R.id.btn_cancel_category)
            cancelPinCodeBtn.setOnClickListener{
                dialog.dismiss()
            }

            val changePinCodeBtn: Button = dialog_view.findViewById(R.id.btn_change_category)
            changePinCodeBtn.setOnClickListener{
                val new_category_id = editCategoryView.checkedRadioButtonId

                when (new_category_id) {
                    R.id.category_ewaste -> {
                        editCategoryView.check(R.id.category_ewaste)
                        btn_filter.text = "Electronics"
                        category_root = "ewaste"
                        sharedPreferences.edit().putString("categoryFilter",category_root).apply()
                    }
                    R.id.category_textwaste -> {
                        editCategoryView.check(R.id.category_textwaste)
                        btn_filter.text = "Text Books"
                        category_root = "textwaste"
                        sharedPreferences.edit().putString("categoryFilter",category_root).apply()
                    }
                    R.id.category_notewaste -> {
                        editCategoryView.check(R.id.category_notewaste)
                        btn_filter.text = "Note Books"
                        category_root = "notewaste"
                        sharedPreferences.edit().putString("categoryFilter",category_root).apply()
                    }
                }

                dialog.dismiss()

                fetchJson(pincode_root, category_root)
                loader.visibility = View.VISIBLE
            }

            dialog.setContentView(dialog_view)
            dialog.show()

        }

        // Inflate the layout for this fragment
        // just some basic data to display
        return view
    }

    fun fetchJson(pincode: String, category: String){
        println("fetching JSON data from backend...")

        val base_url = "https://se-course-app.herokuapp.com"
        val url = "$base_url/$category/all/$pincode"

        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        val token = sharedPreferences.getString("userToken", "-1")

        try {
            client.newCall(request).enqueue(object: Callback {
                override fun onResponse(call: Call, response: Response) {
                    val resBody = response?.body?.string()
                    println(resBody)

                    val gson = GsonBuilder().create()
                    val itemListData =  gson.fromJson(resBody, Array<ItemModel>::class.java).toList()
                    println("itemlist$itemListData")
                    var wishlist: MutableList<String> = arrayListOf()
                    var itemsListAdapter = RecyclerViewAdapter(itemListData, wishlist, activity as Context, category_root)
//                    println("token")
//                    println(token)

                    if(token != "-1") {
                        val wishlist_req = okhttp3.Request.Builder()
                            .url("https://se-course-app.herokuapp.com/users/me")
                            .addHeader("Authorization", "Bearer $token")
                            .build()

                        client.newCall(wishlist_req).enqueue(object : Callback {
                            override fun onFailure(call: Call, e: IOException) {
                                println("Req. failed")
                                activity!!.runOnUiThread {
                                    loader.visibility = View.GONE
                                    view?.findViewById<LinearLayout>(R.id.no_item_modal)?.visibility =
                                        View.VISIBLE
                                    view?.findViewById<RecyclerView>(R.id.recycler_wishlist)?.visibility =
                                        View.INVISIBLE
                                    view?.findViewById<TextView>(R.id.error_text)?.text =
                                        "Something went wrong please try again later"

                                    val dialog = AlertDialog.Builder(activity as Context)
                                    dialog.setTitle("Main Page Error")
                                    dialog.setMessage("Internet Connection Not Found")
                                    dialog.setPositiveButton("Open Settings") { _, _ ->
                                        val settingsIntent =
                                            Intent(Settings.ACTION_WIRELESS_SETTINGS)
                                        startActivity(settingsIntent)
                                        activity!!.finish()
                                    }
                                    dialog.setNegativeButton("Cancel") { _, _ ->
                                        ActivityCompat.finishAffinity(activity!!)
                                    }
                                    dialog.create()
                                    dialog.show()
                                }
                            }

                            override fun onResponse(call: Call, response: Response) {
                                val resBody = response?.body?.string()
                                val gson = GsonBuilder().create()
                                val wishListData = gson.fromJson(resBody, WishList::class.java)
                                wishlist = wishListData.wishlist.toMutableList()

                                itemsListAdapter =
                                    RecyclerViewAdapter(itemListData, wishlist, activity as Context, category_root)

                                activity!!.runOnUiThread {
                                    if (itemListData.isEmpty()) {
                                        view?.findViewById<LinearLayout>(R.id.no_item_modal)?.visibility =
                                            View.VISIBLE
                                        view?.findViewById<RecyclerView>(R.id.recycler_allitems)?.visibility =
                                            View.INVISIBLE
                                    } else {
                                        view?.findViewById<LinearLayout>(R.id.no_item_modal)?.visibility =
                                            View.INVISIBLE
                                        view?.findViewById<RecyclerView>(R.id.recycler_allitems)?.visibility =
                                            View.VISIBLE
                                    }
                                    loader.visibility = View.GONE
                                    recyclerAllItems.adapter = itemsListAdapter
                                }
                            }

                        })
                    }
                    else{
                        activity!!.runOnUiThread{
                            if(itemListData.isEmpty()){
                                view?.findViewById<LinearLayout>(R.id.no_item_modal)?.visibility = View.VISIBLE
                                view?.findViewById<RecyclerView>(R.id.recycler_allitems)?.visibility = View.INVISIBLE
                            }
                            else{
                                view?.findViewById<LinearLayout>(R.id.no_item_modal)?.visibility = View.INVISIBLE
                                view?.findViewById<RecyclerView>(R.id.recycler_allitems)?.visibility = View.VISIBLE
                            }
                            loader.visibility = View.GONE
                            recyclerAllItems.adapter = itemsListAdapter
                        }



                    }



                    }



                override fun onFailure(call: Call, e: IOException) {

                    loader.visibility = View.GONE
                    println("Req. failed")
                    activity!!.runOnUiThread{

                        view?.findViewById<LinearLayout>(R.id.no_item_modal)?.visibility = View.VISIBLE
                        view?.findViewById<RecyclerView>(R.id.recycler_wishlist)?.visibility = View.INVISIBLE
                        view?.findViewById<TextView>(R.id.error_text)?.text = "Something went wrong please try again later"

                        val dialog = AlertDialog.Builder(activity as Context)
                        dialog.setTitle("Main Page Error")
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
        catch (err: Exception) {
            loader.visibility = View.GONE
            view?.findViewById<LinearLayout>(R.id.no_item_modal)?.visibility = View.VISIBLE
            view?.findViewById<RecyclerView>(R.id.recycler_wishlist)?.visibility = View.INVISIBLE
            view?.findViewById<TextView>(R.id.error_text)?.text = "Something went wrong please try again later"
        }

    }

}

data class WishList(
  var wishlist: Array<String>
){}
