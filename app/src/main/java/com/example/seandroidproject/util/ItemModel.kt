package com.example.seandroidproject.util

data class ItemModel (
    var name: String = "Product Name",
    var sellerName: String = "Seller Name",
    var price: Double? = 0.0,
    var imgURL: String = ""
) {}