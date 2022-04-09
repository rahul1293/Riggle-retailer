package com.riggle.data.models.request

import com.google.gson.annotations.SerializedName

class StoreInfo {
    var pin_code: String? = null
    var store_name: String? = null
    var store_type: String? = null
    @SerializedName("user_type")
    var role_type: String? = null
    var address: String? = null
}