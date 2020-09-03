package com.example.seandroidproject.fragment

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.seandroidproject.R
import com.example.seandroidproject.util.ItemModel
import com.example.seandroidproject.util.ProfileModel
import com.example.seandroidproject.util.RecyclerViewAdapterWishlist
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.GsonBuilder
import okhttp3.*
import org.json.JSONException
import java.io.IOException
import java.lang.Override as Override


class ProfileFragment : Fragment() {

    lateinit var sharedPreferences: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val profile_name: TextView = view.findViewById(R.id.txt_profile_name)
        val profile_email: TextView = view.findViewById(R.id.txt_profile_email)
        val profile_pincode: TextView = view.findViewById(R.id.txt_profile_pincode)
        val profile_location: TextView = view.findViewById(R.id.txt_profile_location)
        val profile_phone: TextView = view.findViewById(R.id.txt_profile_phone)

        val btn_name: Button = view.findViewById(R.id.btn_name_edit)
        val btn_email: Button = view.findViewById(R.id.btn_email_edit)
        val btn_pincode: Button = view.findViewById(R.id.btn_pincode_edit)
        val btn_phone: Button = view.findViewById(R.id.btn_phone_edit)
        val btn_location: Button = view.findViewById(R.id.btn_location_edit)

        val btn_done: Button = view.findViewById(R.id.btn_profile_done)

        btn_name.setOnClickListener {
            activity!!.runOnUiThread{
                Toast.makeText(activity as Context, "loading info., please wait...", Toast.LENGTH_SHORT).show()
            }
        }
        btn_email.setOnClickListener {
            activity!!.runOnUiThread{
                Toast.makeText(activity as Context, "loading info., please wait...", Toast.LENGTH_SHORT).show()
            }
        }
        btn_phone.setOnClickListener {
            activity!!.runOnUiThread{
                Toast.makeText(activity as Context, "loading info., please wait...", Toast.LENGTH_SHORT).show()
            }
        }
        btn_pincode.setOnClickListener {
            activity!!.runOnUiThread{
                Toast.makeText(activity as Context, "loading info., please wait...", Toast.LENGTH_SHORT).show()
            }
        }
        btn_location.setOnClickListener {
            activity!!.runOnUiThread{
                Toast.makeText(activity as Context, "loading info., please wait...", Toast.LENGTH_SHORT).show()
            }
        }
        btn_done.setOnClickListener {
            activity!!.runOnUiThread{
                Toast.makeText(activity as Context, "loading info., please wait...", Toast.LENGTH_SHORT).show()
            }
        }

