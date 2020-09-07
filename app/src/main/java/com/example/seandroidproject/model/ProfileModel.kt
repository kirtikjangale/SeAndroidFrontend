package com.example.seandroidproject.model

data class ProfileModel (
    var name: String = "Chadwick Boseman",
    var phone: Long,
    var _id: String = "",
    var location: String = "",
    var pincode: Int,
    var email: String
) {}