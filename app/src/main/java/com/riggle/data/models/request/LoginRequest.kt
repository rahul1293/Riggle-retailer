package com.riggle.data.models.request

import com.google.gson.annotations.SerializedName
import com.riggle.data.models.response.ProductsData

data class Login(
    //val mobile_no: String
    val mobile: String,
    val token: String,
    val bypass: Boolean
)

data class OTPVerification(
    /*val mobile_no: String,
    val otp: String*/
    val mobile: String,
    val otp: String,
    val name: String,
    val bypass: Boolean,
)

data class UploadOrder(
    val redeemed_riggle_coins: Long,
    val delivery_date: String,
    val retailer: Int
)


data class UpdateRetailerOne(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("update_url") var updateUrl: String? = null,
    @SerializedName("account_status") var accountStatus: String? = null,
    @SerializedName("riggle_coins_balance") var riggleCoinsBalance: Int? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null,
    @SerializedName("is_deleted") var isDeleted: Boolean? = null,
    @SerializedName("deleted_at") var deletedAt: String? = null,
    @SerializedName("code") var code: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("address") var address: String? = null,
    @SerializedName("landmark") var landmark: String? = null,
    @SerializedName("pincode") var pincode: String? = null,
    @SerializedName("store_type") var storeType: String? = null,
    @SerializedName("store_location") var storeLocation: String? = null,
    @SerializedName("image") var image: String? = null,
    @SerializedName("doc_id") var docId: String? = null,
    @SerializedName("cart_data") var cartData: CartData? = CartData(),
    @SerializedName("is_active") var isActive: Boolean? = null,
    @SerializedName("sub_area") var subArea: Int? = null

)


data class CartData(

    @SerializedName("amount") var amount: Int? = null,
    @SerializedName("products") var products: List<ProductsData> = arrayListOf(),
    @SerializedName("final_amount") var finalAmount: Int? = null,
    @SerializedName("riggle_coins") var riggleCoins: Int? = null,
    @SerializedName("redeemed_riggle_coins") var redeemedRiggleCoins: Int? = null

)

