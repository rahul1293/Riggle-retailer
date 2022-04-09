package com.riggle.data.models.response

import com.google.gson.annotations.SerializedName

data class ResponseUserData(
        var user_data: UserData
)

data class  UserData(
        var created: String,
        var login_otp: String,
        var login_otp_time: String,
        var mobile: String,
        var modified: String,
        var session_key: String,
        var status: String,
        var store_name: String,
        var city: String,
        var state: String,
        var alternate_no: String,
        var category_of_products: String,
        var g_latitude: String,
        var g_longitude: String,
        var gstn_no: String,
        var parent_id: String,
        var riggle_coins_bal: Int,
        var total_riggle_coins: Int,
        var user_type: String,
        var role_type: String,
        var image_path: String,
        var gstn_doc: String,
        var profile_img: String,
        @SerializedName("locked_keys")
        var lockedKeys : LockedKeys,

        /* New Apis Data*/
        var retailer : UserDetails,
        val id: Int,
        val update_url: String,
        val account_status: String,
        val is_deleted: Boolean,
        val deleted_at: String,
        val code: String,
        var name: String,
        var address: String,
        val landmark: String,
        var pincode: String,
        val store_type: String,
        val store_location: String,
        val image: String,
        val doc_id: String,
        val is_active: Boolean,
        val sub_area: Boolean,
        val created_at: String,
        val updated_at: String

)

