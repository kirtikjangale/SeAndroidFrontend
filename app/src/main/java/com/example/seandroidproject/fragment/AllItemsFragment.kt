package com.example.seandroidproject.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.seandroidproject.R
import com.example.seandroidproject.util.ItemModel
import com.example.seandroidproject.util.RecyclerViewAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException


class AllItemsFragment(default_pincode: String, default_category: String) : Fragment() {

    lateinit var recyclerAllItems : RecyclerView

    var pincode_root = default_pincode
    var category_root = default_category

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_all_items, container, false)

        // initial fetch
        recyclerAllItems = view.findViewById(R.id.recycler_allitems)
        recyclerAllItems.layoutManager = LinearLayoutManager(activity)
        fetchJson(pincode_root, category_root)

        // add event listener to pin_code
        val btn_pin_code: TextView = view.findViewById(R.id.btn_pin_code)
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
                dialog.dismiss()
                btn_pin_code.text = pincode_root
                fetchJson(pincode_root, category_root)
            }

            dialog.setContentView(dialog_view)
            dialog.show()

        }

        // add event listener to filter category
        val btn_filter: TextView = view.findViewById(R.id.btn_filter)
        when (category_root) {
            "ewaste" -> {
                btn_filter.text = "E-Waste"
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
                        btn_filter.text = "E-Waste"
                        category_root = "ewaste"
                    }
                    R.id.category_textwaste -> {
                        editCategoryView.check(R.id.category_textwaste)
                        btn_filter.text = "Text Books"
                        category_root = "textwaste"
                    }
                    R.id.category_notewaste -> {
                        editCategoryView.check(R.id.category_notewaste)
                        btn_filter.text = "Note Books"
                        category_root = "notewaste"
                    }
                }

                dialog.dismiss()
                fetchJson(pincode_root, category_root)
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
        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val resBody = response?.body?.string()
                println(resBody)

                val gson = GsonBuilder().create()
                val itemListData =  gson.fromJson(resBody, Array<ItemModel>::class.java).toList()
//                println(itemListData)

                val itemsListAdapter = RecyclerViewAdapter(itemListData, activity as Context)

                activity!!.runOnUiThread{
                    recyclerAllItems.adapter = itemsListAdapter
                }
            }
            override fun onFailure(call: Call, e: IOException) {
                println("Req. failed")
            }
        })
    }

}
