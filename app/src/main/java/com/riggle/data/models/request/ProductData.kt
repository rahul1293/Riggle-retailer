package com.riggle.data.models.request

data class RequestCartData(var cart_data: ArrayList<VariantUpdate>)

data class VariantUpdate(val /*product_id*/product: Int?, val quantity: Int, val product_combo: Int?)

data class RequestToAddCart(var retailer: Int, var products: ArrayList<VariantUpdate>)

data class ProductCartRequest(var products: ArrayList<VariantUpdate>)