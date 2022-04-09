package com.riggle.data.models.request

data class UserProfileUpdateRequest(
    var pin_code: String? = null,
    var store_name: String? = null,
    var store_type: String? = null,
    var address: String? = null,
    var name: String? = null,
    var alternate_no: String? = null,
    var gstnd_no: String? = null,
    var g_latitude: String? = null,
    var g_longitude: String? = null,
    var category_of_products: String? = null,
    var image_path: String? = null,
    var role_type :String?,
    var gstn_doc :String?
)

