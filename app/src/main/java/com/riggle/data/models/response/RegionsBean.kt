package com.riggle.data.models.response

data class RegionsBean(
    val id: Int, val update_url: String, val code: String, val name: String,
    val is_active: Boolean, val type: String, val belongs: Int,
    val created_at: String, val updated_at: String
)

data class UserDetails(
    /*val id: Int,
    val update_url: String,
    val account_status: String,
    val is_deleted: Boolean,
    val deleted_at: String,
    val code: String,
    val name: String,
    val address: String,
    val landmark: String,
    val pincode: String,
    val store_type: String,
    val store_location: String,
    val image: String,
    val doc_id: String,
    val is_active: Boolean,
    val sub_area: Boolean,
    val created_at: String,
    val updated_at: ,*/
    var id: Int,
    var update_url: String?,
    var account_status: String?,
    var riggle_coins_balance: Int,
    var created_at: String?,
    var updated_at: String?,
    var is_deleted: Boolean,
    var deleted_at: String?,
    var code: String?,
    var name: String?,
    var address: String?,
    var landmark: String?,
    var pincode: String?,
    var store_type: String?,
    var store_location: String?,
    var image: String?,
    var doc_id: String?,
    var is_active: Boolean,
    var sub_area: Int,
    var is_serviceable: Boolean
    //var sub_area: SubArea
)

data class RetailerDetailBean(
    var id: Int,
    var update_url: String?,
    var account_status: String?,
    var riggle_coins_balance: Int,
    var created_at: String?,
    var updated_at: String?,
    var is_deleted: Boolean,
    var deleted_at: String?,
    var code: String?,
    var name: String?,
    var address: String?,
    var landmark: String?,
    var pincode: String?,
    var store_type: String?,
    var store_location: String?,
    var image: String?,
    var doc_id: String?,
    var is_active: Boolean,
    var is_serviceable: Boolean,
    var sub_area: SubArea
)

data class SubArea(
    var id: Int,
    var update_url: String?,
    var created_at: String?,
    var updated_at: String?,
    var code: String?,
    var name: String?,
    var is_active: Boolean?,
    var type: String?,
    var belongs: Belongs?
)

data class Belongs(
    var id: Int,
    var update_url: String?,
    var created_at: String?,
    var updated_at: String?,
    var code: String?,
    var name: String?,
    var is_active: Boolean?,
    var type: String?,
    var belongs: Int?
)