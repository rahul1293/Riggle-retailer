package com.riggle.data.models.request

data class RequestCartData(var cart_data: ArrayList<VariantUpdate>)

data class VariantUpdate(
    val /*product_id*/product: Int?,
    val quantity: Int,
    val product_combo: Int?
)

data class RequestToAddCart(var retailer: Int, var products: ArrayList<VariantUpdate>)

data class ProductCartRequest(var products: ArrayList<VariantUpdate>)

data class RequestComboUpdate(
    val products: List<VariantUpdate>
)

data class PostCartRequest(
    val coupon_code: Int,
    val products: List<VariantUpdate>,
    val redeemed_riggle_coins: Int
)

data class RequestCouponApply(
    val coupon_code: String?,
    val redeemed_riggle_coins: String?
)