        sharedPreferences = activity!!.getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)

        val url = "https://se-course-app.herokuapp.com/users/me"

        val client = OkHttpClient()

        val token = sharedPreferences.getString("userToken","-1")

        // authentication is hardcoded
        val request = okhttp3.Request.Builder().url(url).addHeader("Authorization", "Bearer $token").build()

        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: okhttp3.Response) {
                val resBody = response?.body?.string()
                println(resBody)

                val gson = GsonBuilder().create()
                val ProfileData = gson.fromJson(resBody, ProfileModel::class.java)
                println(ProfileData)

                activity!!.runOnUiThread {
                    profile_name.text = ProfileData.name
                    profile_email.text = ProfileData.email
                    profile_phone.text = ProfileData.phone.toString()
                    profile_location.text = ProfileData.location
                    profile_pincode.text = ProfileData.pincode.toString()

                    val dialog = BottomSheetDialog(activity as Context)
                    val dialog_view = layoutInflater.inflate(R.layout.bottom_pincode_dialog, null)

                    btn_name.setOnClickListener {

                        dialog_view.findViewById<TextView>(R.id.txtChangePinCode).text =
                            "Change Profile Name"

                        val editProfileName: EditText = dialog_view.findViewById(R.id.editPinCode)
                        editProfileName.setText(profile_name.text)

                        val cancelProfileName: Button =
                            dialog_view.findViewById(R.id.btn_cancel_pincode)
                        cancelProfileName.setOnClickListener {
                            dialog.dismiss()
                        }

                        val changeProfileNameBtn: Button =
                            dialog_view.findViewById(R.id.btn_change_pincode)
                        changeProfileNameBtn.setOnClickListener {
                            profile_name.text = editProfileName.text.toString()
                            dialog.dismiss()
                        }

                        dialog.setContentView(dialog_view)
                        dialog.show()
                    }

                    btn_email.setOnClickListener {

                        dialog_view.findViewById<TextView>(R.id.txtChangePinCode).text =
                            "Change  Profile Email"

                        val editProfileEmail: EditText = dialog_view.findViewById(R.id.editPinCode)
                        editProfileEmail.setText(profile_email.text)

                        val cancelProfileEmail: Button =
                            dialog_view.findViewById(R.id.btn_cancel_pincode)
                        cancelProfileEmail.setOnClickListener {
                            dialog.dismiss()
                        }

                        val changeProfileEmailBtn: Button =
                            dialog_view.findViewById(R.id.btn_change_pincode)
                        changeProfileEmailBtn.setOnClickListener {
                            profile_email.text = editProfileEmail.text.toString()
                            dialog.dismiss()
                        }

                        dialog.setContentView(dialog_view)
                        dialog.show()
                    }

                    btn_pincode.setOnClickListener {

                        dialog_view.findViewById<TextView>(R.id.txtChangePinCode).text =
                            "Change Pin Code"

                        val editProfilePincode: EditText =
                            dialog_view.findViewById(R.id.editPinCode)
                        editProfilePincode.setText(profile_pincode.text)

                        val cancelProfilePincode: Button =
                            dialog_view.findViewById(R.id.btn_cancel_pincode)
                        cancelProfilePincode.setOnClickListener {
                            dialog.dismiss()
                        }

                        val changeProfilePincodeBtn: Button =
                            dialog_view.findViewById(R.id.btn_change_pincode)
                        changeProfilePincodeBtn.setOnClickListener {
                            profile_pincode.text = editProfilePincode.text.toString()
                            dialog.dismiss()
                        }

                        dialog.setContentView(dialog_view)
                        dialog.show()
                    }

                    btn_phone.setOnClickListener {

                        dialog_view.findViewById<TextView>(R.id.txtChangePinCode).text =
                            "Change Profile Phone"

                        val editProfilePhone: EditText = dialog_view.findViewById(R.id.editPinCode)
                        editProfilePhone.setText(profile_phone.text)

                        val cancelProfilePhone: Button =
                            dialog_view.findViewById(R.id.btn_cancel_pincode)
                        cancelProfilePhone.setOnClickListener {
                            dialog.dismiss()
                        }

                        val changeProfilePhoneBtn: Button =
                            dialog_view.findViewById(R.id.btn_change_pincode)
                        changeProfilePhoneBtn.setOnClickListener {
                            profile_phone.text = editProfilePhone.text.toString()
                            dialog.dismiss()
                        }

                        dialog.setContentView(dialog_view)
                        dialog.show()
                    }

                    btn_location.setOnClickListener {

                        dialog_view.findViewById<TextView>(R.id.txtChangePinCode).text =
                            "Change Profile Location"

                        val editProfileLocation: EditText =
                            dialog_view.findViewById(R.id.editPinCode)
                        editProfileLocation.setText(profile_location.text)

                        val cancelProfileLocation: Button =
                            dialog_view.findViewById(R.id.btn_cancel_pincode)
                        cancelProfileLocation.setOnClickListener {
                            dialog.dismiss()
                        }

                        val changeProfileLocationBtn: Button =
                            dialog_view.findViewById(R.id.btn_change_pincode)
                        changeProfileLocationBtn.setOnClickListener {
                            profile_location.text = editProfileLocation.text.toString()
                            dialog.dismiss()
                        }

                        dialog.setContentView(dialog_view)
                        dialog.show()

                    }

                    btn_done.setOnClickListener {
                        val requestBody: RequestBody = MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("name", profile_name.text.toString())
                            .addFormDataPart("email", profile_email.text.toString())
                            .addFormDataPart("phone", profile_phone.text.toString())
                            .addFormDataPart("pincode", profile_pincode.text.toString())
                            .addFormDataPart("location", profile_location.text.toString())
                            .build()

                        // authentication is hardcoded
                        val update_request = okhttp3.Request.Builder()
                            .url("https://se-course-app.herokuapp.com/users/update")
                            .addHeader("Authorization", "Bearer $token")
                            .patch(requestBody)
                            .build()


                        client.newCall(update_request).enqueue(object: Callback {
                            override fun onResponse(call: Call, response: okhttp3.Response) {
                                val resBody = response?.body?.string()
                                println(resBody)

                                activity!!.runOnUiThread {
                                    Toast.makeText(activity as Context, "updated profile successfully", Toast.LENGTH_SHORT).show()
                                }

                            }
                            override fun onFailure(call: Call, e: IOException) {
                                println("Req. failed")

                                activity!!.runOnUiThread {
                                    Toast.makeText(activity as Context, "updating profile failed, try again later", Toast.LENGTH_SHORT).show()
                                }
                            }
                        })
                    }

                }

            }
            override fun onFailure(call: Call, e: IOException) {
                println("Req. failed")
                activity!!.run {
                    Toast.makeText(activity as Context, "failed to load info., try again later", Toast.LENGTH_SHORT).show()
                }
            }
        })
        return view

        }
    }