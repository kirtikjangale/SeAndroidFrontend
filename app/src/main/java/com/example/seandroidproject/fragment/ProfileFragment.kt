package com.example.seandroidproject.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.seandroidproject.R
import org.json.JSONException


class ProfileFragment : Fragment() {

    lateinit var sharedPreferences: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        sharedPreferences = activity!!.getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)

        val queue = Volley.newRequestQueue(activity as Context)
        val url = "https://se-course-app.herokuapp.com/users/me"

        val jsonObjectRequest = object : JsonObjectRequest(
            Request.Method.GET,url,null, Response.Listener {

            try {
                Toast.makeText(activity as Context,"Success", Toast.LENGTH_SHORT).show()
                println(it)
            }
            catch (e : JSONException){
                Toast.makeText(activity as Context,"$e", Toast.LENGTH_SHORT).show()
                println(e)
            }


        },
            Response.ErrorListener {
                if(activity !=null){
                    Toast.makeText(activity as Context,"$it", Toast.LENGTH_SHORT).show()
                }
            }){

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String,String>()
                headers["Content-Type"]="application/json"
                headers["Authorization"] = "Bearer "+ sharedPreferences.getString("userToken","-1")
                return headers
            }
        }

        queue.add(jsonObjectRequest)

        return view
    }


}