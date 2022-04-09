package com.riggle.data.models.response

import com.google.gson.annotations.SerializedName

data class ResponseCartData(
    @SerializedName("products")
    var products_in_cart: ArrayList<ProductsData>,
    //var cart_details: CartDetails,
    var usableCoins: Double,
    var amount: Double,
    var final_amount: Double,
    var riggle_coins: Double,
    var redeemed_riggle_coins: Float,
    var margin: Double
)

data class CartDetails(
    val total_price: Double? = null,
    val total_discount: Int? = null,
    val riggle_coins_discount: Float? = null,
    val final_amount: Double? = null,
    val total_riggle_coins: Int? = null,
    val total_profit: String? = null,
    val estimated_delivery: String? = null,
    val usableCoins: Int? = null
)

data class EditCartResponse(
    val cart_details: CartDetails,
    val current_object: ProductsData,
    val parent_object: ProductsData
)