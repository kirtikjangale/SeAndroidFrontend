package com.example.seandroidproject.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.seandroidproject.R
import com.example.seandroidproject.adapter.MyListingsAdapter
import com.example.seandroidproject.model.ItemMyListing
import com.kirtik.foodrunner.util.ConnectionManager


class MyListingsFragment : Fragment() {

    lateinit var recyclerMyListings : RecyclerView
    lateinit var layoutManager : RecyclerView.LayoutManager
    lateinit var recyclerAdapter : MyListingsAdapter
    lateinit var loader : RelativeLayout
    lateinit var sharedPreferences: SharedPreferences
    lateinit var spinner: Spinner
    var wasteCategory = "Ewaste"
    var itemList  = arrayListOf<ItemMyListing>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view =  inflater.inflate(R.layout.fragment_my_listings, container, false)

        sharedPreferences = activity!!.getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)
        loader = view.findViewById(R.id.progressBar)

        //
        recyclerMyListings = view.findViewById(R.id.recyclerMyListings)
        layoutManager = GridLayoutManager(activity as Context,2)


        spinner = view.findViewById(R.id.spinner)
        val category = arrayOf("ELECTRONICS", "NOTEBOOKS", "TEXTBOOKS")
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            activity as Context,
            android.R.layout.simple_spinner_item,
            category
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                loader.visibility = View.VISIBLE

                val item = parent.getItemAtPosition(pos)
                wasteCategory = item.toString()

                if(wasteCategory=="ELECTRONICS"){
                    fetchJson("ewaste")
                }
                else if(wasteCategory == "NOTEBOOKS"){
                    fetchJson("notewaste")

                }
                else if(wasteCategory == "TEXTBOOKS"){
                    fetchJson("textwaste")
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        return view
    }

    fun fetchJson(category:String){

        if(ConnectionManager().checkConnectivity(activity as Context)){
            val queue = Volley.newRequestQueue(activity as Context)
            val url = "https://se-course-app.herokuapp.com/$category/all/me"

            view?.findViewById<LinearLayout>(R.id.no_item_modal)?.visibility = View.INVISIBLE
            view?.findViewById<RecyclerView>(R.id.recyclerMyListings)?.visibility = View.VISIBLE


            itemList.clear()

            val jsonRequest = @SuppressLint("SetTextI18n")
            object : JsonArrayRequest(Request.Method.GET, url, null,
                Response.Listener {

                    try {

                        //println("mylistings:$it")

                        for(i in 0 until it.length()){
                            val jsonObject = it.getJSONObject(i)
                           // println("test:$jsonObject")

                            val item = ItemMyListing(
                                jsonObject.getString("name"),
                                jsonObject.getInt("price").toString(),
                                jsonObject.getString("_id"),
                                jsonObject.getString("thumbnail"),
                                category

                            )

                            itemList.add(item)
                            loader.visibility = View.GONE
                            if(!itemList.isEmpty()) {
                                recyclerAdapter = MyListingsAdapter(
                                    activity as Context,
                                    itemList,
                                    "Bearer " + sharedPreferences.getString("userToken", "-1")
                                )
                                recyclerMyListings.adapter = recyclerAdapter
                                recyclerMyListings.layoutManager = layoutManager
                            }
                        }

                        if (itemList.isEmpty()){
                            loader.visibility = View.GONE
                            view?.findViewById<LinearLayout>(R.id.no_item_modal)?.visibility = View.VISIBLE
                            view?.findViewById<RecyclerView>(R.id.recyclerMyListings)?.visibility = View.INVISIBLE
                        }


                        //println("itemlist:$itemList")

                    } catch (e: Exception) {
                        loader.visibility = View.GONE
                        println(e)
                        Toast.makeText(activity as Context, "Exception", Toast.LENGTH_SHORT)
                            .show()
                    }

                }, Response.ErrorListener {
                    Toast.makeText(activity as Context, "$it", Toast.LENGTH_SHORT).show()
                }){

                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["content-type"] = "application/json"
                    headers["Authorization"] = "Bearer "+ sharedPreferences.getString(
                        "userToken",
                        "-1"
                    ).toString()
                    return  headers
                }
            }


            queue.add(jsonRequest)
        }
        else{
            loader.visibility = View.GONE
            view?.findViewById<LinearLayout>(R.id.no_item_modal)?.visibility = View.VISIBLE
            view?.findViewById<Spinner>(R.id.spinner)?.visibility = View.INVISIBLE
            view?.findViewById<TextView>(R.id.error_text)?.text = "Failed to load your listings"

            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("My Listings Error")
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

}