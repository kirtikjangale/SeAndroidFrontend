package com.example.seandroidproject.util

data class ItemModel (
    var name: String = "Product Name",
    var price: Double? = 0.0,
    var _id: String = "",
    var used_for: String = "1 day",
    var pincode: Int = 0,
    var photos: Array<String>,
    var thumbnail: String = ""
) {}